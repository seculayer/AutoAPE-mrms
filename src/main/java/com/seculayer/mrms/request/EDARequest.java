package com.seculayer.mrms.request;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.info.EDAInfo;

import java.io.IOException;
import java.util.Map;

public class EDARequest extends Request {
    ProjectManageDAO dao = new ProjectManageDAO();

    @Override
    public void doRequest(Map<String, Object> schedule) throws RequestException, IOException {
        // Info 생성
        EDAInfo edaInfo = RequestUtils.createEDAInfo(schedule);
        edaInfo.writeInfo();

        // DB status update
        schedule.replace("status", Constants.STATUS_EDA_ING);
        dao.updateEDAStatus(schedule);

        // Job 생성
        Request.makeEDAJob(edaInfo, Constants.JOB_TYPE_EDA_CHIEF, 0);
    }

    @Override
    public String makeKey(Map<String, Object> schedule) {
        return null;
    }
}
