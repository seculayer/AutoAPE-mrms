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

public class SendLearnResultServlet extends ServletHandlerAbstract {
    protected static final long serialVersionUID = -3148899107745938614L;
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/results/learn";
    public static final CommonDAO commonDAO = new CommonDAO();

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        Scanner s = new Scanner(httpServletRequest.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A");
        String postData = s.hasNext() ? s.next() : "";
        Map postDataJSON = mapper.readValue(postData, Map.class);

        logger.info("###################################################################");
        logger.info("In doPost - send learn results");
        logger.info(postData);

        logger.info("###################################################################");
    }
}
