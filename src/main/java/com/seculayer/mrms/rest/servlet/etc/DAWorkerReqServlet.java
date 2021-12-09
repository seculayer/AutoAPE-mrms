package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.DAInfo;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.Request;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
        logger.debug("###################################################################");
        try {
            String id = httpServletRequest.getParameter("id");
            int numWorkers = Integer.parseInt(httpServletRequest.getParameter("num_worker"));
            logger.debug(
                    "In doGet - create Data Analyze Workers, dataset_id: {}, num_workers: {}",
                    id, numWorkers
            );
            // DAWorker Start
            // ---------------------------------------------
            DAInfo workerDaInfo = new DAInfo(id);
            for (int i=0; i<numWorkers; i++) {
                this.doneFileMove(id, i);
                Request.makeDAJob(workerDaInfo, Constants.JOB_TYPE_DA_WORKER, i);
            }

            // ---------------------------------------------
            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    private void doneFileMove(String datasetID, int workerIdx) {
        String daDirPath = MRMServerManager.getInstance().getConfiguration().get("ape.da.dir");
        File srcPath = new File(daDirPath + "/" + datasetID + "/" + datasetID + "_" + workerIdx + ".done");
        File dstnPath = new File(daDirPath + "/" + datasetID + "/" + workerIdx + "/" + datasetID + "_" + workerIdx + ".done");

        try {
            FileUtils.moveFile(srcPath, dstnPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
