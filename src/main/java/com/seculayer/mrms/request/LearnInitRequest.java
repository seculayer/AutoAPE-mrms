package com.seculayer.mrms.request;

import com.seculayer.mrms.info.LearnInfo;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.util.JsonUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class LearnInitRequest extends Request {

    @Override
    public void doRequest(Map<String, Object> schedule) throws RequestException, IOException {
        String key = this.makeKey(schedule);

        String projectID = schedule.get("project_id").toString();
        List<Map<String, Object>> mlParamList = projectDAO.selectMLParamInfo(schedule);

        for (Map<String, Object> mlParam : mlParamList) {
            Map<String, Object> map = this.makeLearnHistMap(mlParam);
            projectDAO.insertLearnHist(map);

            LearnInfo learnInfo = new LearnInfo(key);
        }



//        learnInfo.loadInfo(key);
//
//        logger.debug(learnInfo.toString());
//        this.makeLearningJob(learnInfo);
    }

    @Override
    public String makeKey(Map<String, Object> schedule) {
        String learnHistNo = schedule.get("learn_hist_no").toString();
        return String.format("%s_%s_%s",learnHistNo, learnHistNo, learnHistNo);
    }

    private Map<String, Object> makeLearnHistMap(Map<String, Object> mlParam) {
        mlParam.put("learn_hist_no", commonDAO.selectUUID());
        mlParam.put("learn_sttus_cd", "1");
        mlParam.put("start_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        return mlParam;
    }
}
