package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DAWorkerReqServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/request_da_worker";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        logger.info("###################################################################");
        try {
            String id = httpServletRequest.getParameter("id");
            String num_workers = httpServletRequest.getParameter("num_worker");
            logger.info(
                    "In doGet - create Data Analyze Workers, id: {}, num_workers: {}",
                    id, num_workers
            );
            // DAWorker Start
            // ---------------------------------------------



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
