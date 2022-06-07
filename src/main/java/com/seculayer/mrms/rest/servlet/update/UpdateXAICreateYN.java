package com.seculayer.mrms.rest.servlet.update;

import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UpdateXAICreateYN extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/xai_create_yn_update";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doGet");

        Map<String, Object> map = new HashMap<>();
        try {
            map.put("infr_hist_no", httpServletRequest.getParameter("infr_hist_no"));
            commonDAO.updateXAICreateYN(map);

            out.print("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("error");
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
