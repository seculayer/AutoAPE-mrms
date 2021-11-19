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
        List<Map<String, Object>> learHistReqList = projectDAO.selectLearnReqList(schedule);

        try {
            for (Map<String, Object> req : learHistReqList) {
                String key = this.makeKey(req);

                projectDAO.updateUsedYN(req);

                LearnInfo learnInfo = new LearnInfo(key);
                learnInfo.init(req);
                learnInfo.writeInfo();

                this.makeLearnJob(learnInfo, learnInfo.getNumWorker());

                req = this.setLearnHist(req);
                commonDAO.updateSttusCd(req);
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
        String learnHistNo = schedule.get("learn_hist_no").toString();
        return String.format("%s_%s", Constants.JOB_TYPE_LEARN, learnHistNo);
    }

    public Map<String, Object> setLearnHist(Map<String, Object> map) {
        map.replace("learn_sttus_cd", "2");
        map.put("task_idx", "0");
        map.put("message", "-");
        map.put("hist_no", map.get("learn_hist_no"));

        return map;
    }
}
