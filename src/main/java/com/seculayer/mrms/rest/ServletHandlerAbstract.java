package com.seculayer.mrms.rest;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.util.conf.Configuration;
import com.seculayer.mrms.managers.MRMServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import java.lang.invoke.MethodHandles;

public class ServletHandlerAbstract extends HttpServlet {
    protected static final long serialVersionUID = -3148899107745938614L;
    protected static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static Configuration conf = MRMServerManager.getInstance().getConfiguration();
    public static final String ContextPath = "/mrms";
    public static final CommonDAO commonDAO = new CommonDAO();
}
