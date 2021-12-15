package com.seculayer.mrms.rest;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.util.conf.Configuration;
import com.seculayer.mrms.managers.MRMServerManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.http.HttpServlet;

public class ServletHandlerAbstract extends HttpServlet {
    protected static final long serialVersionUID = -3148899107745938614L;
    protected static Logger logger = LogManager.getLogger();
    protected static Configuration conf = MRMServerManager.getInstance().getConfiguration();
    public static final String ContextPath = "/mrms";
    public static final CommonDAO commonDAO = new CommonDAO();
    public static final ProjectManageDAO projectDAO = new ProjectManageDAO();
}
