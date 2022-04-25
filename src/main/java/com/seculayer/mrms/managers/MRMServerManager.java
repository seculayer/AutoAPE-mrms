package com.seculayer.mrms.managers;

import com.seculayer.mrms.checker.*;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.kubernetes.KubernetesManager;
import com.seculayer.mrms.rest.HTTPServerManager;
import com.seculayer.util.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class MRMServerManager {
    // base variables
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Configuration conf = new Configuration(false);

    private static final ScheduleQueue daScheduleQueue = new ScheduleQueue();
    private static final ScheduleQueue daDelScheduleQueue = new ScheduleQueue();
    private static final ScheduleQueue rcmdScheduleQueue = new ScheduleQueue();
    private static final ScheduleQueue edaScheduleQueue = new ScheduleQueue();
    private static final ScheduleQueue learnInitScheduleQueue = new ScheduleQueue();
    private static final ScheduleQueue inferenceInitScheduleQueue = new ScheduleQueue();
    private static final ScheduleQueue XAIScheduleQueue = new ScheduleQueue();

    private static final Map<String, Object> modelResourceMap = new HashMap<>();
    private static final Map<String, Object> modelsInfoMap = new HashMap<>();
    private static final Map<String, Object> inferenceProgressRate = new HashMap<>();

    // REST server
    private HTTPServerManager httpServer;

    // kube Manager
    private KubernetesManager kubeManager;

    private MRMServerManager(){}

    // Singleton pattern
    private static class Singleton {
        private static final MRMServerManager instance = new MRMServerManager();
    }

    public static MRMServerManager getInstance() { return Singleton.instance; }
    public Configuration getConfiguration() { return conf; }

    // initialize
    public void init(){
        try {
            // Configuration load
            FileInputStream fis = new FileInputStream("./conf/mrms-conf.xml");
            conf.addResource(fis);

            // HTTP Server
            httpServer = new HTTPServerManager();
            httpServer.start();

            // Database Test
            CommonDAO commonDAO = new CommonDAO();
            commonDAO.selectTestQuery();

            // Kubernetes Manager
            kubeManager = KubernetesManager.getInstance();
            kubeManager.initialize();

            this.initSchedule();
        } catch (Exception e) {
            logger.error(String.format("initialize error : %s", e));
            e.printStackTrace();
        }
    }

    // scheduler
    private long calcDelay(long period){
        Calendar c = Calendar.getInstance();
        long ns = System.currentTimeMillis();
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        if (sec + period >= 60) {
            min += (int)((sec+period) / 60);
            sec = 0;
        } else {
            sec = (int) ((sec/period + 1) * period);
        }
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, sec);
        return c.getTimeInMillis() - ns;
    }

    private void initSchedule(){
        long period = conf.getLong("ape.schedule.period", 10);

        Timer timer = new Timer();
        long delay = this.calcDelay(period);

        logger.info("Delay for JOB - {} seconds", delay / 1000);
        this.initScheduleCheckers(timer, delay, period);
    }

    private void initScheduleCheckers(Timer timer, long delay, long period){
        if (conf.getBoolean("use.learning.schedule", true)) {
//            timer.scheduleAtFixedRate(new ProjectCompleteChecker(), delay, period * 1000);
            timer.scheduleAtFixedRate(new RcmdScheduleChecker(), delay, period * 1000);
            timer.scheduleAtFixedRate(new EDAScheduleChecker(), delay, period * 1000);
            timer.scheduleAtFixedRate(new LearnInitScheduleChecker(), delay, period * 1000);
            timer.scheduleAtFixedRate(new ProjectCompleteChecker(), delay, period * 1000);
        }
        if (conf.getBoolean("use.data.analysis.schedule", true)) {
            timer.scheduleAtFixedRate(new DAScheduleChecker(), delay, period * 1000);
            timer.scheduleAtFixedRate(new DACompleteChecker(), delay, period * 1000);
            timer.scheduleAtFixedRate(new DADelChecker(), delay, period * 1000);
        }
        if (conf.getBoolean("use.inference.schedule", true)) {
            timer.scheduleAtFixedRate(new InferenceInitScheduleChecker(), delay, period * 1000);
            timer.scheduleAtFixedRate(new XAIScheduleChecker(), delay, period * 1000);
        }
    }

    public void terminate() {
        try {
            httpServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final ScheduleQueue getDAScheduleQueue() { return daScheduleQueue; }
    public final ScheduleQueue getDADelScheduleQueue() { return daDelScheduleQueue; }
    public final ScheduleQueue getRcmdScheduleQueue() { return rcmdScheduleQueue; }
    public final ScheduleQueue getEDAScheduleQueue() { return edaScheduleQueue; }
    public final ScheduleQueue getLearnInitScheduleQueue() { return learnInitScheduleQueue; }
    public final ScheduleQueue getInferenceInitScheduleQueue() { return inferenceInitScheduleQueue; }
    public final ScheduleQueue getXAIScheduleQueue() { return XAIScheduleQueue; }
    public final Map<String, Object> getModelResourceMap() { return modelResourceMap; }
    public final Map<String, Object> getModelsInfoMap() { return modelsInfoMap; }
    public final Map<String, Object> getInferenceProgressRate() { return inferenceProgressRate; }
}
