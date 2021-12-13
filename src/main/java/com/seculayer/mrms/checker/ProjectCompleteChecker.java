package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.info.LearnInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.Request;
import com.seculayer.util.JsonUtil;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectCompleteChecker extends Checker {
    private ProjectManageDAO dao = new ProjectManageDAO();
    private static final Map<String, Object> modelResourceMap = MRMServerManager.getInstance().getModelResourceMap();

    @Override
    public void doCheck() throws CheckerException {
        List<Map<String, Object>> recommendingProjectList = dao.selectProjectSchedule(Constants.STATUS_PROJECT_LEARN_ING);

        for (Map<String, Object> idMap : recommendingProjectList) {
            List<Map<String, Object>> schedules = dao.selectLearningModelList(idMap);
            int cntModel = schedules.size();

            int completeCnt = 0;
            int errorCnt = 0;
            for (Map<String, Object> schd: schedules) {
                if (schd.get("learn_sttus_cd").toString().equals(Constants.STATUS_LEARN_COMPLETE)) {
                    completeCnt++;
                    this.updateLearnLog(idMap, schd);
                }
                else if (schd.get("learn_sttus_cd").toString().equals(Constants.STATUS_LEARN_ERROR)) {
                    errorCnt++;
                    this.updateLearnLog(idMap, schd);
                }
            }

            if (cntModel == completeCnt && errorCnt == 0) {
                // 완료 상태 업데이트
                idMap.replace("status", Constants.STATUS_PROJECT_COMPLETE);
                dao.updateStatus(idMap);
                this.deleteKubeResources(idMap, schedules);
                this.deleteResourceMonitoring(schedules);
                this.removeJobFolder(idMap);
            }
            else if (errorCnt > 0) {
                idMap.replace("status", Constants.STATUS_PROJECT_ERROR);
                dao.updateStatus(idMap);
                this.deleteKubeResources(idMap, schedules);
                this.deleteResourceMonitoring(schedules);
            }
        }
    }

    public void updateLearnLog(Map<String, Object> idMap, Map<String, Object> schd) {
        String projectID = idMap.get("project_id").toString();
        String histNo = schd.get("learn_hist_no").toString();
        boolean tail = false;
        Map<String, Object> map = new HashMap<>();

        try {
            LearnInfo learnInfo = new LearnInfo(histNo, projectID);
            learnInfo.loadInfo(histNo);

            for (int idx = 0; idx < learnInfo.getNumWorker(); idx++) {
                String log = KubeUtil.getJobLogs("learn-" + histNo + "-" + idx, "mlps", tail);
                System.out.println(log);
                map.put("worker_" + idx, log);
            }
            JSONObject jsonData = JsonUtil.mapToJson(map);
            schd.put("logs", jsonData.toString());
            dao.updateLearnLog(schd);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void removeJobFolder(Map<String, Object> idMap) {
        String projectID = idMap.get("project_id").toString();
        String folderPath = MRMServerManager.getInstance().getConfiguration().get("ape.job.dir") + "/" + projectID;
        File f = new File(folderPath);
        try {
            FileUtils.cleanDirectory(f);
            f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteKubeResources(Map<String, Object> idMap, List<Map<String, Object>> modelList) {
        // delete job
        String projectID = idMap.get("project_id").toString();
        V1PodList podList = Request.getPodList();

        try {
            // In case Kubernetes < v1.22, It must enable
//            this.deleteRCMDJob(projectID, podList);
            this.deleteLearnJob(projectID, modelList, podList);  // for service delete
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRCMDJob(String projectID, V1PodList podList) {
        Request.deleteJob(String.format("dprs-%s-0", projectID));
        Request.deleteJob(String.format("hprs-%s-0", projectID));
        Request.deleteJob(String.format("mars-%s-0", projectID));

        for (V1Pod item : podList.getItems()) {
            String podName = item.getMetadata().getName();
            if (podName.contains(String.format("dprs-%s-0", projectID)) ||
                podName.contains(String.format("hprs-%s-0", projectID)) ||
                podName.contains(String.format("mars-%s-0", projectID))) {
                Request.deletePod(podName);
            }
        }
    }

    public void deleteLearnJob(String projectID, List<Map<String, Object>> modelList, V1PodList podList) {
        for(Map<String, Object> model: modelList) {
            String modelHistNo = model.get("learn_hist_no").toString();
            LearnInfo loadedInfo = new LearnInfo(modelHistNo, projectID);
            loadedInfo.loadInfo(modelHistNo);
            int numWorker = loadedInfo.getNumWorker();

            for(int i=0; i<numWorker; i++){
                String name = String.format("learn-%s-%s", modelHistNo, i);
                // In case Kubernetes < v1.22, It must enable
//                Request.deleteJob(name);
                Request.deleteService(name);

//                for (V1Pod item : podList.getItems()) {
//                    String podName = item.getMetadata().getName();
//                    if (podName.contains(name)) {
//                        Request.deletePod(podName);
//                    }
//                }
            }
        }
    }

    public void deleteResourceMonitoring(List<Map<String, Object>> modelList) {
        for (Map<String, Object> modelMap : modelList) {
            String histNo = modelMap.get("learn_hist_no").toString();

            modelResourceMap.remove(histNo);
        }
    }
}
