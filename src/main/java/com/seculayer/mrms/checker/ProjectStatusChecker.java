package com.seculayer.mrms.checker;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.managers.MRMServerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectStatusChecker extends Checker {
    private ProjectManageDAO dao = new ProjectManageDAO();

    @Override
    public void doCheck() throws CheckerException {
        List<Map<String, Object>> projectList = MRMServerManager.getInstance().getProjectIdList();
        List<Map<String, Object>> tmpList = new ArrayList<>();

        for (Map<String, Object> idMap : projectList) {
            List<Map<String, Object>> schedules = dao.selectLearningProjectList(idMap);
            boolean isCompl = false;

            for (Map<String, Object> schd: schedules) {
                if (schd.get("status").toString().equals(Constants.STATUS_COMPLETE)) {
                    isCompl = true;
                    break;
                }
            }

            if (isCompl) {
                // 완료 상태 업데이트
                dao.updateStatus(idMap);
                tmpList.add(idMap);
            }
        }

        // 완료된 Project 제거
        for (Map<String, Object> complIdMap : tmpList) {
            projectList.remove(complIdMap);
        }
    }
}
