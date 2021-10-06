package com.seculayer.mrms.rest.servlet;

import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import com.seculayer.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ProjectCreateServlet extends ServletHandlerAbstract {
    protected static final long serialVersionUID = -3148899107745938614L;
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/project_create";

    private ProjectManageDAO projManageDAO = new ProjectManageDAO();

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            this.projManageDAO.insertProjectInfo(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
