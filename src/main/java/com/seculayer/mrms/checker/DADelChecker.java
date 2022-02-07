package com.seculayer.mrms.checker;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.managers.MRMServerManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Map;

public class DADelChecker extends Checker {
    private CommonDAO dao = new CommonDAO();
    private static final ScheduleQueue daDADelScheduleQueue = MRMServerManager.getInstance().getDADelScheduleQueue();

    @Override
    public void doCheck() throws CheckerException {
        while (daDADelScheduleQueue.size() > 0) {
            logger.info("DA meta info delete schedule queue size - {}", daDADelScheduleQueue.size());
            Map<String, Object> schd = daDADelScheduleQueue.pop();
            String datasetID = schd.get("dataset_id").toString();

            this.removeDaMetaInfo(datasetID);
            dao.deleteDataset(schd);
            dao.deleteDataAnalysis(schd);
        }
    }

    private void removeDaMetaInfo(String datasetID) {
        String folderPath = MRMServerManager.getInstance().getConfiguration().get("ape.da.dir") + "/" + datasetID;
        File f = new File(folderPath);
        try {
            FileUtils.forceDelete(f);
            logger.info("DA Meta Folder[{}] is deleted..", folderPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
