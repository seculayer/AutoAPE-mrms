package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.rest.ServletHandlerAbstract;
import com.seculayer.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetLearningLogs extends ServletHandlerAbstract{
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_learn_logs";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doGet - get learning logs");

        String id = httpServletRequest.getParameter("id");
        boolean tail = StringUtil.getBoolean(httpServletRequest.getParameter("tail"));

        try {
            String logs = projectDAO.selectLearnLog(id);
            out.println(logs);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
