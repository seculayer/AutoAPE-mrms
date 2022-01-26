package com.seculayer.mrms.rest.servlet.select;

import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class GetInferenceInfo extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_inference_info";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        logger.debug("###################################################################");
        logger.debug("In doPost - get inference info");

        try {
            List<Map<String, Object>> returnList;
            String projectId = httpServletRequest.getParameter("project_id");

            returnList = commonDAO.selectInferenceInfo(projectId);

            logger.debug(returnList.toString());
            String jsonStr = mapper.writeValueAsString(returnList);

            out.println(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
