package com.seculayer.mrms.rest.servlet.select;

import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class GetDataAnalsInfo extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_data_anls_info";

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        logger.debug("###################################################################");
        logger.debug("In doPost - get data analysis info");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            logger.debug(map.toString());

            Map<String, Object> rst = commonDAO.selectDataAnlsInfo(map);
            Map<String, Object> metadataJson = mapper.readValue(rst.get("metadata_json").toString(), Map.class);
            List<Object> metaList = (List<Object>) metadataJson.get("meta");
            for (Object meta: metaList) {
                Map<String, Object> metaMap = (Map<String, Object>) meta;
                Map<String, Object> statistics = (Map<String, Object>) metaMap.get("statistics");
                statistics.remove("word");
                statistics.remove("unique");
            }
            rst.put("metadata_json", mapper.writeValueAsString(metadataJson));

            String jsonStr = mapper.writeValueAsString(rst);
            out.println(jsonStr);

        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
