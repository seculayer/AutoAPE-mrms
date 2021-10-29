package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;

import java.util.List;
import java.util.Map;

public class ProjectCompleteChecker extends Checker {
    private ProjectManageDAO dao = new ProjectManageDAO();

    @Override
    public void doCheck() throws CheckerException {
        List<Map<String, Object>> recommendingProjectList = dao.selectProjectSchedule(Constants.STATUS_PROJECT_LEARN_ING);

        for (Map<String, Object> idMap : recommendingProjectList) {
            List<Map<String, Object>> schedules = dao.selectLearningModelList(idMap);
            int cntModel = schedules.size();

            int tmpCnt = 0;
            for (Map<String, Object> schd: schedules) {
                if (schd.get("status").toString().equals(Constants.STATUS_PROJECT_COMPLETE)) {
                    tmpCnt++;
                }
            }

            if (cntModel == tmpCnt) {
                // 완료 상태 업데이트
                dao.updateStatus(idMap);
            }
        }
    }
}
