package com.seculayer.mrms.rest.servlet.select;

import com.seculayer.mrms.rest.ServletHandlerAbstract;
import com.seculayer.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GetDatasetMeta extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_dataset_meta";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doGet - get dataset meta");

        String datasetID = httpServletRequest.getParameter("dataset_id");

        try {
            String datasetMeta = commonDAO.selectDatasetMeta(datasetID);
            List<Map<String, Object>> datasetMetaList = JsonUtil.strToListMap(datasetMeta);
            Map<String, Object> rst = getDatasetMeta(datasetMetaList);
            out.println(JsonUtil.mapToJson(rst));
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }

    public Map<String, Object> getDatasetMeta(List<Map<String, Object>> list) {
        Map<String, Object> rst = new HashMap<>();
        int columnsCnt = list.size();
        List<Integer> emptyIdxList = new ArrayList<>();
        Set<String> keySet = null;

        for (int idx=0; idx<list.size(); idx++) {
            Map<String, Object> e = list.get(idx);
            if (e.isEmpty()) {
                emptyIdxList.add(idx);
                continue;
            }
            keySet = e.keySet();
            for (String key: keySet) {
                if (!rst.containsKey(key)) {
                    rst.put(key, new ArrayList<>());
                }
                ((List) rst.get(key)).add(e.get(key));
            }
        }

        for (Integer idx: emptyIdxList) {
            assert keySet != null;
            for (String key: keySet) {
                List<?> tmpList = new ArrayList<>();
                for (int i=0; i<columnsCnt; i++) {
                    tmpList.add(null);
                }
                ((List) rst.get(key)).add(idx, tmpList);
            }
        }

        return rst;
    }
}
