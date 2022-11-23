package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.LearnInitRequest;

import java.util.ArrayList;
import java.util.HashMap;
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
            List<Map<String, Object>> initSchedules = dao.selectProjectSchedule(Constants.STATUS_PROJECT_LEARN_REQ);
            List<Map<String, Object>> learningSchedules = dao.selectProjectSchedule(Constants.STATUS_PROJECT_LEARN_ING);

            ScheduleQueue scheduleQueue = MRMServerManager.getInstance().getLearnInitScheduleQueue();
            List<Map<String, Object>> schedules = new ArrayList<>();
            schedules.addAll(initSchedules);
            schedules.addAll(learningSchedules);

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
