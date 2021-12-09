package com.seculayer.mrms.rest.servlet.insert;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import com.seculayer.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InsertDataAnalsInfoServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/insert_data_anls_info";

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();
        logger.debug("###################################################################");
        logger.debug("In doPost - insert data analysis info");

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("dataset_id", httpServletRequest.getParameter("dataset_id"));

            Map<String, Object> rstMap = this.readDaResult(map);
            commonDAO.insertDataAnlsInfo(rstMap);
            logger.debug(rstMap.toString());

            out.println("1");

        } catch (Exception e){
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - insert data analysis info");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            Map<String, Object> rstMap = this.readDaResult(map);
            commonDAO.insertDataAnlsInfo(rstMap);
            logger.debug(rstMap.toString());

            out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    private Map<String, Object> readDaResult(Map<String, Object> map) {
        Map<String, Object> rstMap = new HashMap<>();
        String fileName = String.format("DA_META_%s.info", map.get("dataset_id").toString());
        File file = new File(MRMServerManager.getInstance().getConfiguration().get("ape.da.dir") + "/" + map.get("dataset_id"), fileName);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))){
            String jsonStr = JsonUtil.getJSONString(reader);
            Map<String, Object> daResult = JsonUtil.strToMap(jsonStr);

            rstMap.put("dataset_id", map.get("dataset_id").toString());
            rstMap.put("metadata_json", jsonStr);
            rstMap.put("analysis_file_nm", fileName);
            rstMap.put("dist_file_cnt", ((List<?>) daResult.get("file_list")).size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rstMap;
    }
}
