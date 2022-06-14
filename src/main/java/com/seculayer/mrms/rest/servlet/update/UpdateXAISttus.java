package com.seculayer.mrms.rest.servlet.update;

import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class UpdateXAISttus extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/status_update/xai";

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - update xai status");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            logger.debug(map.toString());

            commonDAO.updateXAISttusCd(map);

            out.println("1");
        }catch (Exception e){
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");

    }
}
