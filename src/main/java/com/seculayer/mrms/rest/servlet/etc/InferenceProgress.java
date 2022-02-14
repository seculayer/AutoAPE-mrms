package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import com.seculayer.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class InferenceProgress extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/inference_progress_rate";
    public static final Map<String, Object> inferenceProgressRate = MRMServerManager.getInstance().getInferenceProgressRate();

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - inference progress");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            if (map.get("mode").toString().equals("update")) {
                inferenceProgressRate.put(
                    map.get("infr_hist_no").toString(),
                    map.get("progress_rate").toString()
                );
            }
            else { // delete
                inferenceProgressRate.remove(map.get("infr_hist_no").toString());
            }
            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doGet - inference progress");

        String id = httpServletRequest.getParameter("id");

        try {
            String rate = inferenceProgressRate.get(id).toString();
            out.println(rate);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
