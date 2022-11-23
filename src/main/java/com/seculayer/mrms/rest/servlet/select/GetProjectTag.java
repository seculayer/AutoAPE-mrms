package com.seculayer.mrms.rest.servlet.select;

import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetProjectTag extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_project_tag";

    @java.lang.Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json; charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();

        logger.debug("###################################################################");
        PrintWriter out = resp.getWriter();

        try {
            String projectID = req.getParameter("project_id");
            String rst = projectDAO.selectProjectTag(projectID);
            logger.debug(
                "In doGet - get project tag, project_id: {}",
                projectID
            );

            out.print(rst);
        } catch (Exception e) {
            logger.error(e.toString());
            out.print("error");
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");
    }
}
