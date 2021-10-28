package com.seculayer.mrms.info;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class DAInfo extends InfoAbstract {
    // Common
    protected String datasetID = "";
    protected String datasetFormat = "1";
    protected String datasetSize = "0";
    protected String nCols = "0";
    protected String nRows = "0";
    protected Map<String, Object> formatJSON = null;

    public DAInfo(String key) { super(key); }

    @Override
    public void init(Map<String, Object> map) {
        datasetID = map.get("dataset_id").toString();
        datasetFormat = map.get("dataset_format").toString();
        datasetSize = map.get("dataset_size").toString();
        nCols = map.get("n_cols").toString();
        nRows = map.get("n_rows").toString();

        ObjectMapper mapper = new ObjectMapper();
        try {
            formatJSON = mapper.readValue(map.get("format_json").toString(), Map.class);
            String[] field_list = formatJSON.get("field_list").toString().split(",");
            formatJSON.replace("field_list", field_list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> makeInfoMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("dataset_id", datasetID);
        map.put("dataset_format", datasetFormat);
        map.put("dataset_size", datasetSize);
        map.put("n_cols", nCols);
        map.put("n_rows", nRows);
        map.put("format_json", formatJSON);

        return map;
    }

    @Override
    public Map<String, Object> loadInfo(String key) {
        return null;
    }

    public String getDatasetId() { return datasetID; }
}
