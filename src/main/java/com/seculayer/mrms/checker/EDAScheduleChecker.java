package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.EDARequest;

import java.util.List;
import java.util.Map;

public class EDAScheduleChecker extends Checker {
    private ProjectManageDAO dao = new ProjectManageDAO();
    private static final ScheduleQueue edaScheduleQueue = MRMServerManager.getInstance().getEDAScheduleQueue();

    public EDAScheduleChecker(){
        super.req(new EDARequest()
            .queue(edaScheduleQueue)
        );
    }

    @Override
    public void doCheck() throws CheckerException {
        List<Map<String, Object>> daReqProjectList = dao.selectEDASchedule(Constants.STATUS_EDA_REQ);
        for (Map<String, Object> schd : daReqProjectList) {
            try {
                edaScheduleQueue.push(schd);
                logger.info("Exploratory Data Analyze schedule queue size - {}", edaScheduleQueue.size());
            } catch (Exception e) {
                logger.warn("{}, error-{}", schd, e.getMessage());
            }
        }
    }
}
