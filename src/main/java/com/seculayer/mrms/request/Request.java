package com.seculayer.mrms.request;

import com.seculayer.mrms.checker.ScheduleQueue;
import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.db.ProjectManageDAO;
import com.seculayer.mrms.info.*;
import com.seculayer.mrms.kubernetes.yaml.job.*;
import com.seculayer.mrms.kubernetes.yaml.svc.MLPSService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.apache.commons.lang.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

abstract public class Request extends Thread {
    protected Logger logger = LogManager.getLogger();
    protected static final String namespace = Constants.KUBE_EYECLOUDAI_NAMESPACE;

    // schedule
    protected ScheduleQueue queue;
    protected CommonDAO commonDAO = new CommonDAO();
    protected ProjectManageDAO projectDAO = new ProjectManageDAO();

    protected boolean isTerminate = false;

    //builder pattern
    public Request queue(ScheduleQueue queue){
        this.queue = queue;
        return this;
    }

    @Override
    public void run(){

        while(!isTerminate){
            try{
                Map<String, Object> schedule = this.queue.pop();
                this.doRequest(schedule);
            } catch (Exception e) {
                logger.error("Request Error : ");
                e.printStackTrace();
            }
        }
    }

    abstract public void doRequest(Map<String, Object> schedule) throws RequestException, IOException;
    abstract public String makeKey(Map<String, Object> schedule);

    // Job
    public static void makeDAJob(DAInfo daInfo, String jobType, int workerIdx){
        try{
            createJob(makeJob(daInfo, jobType, workerIdx));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void makeEDAJob(EDAInfo edaInfo, String jobType, int workerIdx){
        try{
            createJob(makeJob(edaInfo, jobType, workerIdx));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void makeRcmdJob(RcmdInfo rcmdInfo, String jobType, int workerIdx) {
        try{
            createJob(makeJob(rcmdInfo, jobType, workerIdx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeLearnJob(LearnInfo learnInfo, int numWorker) {
        for (int i=0; i<numWorker; i++) {
            try{
                createJob(makeJob(learnInfo, Constants.JOB_TYPE_LEARN, i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                createService(makeService(learnInfo, Constants.JOB_TYPE_LEARN, i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void makeInferenceJob(InferenceInfo inferenceInfo, int numWorker) {
        for (int i=0; i<numWorker; i++) {
            try{
                createJob(makeJob(inferenceInfo, Constants.JOB_TYPE_INFERENCE, i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void makeXAIJob(XAIInfo xaiInfo) {
        try{
            createJob(makeJob(xaiInfo, Constants.JOB_TYPE_XAI, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static V1Job makeJob(InfoAbstract info, String jobType, int workerIdx){
        switch (jobType){
            case Constants.JOB_TYPE_DA_CHIEF: case Constants.JOB_TYPE_DA_WORKER:
                return new DAJob()
                        .info(info)
                        .jobType(jobType)
                        .workerIdx(workerIdx)
                        .make();
            case Constants.JOB_TYPE_EDA_CHIEF: case Constants.JOB_TYPE_EDA_WORKER:
                return new EDAJob()
                        .info(info)
                        .jobType(jobType)
                        .workerIdx(workerIdx)
                        .make();
            case Constants.JOB_TYPE_DPRS: case Constants.JOB_TYPE_MARS: case Constants.JOB_TYPE_HPRS:
                return new RcmdJob()
                        .info(info)
                        .jobType(jobType)
                        .workerIdx(workerIdx)
                        .make();
            case Constants.JOB_TYPE_LEARN:
                return  new LearnJob()
                        .info(info)
                        .jobType(jobType)
                        .workerIdx(workerIdx)
                        .make();
            case Constants.JOB_TYPE_INFERENCE:
                return new InferenceJob()
                        .info(info)
                        .jobType(jobType)
                        .workerIdx(workerIdx)
                        .make();
            case Constants.JOB_TYPE_XAI:
                return new XAIJob()
                        .info(info)
                        .jobType(jobType)
                        .workerIdx(workerIdx)
                        .make();

            default:
                throw new NotImplementedException();
        }
    }

    protected V1Service makeService(InfoAbstract info, String jobType, int workerIdx){
        switch (jobType){
            case Constants.JOB_TYPE_LEARN:
                return new MLPSService()
                    .info(info)
                    .workerIdx(workerIdx)
                    .jobType(jobType)
                    .make();
            default:
                throw new NotImplementedException();
        }
    }

    // Kubernetes API call
    protected static V1Job createJob(V1Job job) throws ApiException {
        BatchV1Api api = new BatchV1Api();
        return api.createNamespacedJob(namespace, job, null, null, null);
    }

    protected V1Service createService(V1Service service) throws ApiException {
        CoreV1Api api = new CoreV1Api();
        return api.createNamespacedService(namespace, service, null, null, null);
    }

    public static V1Status deleteService(String serviceName) {
        CoreV1Api api = new CoreV1Api();

        try {
            return api.deleteNamespacedService(serviceName, namespace, null, null, null, true, null, new V1DeleteOptions());
        } catch (Exception e) {
            return null;
        }
    }

    public static V1Status deleteJob(String jobName) {
        BatchV1Api api = new BatchV1Api();

        try {
            return api.deleteNamespacedJob(jobName, namespace, null, null, null, true, null, new V1DeleteOptions());
        } catch (Exception e) {
            return null;
        }
    }

    public static V1Status deletePod(String podName) {
        CoreV1Api api = new CoreV1Api();

        try {
            return api.deleteNamespacedPod(podName, namespace, null, null, null, true, null, new V1DeleteOptions());
        } catch (Exception e) {
            return null;
        }
    }

    public static V1PodList getPodList() {
        CoreV1Api api = new CoreV1Api();
        try {
            return api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, false);
        } catch (Exception e) {
            e.printStackTrace();
            return new V1PodList();
        }
    }
}
