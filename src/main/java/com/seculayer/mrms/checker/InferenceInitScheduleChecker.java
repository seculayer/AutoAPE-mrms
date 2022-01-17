package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.InferenceInitRequest;

import java.util.List;
import java.util.Map;

public class InferenceInitScheduleChecker extends Checker {
    private CommonDAO dao = new CommonDAO();

    public InferenceInitScheduleChecker(){
        super.req(new InferenceInitRequest()
            .queue(MRMServerManager.getInstance().getInferenceInitScheduleQueue()));
    }

    @Override
    public void doCheck() throws CheckerException {
        try{
            List<Map<String, Object>> schedules = dao.selectInferenceSchedule(Constants.STATUS_INFERENCE_REQ);
            ScheduleQueue scheduleQueue = MRMServerManager.getInstance().getInferenceInitScheduleQueue();

            for (Map<String, Object> s : schedules){
                try {
                    scheduleQueue.push(s);
                    s.replace("infr_sttus_cd", Constants.STATUS_INFERENCE_INIT);
                    s.put("task_idx", "0");
                    dao.updateInferenceSttusCd(s);
                    logger.info("Inference init schedule queue size - {}", scheduleQueue.size());
                } catch (Exception e) {
                    logger.warn("{}, error-{}", s, e.getMessage());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
