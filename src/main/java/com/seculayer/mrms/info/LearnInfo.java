package com.seculayer.mrms.info;

import com.seculayer.mrms.common.Constants;
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

public class LearnInfo extends InfoAbstract {
    protected String learnHistNo = "";
    protected String projectID = "";
    protected String learnStartTime = "";
    protected String paramID = "";
    protected String algID = "";
    protected String algAnlsID = "";
    protected String dpAnalysisID = "";
    protected double eduPer;
    protected int numWorker;
    protected String sampleTypeCd = "";
    protected Map<String, Object> daAnlsInfo = null;
    protected Map<String, Object> dpAnlsInfo = null;
    protected Map<String, Object> mlParamInfo = null;
    protected Map<String, Object> algInfo = null;
    protected boolean gpuUse = false;
    protected String targetField = "";

    protected CommonDAO commonDAO = new CommonDAO();
    protected ProjectManageDAO projectDAO = new ProjectManageDAO();

    public LearnInfo() {}
    public LearnInfo(String key) {
        super(key);
    }
    public LearnInfo(String key, String projectID) {
        super(key);
        this.projectID = projectID;
    }

    protected File infoFile() {
        if (this.infoFile == null){
            infoFile = new File(outputDir + "/" + projectID, this.key + ".job");
        }
        return infoFile;
    }

    @Override
    public void init(Map<String, Object> map) {
        learnHistNo = map.get("learn_hist_no").toString();
        projectID = map.get("project_id").toString();
        learnStartTime = map.get("start_time").toString();
        paramID = map.get("param_id").toString();
        algID = map.get("alg_id").toString();
        algAnlsID = map.get("alg_anal_id").toString();
        dpAnalysisID = map.get("dp_analysis_id").toString();

        daAnlsInfo = commonDAO.selectDataAnlsID(map);
        dpAnlsInfo = projectDAO.selectDpAnlsInfo(map);
        mlParamInfo = projectDAO.selectMLParamInfo(map);
        algInfo = commonDAO.selectAlgInfo(map);

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

        algInfo.put("learning", "Y");
        algInfo.put("alg_sn", "0");
        algInfo.put("global_sn", "0");
        algInfo.put("global_step", MRMServerManager.getInstance().getConfiguration().get("global_steps", "10"));
        algInfo.put("method_type", "Basic");
        algInfo.put("model_nm", learnHistNo);
        daAnlsInfo.put("fields", dpAnlsInfo);

        eduPer = 80;
        sampleTypeCd = "1";
        gpuUse = this.isGpuUse(algInfo);
        targetField = projectDAO.selectTargetField(map);


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

    @Override
    public Map<String, Object> makeInfoMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("learn_hist_no", learnHistNo);
        map.put("key", key);
        map.put("project_id", projectID);
        map.put("datasets", daAnlsInfo);
        map.put("algorithms", algInfo);
        map.put("edu_per", eduPer);
        map.put("num_worker", numWorker);
        map.put("gpu_use", gpuUse);
        map.put("sample_type_cd", sampleTypeCd);
        map.put("project_target_field", targetField);

        return map;
    }

    @Override
    public Map<String, Object> loadInfo(String key) {
        File file = new File(outputDir + "/" + projectID, "learn_" + key + ".job");

        try {
            Map<String, Object> map = JsonUtil.strToMap(
                JsonUtil.getJSONString(new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))));

            this.learnHistNo = StringUtil.get(map.get("learn_hist_no"));
            this.key = StringUtil.get(map.get("key"));
            this.projectID = StringUtil.get(map.get("project_id"));
            this.daAnlsInfo = (Map<String, Object>) map.get("datasets");
            this.algInfo = (Map<String, Object>) map.get("algorithms");
            this.eduPer = StringUtil.getDouble(map.get("edu_per"));
            this.numWorker = StringUtil.getInt(map.get("num_worker"));
            this.gpuUse = StringUtil.getBoolean(map.get("gpu_use"));
            this.sampleTypeCd = StringUtil.get(map.get("sample_type_cd"));
            this.targetField = StringUtil.get(map.get("project_target_field"));
            logger.debug("load learn info : {}", key);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void setNumWorker(int numFile){
        numWorker = "Y".equals(algInfo.get("dist_yn").toString()) ? numFile : 1;
    }

    public synchronized boolean isGpuUse(Map<String, Object> algInfo){
        switch (algInfo.get("lib_type").toString()){
            case Constants.LIB_TYPE_TFV1: case Constants.LIB_TYPE_KERAS: case Constants.LIB_TYPE_TFV2:
                return gpuUseExceptions(algInfo.get("algorithm_code").toString(), true);
            default:
                return gpuUseExceptions(algInfo.get("algorithm_code").toString(), false);
        }
    }

    public static boolean gpuUseExceptions(String algorithmCode, boolean result){
        // library type exceptions
        switch (algorithmCode){
            // 1. TFSTSV1 -> None GPU Use.

            default:
                return result;
        }
    }

    public int getNumWorker() { return numWorker; }
    public boolean getGpuUse() { return gpuUse; }
    public String getLearnHistNo() { return learnHistNo; }
    public Map<String, Object> getAlgInfo() { return algInfo; }

}
