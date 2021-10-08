package com.seculayer.mrms.rest.servlet;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.rest.ServletHandlerAbstract;
import com.seculayer.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetCVTFunction extends ServletHandlerAbstract {
    protected static final long serialVersionUID = -3148899107745938614L;
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/get_cvt_fn";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONArray result = new JSONArray();
        for (Map<String, Object> map: new CommonDAO().selectCVTFunction()){
            try {
                result.put(JsonUtil.mapToJson(map));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            resp.getWriter().println(result.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
