package com.seculayer.mrms.rest.servlet.insert;

import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class InsertMLAlgInfoServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/insert_ml_algorithm_info";

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - insert ml algorithm info");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            projectDAO.insertMLAlgorithmInfo(map);
            logger.debug(map.toString());

            out.println("1");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        logger.debug("###################################################################");
    }
}
