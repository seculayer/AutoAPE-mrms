package com.seculayer.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static String getJSONString(BufferedReader reader) throws IOException {
        String line;
        StringBuilder mapStr = new StringBuilder();
        try {
            while((line = reader.readLine()) != null){
                mapStr.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapStr.toString();
    }

    public static Map<String, Object> getMapFromString(BufferedReader reader) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        return JsonUtil.strToMap(JsonUtil.getJSONString(reader).toString());
    }

    public static List<Map<String, Object>> getListMapFromString(BufferedReader reader) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        return JsonUtil.strToListMap(JsonUtil.getJSONString(reader).toString());
    }

    public static Map<String, Object> strToMap(String data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {};
        return objectMapper.readValue(data, typeReference);
    }

    public static List<Map<String, Object>> strToListMap(String data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<HashMap<String, Object>>> typeReference = new TypeReference<List<HashMap<String, Object>>>() {};
        return objectMapper.readValue(data, typeReference);
    }

    public static JSONObject mapToJson(Map<String, Object> map) throws JSONException {
        JSONObject jsonData = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonData.put(key, value);
        }
        return jsonData;
    }
}
