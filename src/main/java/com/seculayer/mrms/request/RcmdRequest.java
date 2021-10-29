package com.seculayer.mrms.request;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.info.RcmdInfo;

import java.io.IOException;
import java.util.Map;

public class RcmdRequest extends Request {

    ProjectManageDAO dao = new ProjectManageDAO();

    @Override
    public void doRequest(Map<String, Object> schedule) throws RequestException, IOException {

        RcmdInfo rcmdInfo = RequestUtils.createRcmdInfo(schedule);
        rcmdInfo.writeInfo();

        logger.debug(rcmdInfo.toString());

        this.makeRcmdJob(rcmdInfo, Constants.JOB_TYPE_DPRS, 0);
        this.makeRcmdJob(rcmdInfo, Constants.JOB_TYPE_MARS, 0);
        this.makeRcmdJob(rcmdInfo, Constants.JOB_TYPE_HPRS, 0);

        schedule.replace("status", Constants.STATUS_PROJECT_RCMD_ING);
        dao.updateStatus(schedule);
    }

    @Override
    public String makeKey(Map<String, Object> schedule) {
        return null;
    }
}
