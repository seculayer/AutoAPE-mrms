package com.seculayer.mrms.info;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.util.JsonUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearnInfo extends InfoAbstract {
    protected String learnHistNo = "";
    protected String projectID = "";
    protected String learnStartTime = "";
    protected String eduPer = "";
    protected String key = "";
    protected String modelTypeCd = "";
    protected String numWorker = "";
    protected String sampleTypeCd = "";
    protected Map<String, Object> daAnlsInfo = null;
    protected Map<String, Object> dpAnlsInfo = null;
    protected Map<String, Object> algAnlsInfo = null;
    protected Map<String, Object> mlParamInfo = null;

    protected CommonDAO commonDAO = new CommonDAO();
    protected ProjectManageDAO projectDAO = new ProjectManageDAO();

    public LearnInfo(String key) {
        super(key);
    }

    @Override
    public void init(Map<String, Object> map) {
        learnHistNo = map.get("learn_hist_no").toString();
        projectID = map.get("project_id").toString();
        learnStartTime = map.get("start_time").toString();
//        Map<String, Object> daMap = new HashMap<>().put("data_analysis_id", commonDAO.selectP)
//        daAnlsInfo = commonDAO.selectDataAnlsInfoWithDataAnalsID()

    }

    @Override
    public Map<String, Object> makeInfoMap() {
        return null;
    }

    @Override
    public Map<String, Object> loadInfo(String fileName) {
        Map<String, Object> resultMap = null;
        File file = new File(MRMServerManager.getInstance().getConfiguration().get("ape.features.dir"), fileName);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))){
            String jsonStr = JsonUtil.getJSONString(reader);
            resultMap = JsonUtil.strToMap(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

}
