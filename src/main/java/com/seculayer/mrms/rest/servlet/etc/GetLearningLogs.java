package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.info.LearnInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import com.seculayer.util.JsonUtil;
import com.seculayer.util.StringUtil;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class GetLearningLogs extends ServletHandlerAbstract{
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_learn_logs";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        String id = httpServletRequest.getParameter("id");
        boolean tail = StringUtil.getBoolean(httpServletRequest.getParameter("tail"));

        Map<String, Object> map = new HashMap<>();

        try {
            LearnInfo learnInfo = new LearnInfo();
            learnInfo.loadInfo(id);

            for (int idx = 0; idx < learnInfo.getNumWorker(); idx++) {
                String log = KubeUtil.getJobLogs("learn-" + id + "-" + idx, "mlps", tail);
                System.out.println(log);
                map.put("worker_" + idx, log);
            }
            JSONObject jsonData = JsonUtil.mapToJson(map);
            out.println(jsonData);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
