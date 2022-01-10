package com.seculayer.mrms.rest.servlet.update;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class UpdateEvalResult extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/eval_result_update";

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        logger.debug("###################################################################");
        logger.debug("In doPost - update eval result");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            String learnHistNo = map.get("hist_no").toString();
            String taskIdx = map.get("task_idx").toString();
            String result = mapper.writeValueAsString(map.get("result"));

            boolean trigger = false;
            Map<String, Object> evalMap = null;
            while(!trigger) {
                evalMap = mapper.readValue(commonDAO.selectEvalResult(learnHistNo), Map.class);
                if (taskIdx.equals("0")){
                    trigger = true;
                } else {
                    String tmpTaskIdx = Integer.toString(Integer.parseInt(taskIdx) - 1);
                    if (evalMap.containsKey(tmpTaskIdx)) { trigger = true; }
                    else { Thread.sleep(2000); }
                }
            }
            evalMap.put(taskIdx, result);
            String resultStr = mapper.writeValueAsString(evalMap);
            map.put("result", resultStr);

            commonDAO.updateEvalResult(map);

//            try {
//                Map<String, Object> info = (Map<String, Object>) MRMServerManager.getInstance().getModelsInfoMap().get(learnHistNo);
//                info.put("eval_result_json", resultStr);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            out.println("1");
        } catch(Exception e) {
            e.printStackTrace();
            out.println("error");
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
