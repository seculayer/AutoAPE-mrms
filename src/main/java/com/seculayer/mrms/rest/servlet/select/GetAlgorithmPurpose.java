/* -*-coding:utf-8-*-
    # Author:Manki Baek
    # e-mail:manki.baek@seculayer.com
# Powered by Seculayer © 2023Service Model Team,R&D Center.
*/

package com.seculayer.mrms.rest.servlet.select;

import com.seculayer.mrms.rest.ServletHandlerAbstract;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class GetAlgorithmPurpose extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_algorithm_purpose";

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.setContentType("text/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        logger.debug("###################################################################");
        logger.debug("In doGet - get algorithm purpose");

        try {
            String projectPurposeCd = httpServletRequest.getParameter("project_purpose_cd");
            List<Map<String, Object>> algoList = commonDAO.selectAlgoPurpose(projectPurposeCd);
            String jsonStr = mapper.writeValueAsString(algoList);

            out.print(jsonStr);

        } catch (Exception e) {
            e.printStackTrace();
            out.println("error");
        }

    }
}
