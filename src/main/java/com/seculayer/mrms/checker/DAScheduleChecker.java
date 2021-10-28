package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.request.DARequest;

import java.util.List;
import java.util.Map;

public class DAScheduleChecker extends Checker {
    private CommonDAO dao = new CommonDAO();
    private static final ScheduleQueue daCheifScheduleQueue = MRMServerManager.getInstance().getDAScheduleQueue();

    public DAScheduleChecker(){
        super.req(new DARequest()
                .queue(daCheifScheduleQueue));
    }

    @Override
    public void doCheck() throws CheckerException {
        List<Map<String, Object>> daReqProjectList = dao.selectDASchedule(Constants.STATUS_DA_REQ);
        for (Map<String, Object> schd : daReqProjectList) {
            try {
                daCheifScheduleQueue.push(schd);
                logger.info("Data Analyze schedule queue size - {}", daCheifScheduleQueue.size());
            } catch (Exception e) {
                logger.warn("{}, error-{}", schd, e.getMessage());
            }
        }
    }
}
