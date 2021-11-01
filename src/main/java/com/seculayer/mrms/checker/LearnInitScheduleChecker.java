package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.LearnInitRequest;

import java.util.List;
import java.util.Map;

public class LearnInitScheduleChecker extends Checker {
    private ProjectManageDAO dao = new ProjectManageDAO();

    public LearnInitScheduleChecker(){
        super.req(new LearnInitRequest()
                .queue(MRMServerManager.getInstance().getLearnInitScheduleQueue()));
    }

    @Override
    public void doCheck() throws CheckerException {
        try{
            List<Map<String, Object>> schedules = dao.selectProjectSchedule(Constants.STATUS_PROJECT_LEARN_REQ);
            ScheduleQueue scheduleQueue = MRMServerManager.getInstance().getLearnInitScheduleQueue();

            for (Map<String, Object> s : schedules){
                try {
                    scheduleQueue.push(s);
                    logger.info("Learn init schedule queue size - {}", scheduleQueue.size());
                } catch (Exception e) {
                    logger.warn("{}, error-{}", s, e.getMessage());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
