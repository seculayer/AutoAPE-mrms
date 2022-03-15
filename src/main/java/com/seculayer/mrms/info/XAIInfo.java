package com.seculayer.mrms.info;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.db.ProjectManageDAO;
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

public class XAIInfo extends InfoAbstract {
    protected CommonDAO commonDAO = new CommonDAO();
    protected ProjectManageDAO projectDAO= new ProjectManageDAO();

    protected String xaiHistNo = "";
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

    protected String targetField = "";

    public XAIInfo(String key) { super(key); }

    protected File infoFile() {
        if (this.infoFile == null){
            infoFile = new File(outputDir + "/xai", this.key + ".job");
        }
        return infoFile;
    }

    @Override
    public void init(Map<String, Object> map) {
        xaiHistNo = map.get("xai_hist_no").toString();
        infrHistNo = map.get("infr_hist_no").toString();
        learnHistNo = map.get("learn_hist_no").toString();
        learnHistMap = commonDAO.selectLearnHistInfo(learnHistNo);
        this.setInfo(learnHistMap);

        targetField = map.get("target_field").toString();
        String dataAnlsID = map.get("data_analysis_id").toString();
        daAnlsInfo = commonDAO.selectDataAnlsInfoWithDataAnalsID(dataAnlsID);
        datasetFormat = commonDAO.selectDatasetFormat(dataAnlsID);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<?,?> metadataJson = mapper.readValue(daAnlsInfo.get("metadata_json").toString(), Map.class);
            daAnlsInfo.replace("metadata_json", metadataJson);

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
    }

    @Override
    public Map<String, Object> makeInfoMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("xai_hist_no", xaiHistNo);
        map.put("learn_hist_no", learnHistNo);
        map.put("infr_hist_no", infrHistNo);
        map.put("key", xaiHistNo);
        map.put("project_id", projectID);
        map.put("datasets", daAnlsInfo);
        map.put("algorithms", algInfo);
        map.put("project_target_field", targetField);
        map.put("dataset_format", datasetFormat);

        return map;
    }

    @Override
    public Map<String, Object> loadInfo(String key) {
        File file = new File(outputDir + "/xai", Constants.JOB_TYPE_XAI + "_" + key + ".job");

        try {
            Map<String, Object> map = JsonUtil.strToMap(
                JsonUtil.getJSONString(new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))));

            this.xaiHistNo = StringUtil.get(map.get("xai_hist_no"));
            this.learnHistNo = StringUtil.get(map.get("learn_hist_no"));
            this.infrHistNo = StringUtil.get(map.get("infr_hist_no"));
            logger.debug("load xai info : {}", key);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public String getXAIHistNo() { return xaiHistNo; }
}
