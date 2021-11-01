package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetUUIDServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_uuid";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        String uuid = commonDAO.selectUUID();

        httpServletResponse.getWriter().println(uuid);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }
}
