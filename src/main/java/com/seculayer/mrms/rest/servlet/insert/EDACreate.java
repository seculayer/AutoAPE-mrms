package com.seculayer.mrms.rest.servlet.insert;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class EDACreate extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/create_eda";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        logger.debug("###################################################################");
        String dpAnalysisID = httpServletRequest.getParameter("dp_analysis_id");
        String projectID = httpServletRequest.getParameter("project_id");
        logger.debug(
            "In doGet - create Exploratory Data Analyze, dp_analysis_id : {}, project_id : {}",
            dpAnalysisID, projectID
        );
        Map<String, Object> map = new HashMap<>();
        map.put("dp_analysis_id", dpAnalysisID);
        map.put("project_id", projectID);
        map.put("status", Constants.STATUS_EDA_REQ);
        projectDAO.insertEDAInfo(map);

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
