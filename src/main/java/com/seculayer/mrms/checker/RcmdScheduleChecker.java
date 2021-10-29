package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.RcmdRequest;

import java.util.List;
import java.util.Map;

public class RcmdScheduleChecker extends Checker {
    private ProjectManageDAO dao = new ProjectManageDAO();
    private static final ScheduleQueue rcmdScheduleQueue = MRMServerManager.getInstance().getRcmdScheduleQueue();

    public RcmdScheduleChecker() {
        super.req(new RcmdRequest()
                .queue(rcmdScheduleQueue));
    }
    @Override
    public void doCheck() throws CheckerException {
        List<Map<String, Object>> rcmdReqProjectList = dao.selectProjectSchedule(Constants.STATUS_PROJECT_RCMD_REQ);
        for (Map<String, Object> schd : rcmdReqProjectList) {
            try {
                rcmdScheduleQueue.push(schd);
                logger.info("Recommender schedule queue size - {}", rcmdScheduleQueue.size());
            } catch (Exception e) {
                logger.warn("{}, error-{}", schd, e.getMessage());
            }
        }

    }
}
