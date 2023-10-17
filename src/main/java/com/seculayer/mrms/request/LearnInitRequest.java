package com.seculayer.mrms.request;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.LearnInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LearnInitRequest extends Request {

    @Override
    public void doRequest(Map<String, Object> schedule) throws RequestException, IOException {
        List<Map<String, Object>> learHistReqList = projectDAO.selectLearnReqList(schedule);
        int limiteCnt = 0;

        try {
            for (Map<String, Object> req : learHistReqList) {
                if (!req.get("learn_sttus_cd").equals(Constants.STATUS_LEARN_HIST_INIT)){
                    continue;
                }
                String key = this.makeKey(req);

                LearnInfo learnInfo = new LearnInfo(key);
                learnInfo.init(req);

                if (!KubeUtil.isAllocatable(learnInfo.getNumWorker())){
//                    logger.info(
//                        "CPU Limitations!, {} pod(s) is/are waiting for resource free...",
//                        learnInfo.getNumWorker()
//                    );

                    limiteCnt ++;
                    continue;
                }

                projectDAO.updateUsedYN(req);

                learnInfo.writeInfo();

                this.makeLearnJob(learnInfo, learnInfo.getNumWorker());

                req = this.setLearnHist(req);
                commonDAO.updateSttusCd(req);

                Thread.sleep(3000);
            }

            schedule.put("status", Constants.STATUS_PROJECT_LEARN_ING);

            if (limiteCnt < learHistReqList.size()) {
                projectDAO.updateStatus(schedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
            schedule.put("status", Constants.STATUS_PROJECT_ERROR);
            projectDAO.updateStatus(schedule);
        }
    }

    @Override
    public String makeKey(Map<String, Object> schedule) {
        String learnHistNo = schedule.get("learn_hist_no").toString();
        return String.format("%s_%s", Constants.JOB_TYPE_LEARN, learnHistNo);
    }

    public Map<String, Object> setLearnHist(Map<String, Object> map) {
        map.put("sttus_cd", "2");
        map.put("task_idx", "0");
        map.put("hist_no", map.get("learn_hist_no"));

        return map;
    }
}
