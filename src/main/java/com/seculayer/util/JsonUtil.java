package com.seculayer.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public static Map<String, Object> getMapFromString(BufferedReader reader) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();

        String line;
        StringBuilder mapStr = new StringBuilder();
        try {
            while((line = reader.readLine()) != null){
                mapStr.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonUtil.strToMap(mapStr.toString());
    }

    public static Map<String, Object> strToMap(String data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {};
        return objectMapper.readValue(data, typeReference);
    }
}
