package com.seculayer.mrms.info;

import com.seculayer.mrms.db.CommonDAO;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EDAInfo extends InfoAbstract {
    protected String edaID = "";
    protected String dpAnalysisID = "";
    protected String projectID = "";
    protected Map<String, Object> dataAnalsInfo;

    protected CommonDAO dao = new CommonDAO();


    public EDAInfo(String key) { super(key); }

    @Override
    public void init(Map<String, Object> map) {
        edaID = map.get("eda_id").toString();
        dpAnalysisID = map.get("dp_analysis_id").toString();
        projectID = map.get("project_id").toString();
        dataAnalsInfo = dao.selectDataAnlsID(map);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<?, ?> metadataJson = mapper.readValue(dataAnalsInfo.get("metadata_json").toString(), Map.class);
            dataAnalsInfo.replace("metadata_json", metadataJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected File infoFile() {
        if (this.infoFile == null){
            infoFile = new File(outputDir + "/eda", "EDA_" + this.key + ".job");
        }
        return infoFile;
    }

    @Override
    public Map<String, Object> makeInfoMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("eda_id", edaID);
        map.put("dp_analysis_id", dpAnalysisID);
        map.put("project_id", projectID);
        map.put("data_anals_info", dataAnalsInfo);

        return map;
    }

    @Override
    public Map<String, Object> loadInfo(String key) {
        return null;
    }
}
