package com.seculayer.mrms.request;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.info.DACheifInfo;

import java.io.IOException;
import java.util.Map;

public class DACheifRequest extends Request{
    CommonDAO dao = new CommonDAO();

    @Override
    public void doRequest(Map<String, Object> schedule) throws RequestException, IOException {
        // Info 생성
        DACheifInfo daCheifInfo = RequestUtils.createDACheifInfo(schedule);
        daCheifInfo.writeInfo();

        // Job 생성
        this.makeDAJob(daCheifInfo);

        // DB status update
        schedule.replace("status_cd", Constants.STATUS_DA_ING);
        dao.updateDAStatus(schedule);

    }

    @Override
    public String makeKey(Map<String, Object> schedule) {
        return null;
    }
}
