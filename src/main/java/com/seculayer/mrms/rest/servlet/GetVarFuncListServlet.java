package com.seculayer.mrms.rest.servlet;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class GetVarFuncListServlet extends ServletHandlerAbstract {
    protected static final long serialVersionUID = -3148899107745938614L;
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_cvt_fn";
    public static final CommonDAO commonDAO = new CommonDAO();

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        List<Map<String, Object>> funcMap = commonDAO.selectVarFuncList();
        ObjectMapper mapper = new ObjectMapper();
        logger.info("###################################################################");
        String jsonStr = mapper.writeValueAsString(funcMap);
        logger.info(jsonStr);
        out.println(jsonStr);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.info("###################################################################");
    }
}
