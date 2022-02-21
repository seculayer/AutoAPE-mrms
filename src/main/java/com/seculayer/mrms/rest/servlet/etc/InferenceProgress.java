package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.info.InferenceInfo;
import com.seculayer.mrms.info.LearnInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import com.seculayer.util.JsonUtil;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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

//                logger.info("progress {}", inferenceProgressRate.toString());
            }
            else { // delete
                String infr_hist_no = map.get("infr_hist_no").toString();
                try {
                    inferenceProgressRate.remove(infr_hist_no);
                } catch (Exception e){
                    logger.error("infr_hist_no [{}] is not existed..", infr_hist_no);
                }
                this.updateInferenceLog(map);
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

        try {
            String id = httpServletRequest.getParameter("id");

            String rate = inferenceProgressRate.get(id).toString();
            out.println(rate);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    public void updateInferenceLog(Map<String, Object> idMap) {
        String histNo = idMap.get("infr_hist_no").toString();
        boolean tail = false;
        Map<String, Object> map = new HashMap<>();

        try {
            InferenceInfo infrInfo = new InferenceInfo(histNo);
            infrInfo.loadInfo(histNo);

            for (int idx = 0; idx < infrInfo.getNumWorker(); idx++) {
                String log = KubeUtil.getJobLogs("inference-" + histNo + "-" + idx, "mlps", tail);
                map.put("worker_" + idx, log);
            }
            JSONObject jsonData = JsonUtil.mapToJson(map);
            idMap.put("logs", jsonData.toString());
            commonDAO.updateInferenceLog(idMap);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
