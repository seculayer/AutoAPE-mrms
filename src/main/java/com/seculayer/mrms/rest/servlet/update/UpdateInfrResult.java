package com.seculayer.mrms.rest.servlet.update;

import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UpdateInfrResult extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/update_infr_result";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doGet - update inference result");

        try {
            String accuracy = httpServletRequest.getParameter("infr_accuracy");
            String f1score = httpServletRequest.getParameter("infr_f1score");
            String infrHistNo = httpServletRequest.getParameter("infr_hist_no");
            logger.debug("infr_hist_no: {}, Accuracy: {}, F1-Score: {}", infrHistNo, accuracy, f1score);

            Map<String, Object> map = new HashMap<>();
            map.put("infr_hist_no", infrHistNo);
            map.put("infr_accuracy", accuracy);
            map.put("infr_f1score", f1score);

            commonDAO.updateInferenceRst(map);

            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
