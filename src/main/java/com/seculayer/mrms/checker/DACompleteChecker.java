package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.Request;
import com.seculayer.util.JsonUtil;

import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class DACompleteChecker extends Checker {
    private CommonDAO dao = new CommonDAO();

    @Override
    public void doCheck() throws CheckerException {
        List<Map<String, Object>> reqList = dao.selectDASchedule(Constants.STATUS_DA_RM_REQ);

        for (Map<String, Object> req: reqList) {
            String datasetID = req.get("dataset_id").toString();

            // In case Kubernetes < v1.22, It must enable
            // this.deleteDAJob(datasetID);
            this.removeInitFoler(datasetID);
            req.replace("status_cd", Constants.STATUS_DA_COMPLETE);
            dao.updateDAStatus(req);
        }
    }

    private void removeInitFoler(String datasetID) {
        String folderPath = MRMServerManager.getInstance().getConfiguration().get("ape.job.dir") + "/" + datasetID;
        File f = new File(folderPath);
        try {
            FileUtils.cleanDirectory(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteDAJob(String datasetID) {
        String outputDir = MRMServerManager.getInstance().getConfiguration().get("ape.da.dir");
        File file = new File(outputDir + "/" + datasetID, "DA_META_" + datasetID + ".info");
        Map<String, Object> map = null;
        int numWorker = 1;
        try {
            map = JsonUtil.strToMap(
                JsonUtil.getJSONString(new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), Charset.defaultCharset())
                ))
            );
            List<?> fileList = ((List<?>) map.get("file_list"));
            numWorker = fileList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Request.deleteJob(String.format("da-chief-%s-0", datasetID));
        for (int i=0; i<numWorker; i++) {
            Request.deleteJob(String.format("da-worker-%s-%s", datasetID, i));
        }

        V1PodList podList = Request.getPodList();

        for (V1Pod item : podList.getItems()) {
            String podName = item.getMetadata().getName();
            if (podName.contains(String.format("da-chief-%s", datasetID)) ||
                    podName.contains(String.format("da-worker-%s", datasetID))) {
                Request.deletePod(podName);
            }
        }
    }
}
