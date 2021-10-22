package com.seculayer.mrms.rest.servlet.insert;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.rest.ServletFactory;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class InsertDatasetServlet extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/insert_dataset";

    private CommonDAO commonDAO = new CommonDAO();

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        PrintWriter out = httpServletResponse.getWriter();

        logger.debug("###################################################################");
        logger.debug("In doPost - insert dataset");

        try {
            Map<String, Object> map = ServletFactory.getBodyFromJSON(httpServletRequest);
            this.commonDAO.insertDataset(map);
            logger.debug(map.toString());

            out.println("1");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

        logger.debug("###################################################################");
    }
}
