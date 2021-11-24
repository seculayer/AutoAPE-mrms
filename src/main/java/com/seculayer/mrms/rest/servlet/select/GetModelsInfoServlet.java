package com.seculayer.mrms.rest.servlet.select;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetModelsInfoServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_models_info";
    private static final Map<String, Object> modelResourceMap = MRMServerManager.getInstance().getModelResourceMap();

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        logger.debug("###################################################################");
        logger.debug("In doPost - get models info");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            logger.debug(map.toString());

            List<Map<String, Object>> rst = commonDAO.selectModelsInfo(map);

            for (Map<String, Object> model: rst) {
                String learnHistNo = model.get("learn_hist_no").toString();

                Map<String, Object> resourceMap = (Map<String, Object>) modelResourceMap.getOrDefault(learnHistNo, new HashMap<>());
                model.put("resource_usage", mapper.writeValueAsString(resourceMap));
            }

            String jsonStr = mapper.writeValueAsString(rst);
            out.println(jsonStr);
        } catch (Exception e) {
            logger.error(e.toString());
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
