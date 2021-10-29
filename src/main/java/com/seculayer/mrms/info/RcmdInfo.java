package com.seculayer.mrms.info;

import com.seculayer.mrms.db.CommonDAO;

import java.util.HashMap;
import java.util.Map;

public class RcmdInfo extends InfoAbstract {
    protected String projectID = "";
    protected String projectPurposeCd = "0";
    protected String dataAnalysisID = "";
    protected String startTime = "";
    protected Map<String, Object> dataAnalsInfo = null;
    protected CommonDAO commonDAO = new CommonDAO();

    public RcmdInfo(String key) {
        super(key);
    }

    @Override
    public void init(Map<String, Object> map) {
        projectID = map.get("project_id").toString();
        projectPurposeCd = map.get("project_purpose_cd").toString();
        dataAnalysisID = map.get("data_analysis_id").toString();
        startTime = map.get("start_time").toString();
        dataAnalsInfo = commonDAO.selectDataAnlsInfoWithDataAnalsID(dataAnalysisID);
    }

    @Override
    public Map<String, Object> makeInfoMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("project_id", projectID);
        map.put("project_purpose_cd", projectPurposeCd);
        map.put("data_analysis_id", dataAnalysisID);
        map.put("start_time", startTime);
        map.put("data_anls_info", dataAnalsInfo);

        return map;
    }

    @Override
    public Map<String, Object> loadInfo(String key) {
        return null;
    }

    public String getProjectID() {
        return projectID;
    }
}
