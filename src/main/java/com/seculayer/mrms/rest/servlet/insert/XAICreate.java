package com.seculayer.mrms.rest.servlet.insert;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class XAICreate extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/xai_create";

    private CommonDAO commonDAO = new CommonDAO();

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        logger.debug("###################################################################");
        try {
            String infrHistNo = httpServletRequest.getParameter("infr_hist_no");
            String targetField = httpServletRequest.getParameter("target_field");
            String dataAnlsID = httpServletRequest.getParameter("data_analysis_id");
            String learnHistNo = httpServletRequest.getParameter("learn_hist_no");
            logger.debug(
                "In doGet - create XAI, infr_hist_no: {}", infrHistNo
            );

            Map<String, Object> init_map = this.get_init_map(infrHistNo, targetField, dataAnlsID, learnHistNo);
            commonDAO.insertXaiInfo(init_map);
            commonDAO.updateXAICreateYN(init_map);

            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    private Map<String, Object> get_init_map(String infrHistNo, String targetField, String dataAnlsID, String learnHistNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("infr_hist_no", infrHistNo);
        map.put("xai_sttus_cd", "1");
        map.put("target_field", targetField);
        map.put("data_analysis_id", dataAnlsID);
        map.put("learn_hist_no", learnHistNo);

        return map;
    }
}
