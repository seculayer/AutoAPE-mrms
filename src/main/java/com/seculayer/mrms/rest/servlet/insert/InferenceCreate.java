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

public class InferenceCreate extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/inference_create";

    private CommonDAO commonDAO = new CommonDAO();

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doGet - inference create");

        try {
            String learnHistNo = httpServletRequest.getParameter("learn_hist_no");
            String dataAnlsId = httpServletRequest.getParameter("data_analysis_id");
            String targetField = httpServletRequest.getParameter("target_field");

            commonDAO.insertInferenceInfo(this.get_init_map(learnHistNo, dataAnlsId, targetField));

            logger.debug("learnHistNo : {}" + learnHistNo);
            logger.debug("dataAnlsId : {}" + dataAnlsId);
            logger.debug("targetField : {}" + targetField);

            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    private Map<String, Object> get_init_map(String learnHistNo, String dataAnlsId, String targetField) {
        Map<String, Object> map = new HashMap<>();
        map.put("learn_hist_no", learnHistNo);
        map.put("data_analysis_id", dataAnlsId);
        map.put("target_field", targetField);
        map.put("infr_sttus_cd", "1");

        return map;
    }
}
