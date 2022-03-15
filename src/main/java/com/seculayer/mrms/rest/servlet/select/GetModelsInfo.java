package com.seculayer.mrms.rest.servlet.select;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetModelsInfo extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_models_info";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        logger.debug("###################################################################");
        logger.debug("In doPost - get models info");

        try {
            List<Map<String, Object>> returnList = null;

            String projectId = httpServletRequest.getParameter("project_id");
            try {
                String learnHistNoList = httpServletRequest.getParameter("learn_hist_no");
                String[] learnHistNoSplit = learnHistNoList.split(",");

                returnList = this.getModelInfoCache(learnHistNoSplit);
            } catch (Exception e) { }

            if (returnList == null || returnList.size() == 0) {
                returnList = this.parseModelInfo(projectId);
            }

            logger.debug(returnList.toString());
            String jsonStr = mapper.writeValueAsString(returnList);

            out.println(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }


    private List<Map<String, Object>> getModelInfoCache(String[] histNoList) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> info = null;

        for (String histNo: histNoList) {
            try {
                info = (Map<String, Object>) MRMServerManager.getInstance().getModelsInfoMap().getOrDefault(histNo, null);
                Map<String, Object> resourceMap = (Map<String, Object>) MRMServerManager.getInstance().getModelResourceMap().getOrDefault(histNo, new HashMap<>());
                if (info != null) {
                    info.put("resource_usage", mapper.writeValueAsString(resourceMap));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (info != null) { returnList.add(info); }
        }

        return returnList;
    }

    private List<Map<String, Object>> parseModelInfo(String projectID) {
        Map<String, Object> map = new HashMap<>();
        map.put("project_id", projectID);
        map.put("learn_hist_no_list", null);

        List<Map<String, Object>> rst = commonDAO.selectModelsInfo(map);

        for (Map<String, Object> model: rst) {
            ObjectMapper mapper = new ObjectMapper();
            String learnHistNo = model.get("learn_hist_no").toString();
            try {
                List<Object> dataAnalysisJson = mapper.readValue(model.get("data_analysis_json").toString(), List.class);
                for (Object fieldJson : dataAnalysisJson) {
                    ((Map<String, Object>) fieldJson).remove("statistic");
                }
                model.put("data_analysis_json", dataAnalysisJson);

                Map<String, Object> resourceMap = (Map<String, Object>) MRMServerManager.getInstance().getModelResourceMap().getOrDefault(learnHistNo, new HashMap<>());
                model.put("resource_usage", mapper.writeValueAsString(resourceMap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rst;
    }
}
