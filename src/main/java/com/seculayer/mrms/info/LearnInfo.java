package com.seculayer.mrms.info;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.db.ProjectManageDAO;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
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

    protected CommonDAO commonDAO = new CommonDAO();
    protected ProjectManageDAO projectDAO = new ProjectManageDAO();

    public LearnInfo(String key) {
        super(key);
    }

    protected File infoFile() {
        if (this.infoFile == null){
            infoFile = new File(outputDir, this.key + ".job");
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

        algInfo.put("learning", "Y");
        daAnlsInfo.put("fields", dpAnlsInfo);

        eduPer = 80;
        sampleTypeCd = "1";
        gpuUse = this.isGpuUse(algInfo);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<?,?> metadataJson = mapper.readValue(daAnlsInfo.get("metadata_json").toString(), Map.class);
            daAnlsInfo.replace("metadata_json", metadataJson);
            List<?> fileList = ((List<?>) metadataJson.get("file_list"));
            this.setNumWorker(fileList.size());

            algInfo.put("params", mapper.readValue(mlParamInfo.get("param_json").toString(), Map.class));
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

        return map;
    }

    @Override
    public Map<String, Object> loadInfo(String key) {
        return null;
    }

    public void setNumWorker(int numFile){
        numWorker = "Y".equals(algInfo.get("dist_yn").toString()) ? numFile : 1;
    }

    public synchronized boolean isGpuUse(Map<String, Object> algInfo){
        switch (algInfo.get("lib_type").toString()){
            case Constants.LIB_TYPE_TFV1: case Constants.LIB_TYPE_KERAS: case Constants.LIB_TYPE_TFV2:
                return gpuUseExceptions(algInfo.get("alg_cls").toString(), true);
            default:
                return gpuUseExceptions(algInfo.get("alg_cls").toString(), false);
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
