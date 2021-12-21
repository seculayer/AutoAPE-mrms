package com.seculayer.mrms.rest.servlet.update;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UpdateResultServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/learn_result_update";

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - update learn result");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            logger.debug(map.toString());
            commonDAO.updateLearnResult(map);

            String learnHistNo = map.get("hist_no").toString();
            String mlResultJson = map.get("result").toString();
            try {
                Map<String, Object> info = (Map<String, Object>) MRMServerManager.getInstance().getModelsInfoMap().get(learnHistNo);
                info.put("ml_result_json", mlResultJson);
            } catch (Exception e) {
                e.printStackTrace();
            }

            out.println("1");
        }catch (Exception e){
            logger.error(e.toString());
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
