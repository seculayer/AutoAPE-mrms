package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.XAIRequest;

import java.util.List;
import java.util.Map;

public class XAIScheduleChecker extends Checker {
    private CommonDAO dao = new CommonDAO();
    public static final Map<String, Object> xaiProgressRate = MRMServerManager.getInstance().getXaiProgressRate();

    public XAIScheduleChecker() {
        super.req(new XAIRequest()
            .queue(MRMServerManager.getInstance().getXAIScheduleQueue()));
    }
    @Override
    public void doCheck() throws CheckerException {
        try{
            List<Map<String, Object>> schedules = dao.selectXAISchedule(Constants.STATUS_XAI_REQ);
            ScheduleQueue scheduleQueue = MRMServerManager.getInstance().getXAIScheduleQueue();

            for (Map<String, Object> s : schedules){
                try {
                    scheduleQueue.push(s);
                    s = setXAIHist(s, Constants.STATUS_XAI_INIT);

                    xaiProgressRate.put(s.get("xai_hist_no").toString(), "0.0");

                    dao.updateXAISttusCd(s);
                    logger.info("XAI init schedule queue size - {}", scheduleQueue.size());
                } catch (Exception e) {
                    logger.warn("{}, error-{}", s, e.getMessage());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<String, Object> setXAIHist(Map<String, Object> map, String status) {
        map.put("sttus_cd", status);
        map.put("hist_no", map.get("xai_hist_no"));

        return map;
    }
}
