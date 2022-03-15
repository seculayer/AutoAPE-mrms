package com.seculayer.mrms.request;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.XAIInfo;

import java.io.IOException;
import java.util.Map;

public class XAIRequest extends Request {

    @Override
    public void doRequest(Map<String, Object> schedule) throws RequestException, IOException {
        try {
            String key = this.makeKey(schedule);

            XAIInfo xaiInfo = new XAIInfo(key);
            xaiInfo.init(schedule);
            xaiInfo.writeInfo();

            this.makeXAIJob(xaiInfo);

            schedule = this.setXAIHist(schedule, Constants.STATUS_XAI_ING);
        } catch (Exception e) {
            e.printStackTrace();
            schedule = this.setXAIHist(schedule, Constants.STATUS_XAI_ERROR);
        } finally {
            commonDAO.updateXAISttusCd(schedule);
        }
    }

    @Override
    public String makeKey(Map<String, Object> schedule) {
        String xaiHistNo = schedule.get("xai_hist_no").toString();
        return String.format("%s_%s", Constants.JOB_TYPE_XAI, xaiHistNo);
    }

    public Map<String, Object> setXAIHist(Map<String, Object> map, String status) {
        map.put("sttus_cd", status);
        map.put("hist_no", map.get("xai_hist_no"));

        return map;
    }
}
