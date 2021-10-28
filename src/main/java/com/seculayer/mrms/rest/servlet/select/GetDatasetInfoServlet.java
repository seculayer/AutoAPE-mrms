package com.seculayer.mrms.rest.servlet.select;

import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class GetDatasetInfoServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_dataset_info";

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        logger.debug("###################################################################");
        logger.debug("In doPost - get dataset info");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            logger.debug(map.toString());
            String datasetID = map.get("dataset_id").toString();
            Map<String, Object> rst = commonDAO.selectDatasetInfo(datasetID);

            String jsonStr = mapper.writeValueAsString(rst);
            out.println(jsonStr);

            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error(e.toString());
            out.println("error");
        }

        logger.debug("###################################################################");
    }
}
