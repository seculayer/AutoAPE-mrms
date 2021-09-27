package com.seculayer.mrms.rest.servlet;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class UpdateEpsServlet extends ServletHandlerAbstract {
    protected static final long serialVersionUID = -3148899107745938614L;
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/eps_update";
    public static final CommonDAO commonDAO = new CommonDAO();

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        Scanner s = new Scanner(httpServletRequest.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A");
        String postData = s.hasNext() ? s.next() : "";
        Map postDataJSON = mapper.readValue(postData, Map.class);

        logger.info("###################################################################");
        logger.info("In doPost - update eps");
        logger.info(postData);

        double eps = (Double) postDataJSON.get("eps");
        String histNo = postDataJSON.get("hist_no").toString();

        try {
            commonDAO.updateEps(histNo, eps);
            out.println(1);
        }catch (Exception e){
            logger.error(e.toString());
            out.println(0);
        }

        logger.info("###################################################################");

    }
}
