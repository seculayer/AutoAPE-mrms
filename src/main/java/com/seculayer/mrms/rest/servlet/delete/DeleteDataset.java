package com.seculayer.mrms.rest.servlet.delete;

import com.seculayer.mrms.checker.ScheduleQueue;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class DeleteDataset extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/delete_dataset";

    private CommonDAO commonDAO = new CommonDAO();
    private static final ScheduleQueue daDelScheduleQueue = MRMServerManager.getInstance().getDADelScheduleQueue();

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - delete dataset");

        try{
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            daDelScheduleQueue.push(map);

            logger.debug(map.toString());

            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
