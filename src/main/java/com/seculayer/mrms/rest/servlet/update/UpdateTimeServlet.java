package com.seculayer.mrms.rest.servlet.update;

import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Map;

public class UpdateTimeServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/time_update";

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        logger.debug("###################################################################");
        logger.debug("In doPost - update time");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            logger.debug(map.toString());

            String type = map.get("type").toString();
            String currTime = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
            map.put("curr_time", currTime);

            if (type.equals("start")) {
                commonDAO.updateStartTime(map);
            } else if (type.equals("end")) {
                commonDAO.updateEndTime(map);
            }

            out.println("1");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error(e.toString());
            out.println("error");
        }
        logger.debug("###################################################################");
    }
}
