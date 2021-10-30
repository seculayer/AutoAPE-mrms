package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.DAInfo;
import com.seculayer.mrms.request.Request;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAWorkerReqServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/request_da_worker";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        logger.info("###################################################################");
        try {
            String id = httpServletRequest.getParameter("id");
            String numWorkers = httpServletRequest.getParameter("num_worker");
            logger.info(
                    "In doGet - create Data Analyze Workers, dataset_id: {}, num_workers: {}",
                    id, numWorkers
            );
            // DAWorker Start
            // ---------------------------------------------
            Map<String, Object> scd = new HashMap<>();
            // Todo : worker들 info list 생성
            List<DAInfo> daWorkerInfoList = new ArrayList<>();

            for (int i=0; i<daWorkerInfoList.size(); i++) {
                Request.makeDAJob(daWorkerInfoList.get(i), Constants.JOB_TYPE_DA_WORKER, i);
            }

            // ---------------------------------------------
            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.info("###################################################################");
    }
}