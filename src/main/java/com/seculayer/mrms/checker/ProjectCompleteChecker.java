package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.info.LearnInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.Request;
import com.seculayer.util.JsonUtil;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.*;

public class ProjectCompleteChecker extends Checker {
    private ProjectManageDAO projectDAO = new ProjectManageDAO();
    private CommonDAO commonDAO = new CommonDAO();
    private static final Map<String, Object> modelResourceMap = MRMServerManager.getInstance().getModelResourceMap();
    private static final Map<String, Object> modelsInfoMap = MRMServerManager.getInstance().getModelsInfoMap();

    @Override
    public void doCheck() throws CheckerException {
        List<Map<String, Object>> recommendingProjectList = projectDAO.selectProjectSchedule(Constants.STATUS_PROJECT_LEARN_ING);

        for (Map<String, Object> idMap : recommendingProjectList) {
            List<Map<String, Object>> schedules = projectDAO.selectLearningModelList(idMap);
            int cntModel = schedules.size();

            int completeCnt = 0;
            int errorCnt = 0;
            for (Map<String, Object> schd: schedules) {
                if (schd.get("learn_sttus_cd").toString().equals(Constants.STATUS_LEARN_COMPLETE)) {
                    completeCnt++;
                    this.mergeEvalResult(schd);
                    this.updateLearnLog(idMap, schd);
                    this.deleteLearnJob(idMap, schd);
                }
                else if (schd.get("learn_sttus_cd").toString().equals(Constants.STATUS_LEARN_ERROR)) {
                    errorCnt++;
                    this.updateLearnLog(idMap, schd);
                    this.deleteLearnJob(idMap, schd);
                }
            }

            if (completeCnt > 0 && cntModel == completeCnt + errorCnt) {
                // 완료 상태 업데이트
                idMap.replace("status", Constants.STATUS_PROJECT_COMPLETE);
                projectDAO.updateStatus(idMap);
//                this.deleteRCMDJob(idMap);
                this.deleteResourceMonitoring(schedules);
                this.removeJobFolder(idMap);
            }
            else if (errorCnt == cntModel) {
                idMap.replace("status", Constants.STATUS_PROJECT_ERROR);
                projectDAO.updateStatus(idMap);
//                this.deleteRCMDJob(idMap);
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
                map.put("worker_" + idx, log);
            }
            JSONObject jsonData = JsonUtil.mapToJson(map);
            schd.put("logs", jsonData.toString());
            projectDAO.updateLearnLog(schd);
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

    public void deleteRCMDJob(Map<String, Object> idMap) {
        String projectID = idMap.get("project_id").toString();
        V1PodList podList = Request.getPodList();

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

    public void deleteLearnJob(Map<String, Object> idMap, Map<String, Object> model) {
        String modelHistNo = model.get("learn_hist_no").toString();
        String projectID = idMap.get("project_id").toString();
        V1PodList podList = Request.getPodList();
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

    public void deleteResourceMonitoring(List<Map<String, Object>> modelList) {
        for (Map<String, Object> modelMap : modelList) {
            String histNo = modelMap.get("learn_hist_no").toString();

            modelResourceMap.remove(histNo);
            modelsInfoMap.remove(histNo);
        }
    }

    public void mergeEvalResult(Map<String, Object> learnHistInfo) {
        String histNo = learnHistInfo.get("learn_hist_no").toString();
        List<Object> totalList = null;
        ObjectMapper mapper = new ObjectMapper();

        String evalRstStr = commonDAO.selectEvalResult(histNo);

        try {
            Map<String, Object> evalRstMap = mapper.readValue(evalRstStr, Map.class);
            for (String key : evalRstMap.keySet()) {
                List<Object> rstList = mapper.readValue(evalRstMap.get(key).toString(), List.class);
                if (totalList == null) {
                    totalList = rstList;
                } else {
                    for (int i = 0; i < rstList.size(); i++) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> tmpLabelRst = (Map<String, Object>) rstList.get(i);
                        @SuppressWarnings("unchecked")
                        Map<String, Object> totalLabelRst = (Map<String, Object>) totalList.get(i);
                        for (String labelKey : tmpLabelRst.keySet()) {
                            String updateVal = Integer.toString(Integer.parseInt(totalLabelRst.get(labelKey).toString()) + Integer.parseInt(tmpLabelRst.get(labelKey).toString()));
                            totalLabelRst.put(labelKey, updateVal);
                        }
                    }
                }
            }
        } catch (JsonMappingException e){

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (totalList != null) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hist_no", histNo);
            try {
                paramMap.put("result", mapper.writeValueAsString(totalList));
            } catch (Exception e) {
                e.printStackTrace();
                paramMap.put("result", "[]");
            }
            commonDAO.updateEvalResult(paramMap);
        }

    }
}
