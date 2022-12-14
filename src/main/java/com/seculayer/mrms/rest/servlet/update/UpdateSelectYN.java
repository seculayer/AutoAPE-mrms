package com.seculayer.mrms.rest.servlet.update;

import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UpdateSelectYN extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/update_model_select";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json; charset=utf-8");
        PrintWriter out = resp.getWriter();
        logger.debug("###################################################################");
        logger.debug("In doGet");
        try {
            String learnHistNo = req.getParameter("learn_hist_no");
            String projectID = req.getParameter("project_id");

            Map<String, Object> map = new HashMap<>();
            map.put("learn_hist_no", learnHistNo);
            map.put("project_id", projectID);

            projectDAO.updateSelectYN(map);

            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("error");
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
