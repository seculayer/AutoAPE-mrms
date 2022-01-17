package com.seculayer.mrms.info;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.util.JsonUtil;
import com.seculayer.util.StringUtil;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InferenceInfo extends InfoAbstract {
    protected CommonDAO commonDAO = new CommonDAO();
    protected ProjectManageDAO projectDAO= new ProjectManageDAO();

    protected String infrHistNo = "";
    protected String learnHistNo = "";
    protected Map<String, Object> learnHistMap = null;
    protected String projectID = "";
    protected String paramID = "";
    protected String algID = "";
    protected String algAnlsID = "";
    protected String dpAnalysisID = "";

    protected Map<String, Object> daAnlsInfo = null;
    protected Map<String, Object> dpAnlsInfo = null;
    protected Map<String, Object> mlParamInfo = null;
    protected Map<String, Object> algInfo = null;
    protected String datasetFormat = "";

    protected boolean gpuUse = false;
    protected String targetField = "";
    protected int numWorker = 1;


    public InferenceInfo(String key) { super(key); }

    protected File infoFile() {
        if (this.infoFile == null){
            infoFile = new File(outputDir + "/inference", this.key + ".job");
        }
        return infoFile;
    }

    @Override
    public void init(Map<String, Object> map) {
        infrHistNo = map.get("infr_hist_no").toString();
        learnHistNo = map.get("learn_hist_no").toString();
        learnHistMap = commonDAO.selectLearnHistInfo(learnHistNo);
        targetField = map.get("target_field").toString();
        this.setInfo(learnHistMap);

        String dataAnlsID = map.get("data_analysis_id").toString();
        daAnlsInfo = commonDAO.selectDataAnlsInfoWithDataAnalsID(dataAnlsID);
        datasetFormat = commonDAO.selectDatasetFormat(dataAnlsID);
        daAnlsInfo.put("fields", dpAnlsInfo);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<?,?> metadataJson = mapper.readValue(daAnlsInfo.get("metadata_json").toString(), Map.class);
            daAnlsInfo.replace("metadata_json", metadataJson);
            List<?> fileList = ((List<?>) metadataJson.get("file_list"));
            this.setNumWorker(fileList.size());

            Map<String, Object> params = mapper.readValue(mlParamInfo.get("param_json").toString(), Map.class);
            params.put("early_key", "0");
            params.put("early_type", "0");
            params.put("early_value", "10");
            params.put("minsteps", "10");
            algInfo.put("params", params);

            List<?> dpAnlsJson = mapper.readValue(dpAnlsInfo.get("data_analysis_json").toString(), List.class);
            daAnlsInfo.put("fields", dpAnlsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setInfo(Map<String, Object> learnHistMap) {
        paramID = learnHistMap.get("param_id").toString();
        algID = learnHistMap.get("alg_id").toString();
        algAnlsID = learnHistMap.get("alg_anal_id").toString();
        dpAnalysisID = learnHistMap.get("dp_analysis_id").toString();
        projectID = learnHistMap.get("project_id").toString();

        dpAnlsInfo = projectDAO.selectDpAnlsInfo(learnHistMap);
        mlParamInfo = projectDAO.selectMLParamInfo(learnHistMap);
        algInfo = commonDAO.selectAlgInfo(learnHistMap);

        this.setAlgInfo();
        gpuUse = this.isGpuUse(algInfo);
    }

    protected void setAlgInfo() {
        String projectPurpose = commonDAO.selectProjectInfo(projectID).get("project_purpose_cd").toString();
        switch (projectPurpose){
            case "2":
                algInfo.put("algorithm_type", "Regressor");
                break;
            case "7":
                algInfo.put("algorithm_type", "OD");
                break;
            default:
                algInfo.put("algorithm_type", "Classifier");
        }
        algInfo.put("learning", "N");
        algInfo.put("alg_sn", "0");
        algInfo.put("global_sn", "0");
        algInfo.put("global_step", MRMServerManager.getInstance().getConfiguration().get("global_steps", "10"));
        algInfo.put("method_type", "Basic");
        algInfo.put("model_nm", learnHistNo);
    }

    @Override
    public Map<String, Object> makeInfoMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("learn_hist_no", learnHistNo);
        map.put("infr_hist_no", infrHistNo);
        map.put("key", key);
        map.put("project_id", projectID);
        map.put("datasets", daAnlsInfo);
        map.put("algorithms", algInfo);
        map.put("num_worker", numWorker);
        map.put("gpu_use", gpuUse);
        map.put("project_target_field", targetField);
        map.put("dataset_format", datasetFormat);

        return map;
    }

    @Override
    public Map<String, Object> loadInfo(String key) {
        File file = new File(outputDir + "/inference", "inference_" + key + ".job");

        try {
            Map<String, Object> map = JsonUtil.strToMap(
                JsonUtil.getJSONString(new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))));

            this.learnHistNo = StringUtil.get(map.get("learn_hist_no"));
            this.infrHistNo = StringUtil.get(map.get("infr_hist_no"));
//            this.projectID = StringUtil.get(map.get("project_id"));
//            this.daAnlsInfo = (Map<String, Object>) map.get("datasets");
//            this.algInfo = (Map<String, Object>) map.get("algorithms");
//            this.eduPer = StringUtil.getDouble(map.get("edu_per"));
//            this.numWorker = StringUtil.getInt(map.get("num_worker"));
//            this.gpuUse = StringUtil.getBoolean(map.get("gpu_use"));
//            this.sampleTypeCd = StringUtil.get(map.get("sample_type_cd"));
//            this.targetField = StringUtil.get(map.get("project_target_field"));
//            this.datasetFormat = StringUtil.get(map.get("dataset_format"));
            logger.debug("load inference info : {}", key);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void setNumWorker(int numFile){
        numWorker = "Y".equals(algInfo.get("dist_yn").toString()) ? numFile : 1;
    }
    public String getInfrHistNo() { return infrHistNo; }
    public int getNumWorker() { return numWorker; }
    public boolean getGpuUse() { return gpuUse; }
}
