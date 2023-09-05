package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ModelResource extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/model_resources";
    public static final Map<String, Object> modelResourceMap = MRMServerManager.getInstance().getModelResourceMap();

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - model resource");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            String learnHistNo = map.get("learn_hist_no").toString();
            Map<String, Object> cpu = (Map<String, Object>) map.get("cpu");
            String scaledCpuUsage = this.scaleCpuUsage(cpu);

            cpu.put("percent", scaledCpuUsage);
            modelResourceMap.put(learnHistNo, map);
            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    public String scaleCpuUsage(Map<String, Object> cpu) {
        String cpuUsage = "";
        try {
            cpuUsage = cpu.get("percent").toString();
            int limitCpuUsage = MRMServerManager.getInstance().getConfiguration().getInt("kube.pod.cpu.limit", 1200) / 10;

            return Integer.toString((int) Math.floor(Double.parseDouble(cpuUsage) / limitCpuUsage * 100));
        } catch (Exception e) {
            logger.error("cpuUsage : {}", cpuUsage);
            e.printStackTrace();
        }

        return "0";
    }
}
