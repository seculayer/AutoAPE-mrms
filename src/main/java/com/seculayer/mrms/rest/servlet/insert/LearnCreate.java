package com.seculayer.mrms.rest.servlet.insert;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearnCreate extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/insert_learn_hist";

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - insert learn hist");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            logger.info(map.toString());
            projectDAO.insertLearnHist(map);
            this.cachingModelsInfo(map);

            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    public void cachingModelsInfo(Map<String, Object> req) {
        String learnHistNo = req.get("learn_hist_no").toString();
        String[] learnHistNoList = learnHistNo.split(",");
        String projectID = req.get("project_id").toString();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = new HashMap<>();
        map.put("project_id", projectID);
        map.put("learn_hist_no_list", learnHistNoList);

        List<Map<String, Object>> modelInfoList = commonDAO.selectModelsInfo(map);
        Map<String, Object> model = modelInfoList.get(0);

        try {
            List<Object> dataAnalysisJson = mapper.readValue(model.get("data_analysis_json").toString(), List.class);
            for (Object fieldJson : dataAnalysisJson) {
                ((Map<String, Object>) fieldJson).remove("statistic");
            }
            model.put("data_analysis_json", dataAnalysisJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MRMServerManager.getInstance().getModelsInfoMap().put(learnHistNo, model);
    }
}
