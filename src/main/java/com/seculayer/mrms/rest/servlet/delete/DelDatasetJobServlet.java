package com.seculayer.mrms.rest.servlet.delete;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.Request;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import com.seculayer.util.JsonUtil;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class DelDatasetJobServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/delete_dataset_job";


    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doGet - Delete DA Job");

        try {
            String datasetID = httpServletRequest.getParameter("dataset_id");
            logger.debug("In doGet - dataset_id : {}", datasetID);

            this.deleteDAJob(datasetID);
            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    private void deleteDAJob(String datasetID) {
        String outputDir = MRMServerManager.getInstance().getConfiguration().get("ape.features.dir");
        File file = new File(outputDir, "DA_META_" + datasetID + ".meta");
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
