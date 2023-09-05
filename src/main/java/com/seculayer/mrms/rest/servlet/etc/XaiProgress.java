package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.info.XAIInfo;
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

public class XaiProgress extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/xai_progress_rate";
    public static final Map<String, Object> xaiProgressRate = MRMServerManager.getInstance().getXaiProgressRate();

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - xai progress");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            if (map.get("mode").toString().equals("update")) {
                xaiProgressRate.put(
                    map.get("xai_hist_no").toString(),
                    map.get("progress_rate").toString()
                );

            }
            else { // delete
                String xai_hist_no = map.get("xai_hist_no").toString();
                try {
                    xaiProgressRate.remove(xai_hist_no);
                } catch (Exception e){
                    logger.error("xai_hist_no [{}] is not existed..", xai_hist_no);
                }
//                this.updateXaiLog(map);
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
        logger.debug("In doGet - xai progress");

        try {
            String id = httpServletRequest.getParameter("id");

            String rate;
            if (xaiProgressRate.containsKey(id)) {
                rate = xaiProgressRate.get(id).toString();
            } else {
                rate = "100.0";
            }
            out.print(rate);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    public void updateXaiLog(Map<String, Object> idMap) {
        String histNo = idMap.get("xai_hist_no").toString();
        boolean tail = false;
        Map<String, Object> map = new HashMap<>();

        try {
            XAIInfo xaiInfo = new XAIInfo(histNo);
            xaiInfo.loadInfo(histNo);


            String log = KubeUtil.getJobLogs("xai-" + histNo + "-0", "xai", tail);
            map.put("worker_0", log);

            JSONObject jsonData = JsonUtil.mapToJson(map);
            idMap.put("logs", jsonData.toString());
//            commonDAO.updateXaiLog(idMap);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
