package com.seculayer.mrms.request;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;

import java.io.IOException;
import java.util.Map;

public class RcmdRequest extends Request {

    ProjectManageDAO dao = new ProjectManageDAO();

    @Override
    public void doRequest(Map<String, Object> schedule) throws RequestException, IOException {
        String key = this.makeKey(schedule);
//        RcmdInfo = rcmdInfo = new RcmdInfo();
//
//        logger.debug(RcmdInfo.toString());
//        this.makeRcmdJob(RcmdInfo);
        schedule.replace("status", Constants.STATUS_RCMD_ING);
        dao.updateStatus(schedule);
    }

    @Override
    public String makeKey(Map<String, Object> schedule) {
        return null;
    }
}
