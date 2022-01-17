package com.seculayer.mrms.request;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.InferenceInfo;

import java.io.IOException;
import java.util.Map;

public class InferenceInitRequest extends Request {

    @Override
    public void doRequest(Map<String, Object> schedule) throws RequestException, IOException {
        try {
            String key = this.makeKey(schedule);

            InferenceInfo inferenceInfo = new InferenceInfo(key);
            inferenceInfo.init(schedule);
            inferenceInfo.writeInfo();

            this.makeInferenceJob(inferenceInfo, inferenceInfo.getNumWorker());

            schedule = this.setInferenceHist(schedule, Constants.STATUS_INFERENCE_ING);
        } catch (Exception e) {
            e.printStackTrace();
            schedule = this.setInferenceHist(schedule, Constants.STATUS_INFERENCE_ING);
        } finally {
            commonDAO.updateInferenceSttusCd(schedule);
        }
    }

    @Override
    public String makeKey(Map<String, Object> schedule) {
        String infrHistNo = schedule.get("infr_hist_no").toString();
        return String.format("%s_%s", Constants.JOB_TYPE_INFERENCE, infrHistNo);
    }

    public Map<String, Object> setInferenceHist(Map<String, Object> map, String status) {
        map.put("sttus_cd", status);
        map.put("task_idx", "0");
        map.put("hist_no", map.get("infr_hist_no"));

        return map;
    }
}
