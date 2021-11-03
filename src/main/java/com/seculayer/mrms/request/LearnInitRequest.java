package com.seculayer.mrms.request;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.LearnInfo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class LearnInitRequest extends Request {

    @Override
    public void doRequest(Map<String, Object> schedule) throws RequestException, IOException {
        List<Map<String, Object>> mlParamList = projectDAO.selectMLParamInfoList(schedule);

        try {
            for (Map<String, Object> mlParam : mlParamList) {
                Map<String, Object> map = this.makeLearnHistMap(mlParam);
                String key = this.makeKey(mlParam);

                projectDAO.insertLearnHist(map);

                LearnInfo learnInfo = new LearnInfo(key);
                learnInfo.init(mlParam);
                learnInfo.writeInfo();

                this.makeLearnJob(learnInfo, learnInfo.getNumWorker());
            }

            schedule.put("status", Constants.STATUS_PROJECT_LEARN_ING);
        } catch (Exception e) {
            e.printStackTrace();
            schedule.put("status", Constants.STATUS_PROJECT_ERROR);
        }
        projectDAO.updateStatus(schedule);
    }

    @Override
    public String makeKey(Map<String, Object> schedule) {
        String projectID = schedule.get("project_id").toString();
        String learnHistNo = schedule.get("learn_hist_no").toString();
        return String.format("%s_%s",projectID, learnHistNo);
    }

    private Map<String, Object> makeLearnHistMap(Map<String, Object> mlParam) {
        mlParam.put("learn_hist_no", commonDAO.selectUUID());
        mlParam.put("learn_sttus_cd", "1");
        mlParam.put("start_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        return mlParam;
    }
}
