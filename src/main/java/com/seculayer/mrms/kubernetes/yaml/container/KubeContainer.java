package com.seculayer.mrms.kubernetes.yaml.container;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.mrms.info.*;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class KubeContainer {
    private Logger logger = LogManager.getLogger();
    protected static final String registryURL = "registry.seculayer.com:31500/ape";

    protected String name;
    protected String image;
    protected String jobType;

    public KubeContainer(String jobType){
        this.jobType = jobType;
    }

    protected CommonDAO commonDao = new CommonDAO();

    protected InfoAbstract info;
    public KubeContainer info(InfoAbstract info){
        this.info = info;
        return this;
    }

    protected int workerIdx = 0;
    public KubeContainer workerIdx(int workerIdx){
        this.workerIdx = workerIdx;
        return this;
    }

    protected List<KubeConfigMap> configMapList;
    public KubeContainer configMapList(List<KubeConfigMap> configMapList){
        this.configMapList = KubeUtil.getKubeConfMapList(configMapList, this.makeConfigMapName());
        return this;
    }

    protected V1Container makeContainer(){
        this.printContainerInfoDebug();

        return new V1Container()
                .name(this.name)
                .image(this.image)
                .imagePullPolicy("Always")
                .command(this.makeCommands())
                .volumeMounts(this.makeVolumeMounts())
                .env(this.makeEnv())
                .resources(new V1ResourceRequirements().limits(this.makeLimits()).requests(this.makeLimits()))
                .ports(this.makePorts());
    }

    public abstract V1Container make();
    protected abstract List<String> makeConfigMapName();
    protected List<V1VolumeMount> makeVolumeMounts(){
        List<V1VolumeMount> volumeMounts = new ArrayList<>();
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("features", "/eyeCloudAI/data/processing/ape/features"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("errors", "/eyeCloudAI/data/processing/ape/errors"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("rtdetect", "/eyeCloudAI/data/processing/ape/rtdetect"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("temp", "/eyeCloudAI/data/processing/ape/temp"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("verifys", "/eyeCloudAI/data/processing/ape/verifys"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("detects", "/eyeCloudAI/data/processing/ape/detects"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("results", "/eyeCloudAI/data/processing/ape/results"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("models", "/eyeCloudAI/data/processing/ape/models"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("storage", "/eyeCloudAI/data/storage"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("division", "/eyeCloudAI/data/processing/ape/division"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("tz", "/etc/localtime"));
        return volumeMounts;
    }
    protected List<V1VolumeMount> makeDaVolumeMounts(){
        List<V1VolumeMount> volumeMounts = new ArrayList<>();

        volumeMounts.add(KubeUtil.getVolumeMountFromPath("temp", "/eyeCloudAI/data/processing/ape/temp"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("tz", "/etc/localtime"));
        return volumeMounts;
    }
    protected List<V1VolumeMount> makeRcmdVolumeMounts(){
        List<V1VolumeMount> volumeMounts = new ArrayList<>();

        volumeMounts.add(KubeUtil.getVolumeMountFromPath("temp", "/eyeCloudAI/data/processing/ape/temp"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("tz", "/etc/localtime"));
        return volumeMounts;
    }
    protected List<V1VolumeMount> makeXAIVolumeMounts(){
        List<V1VolumeMount> volumeMounts = new ArrayList<>();

        volumeMounts.add(KubeUtil.getVolumeMountFromPath("temp", "/eyeCloudAI/data/processing/ape/temp"));
        volumeMounts.add(KubeUtil.getVolumeMountFromPath("tz", "/etc/localtime"));
        return volumeMounts;
    }
    protected abstract Map<String, Quantity> makeLimits();
    protected abstract List<String> makeCommands();
    protected List<V1EnvVar> makeEnv(){
        List<V1EnvVar> envList = new ArrayList<>();
        envList.add(new V1EnvVar().name("TZ").value("Asia/Seoul"));
        return envList;
    }
    protected abstract List<V1ContainerPort> makePorts();

    protected String getProcessKey(){
        switch(this.jobType){
            case Constants.JOB_TYPE_DA_CHIEF: case Constants.JOB_TYPE_DA_WORKER:
                return ((DAInfo)this.info).getDatasetId();
            case Constants.JOB_TYPE_EDA_CHIEF: case Constants.JOB_TYPE_EDA_WORKER:
                return ((EDAInfo)this.info).getKey();
            case Constants.JOB_TYPE_DPRS: case Constants.JOB_TYPE_MARS: case Constants.JOB_TYPE_HPRS:
                return ((RcmdInfo)this.info).getProjectID();
            case Constants.JOB_TYPE_LEARN:
                return ((LearnInfo)this.info).getLearnHistNo();
            case Constants.JOB_TYPE_INFERENCE:
                return ((InferenceInfo)this.info).getInfrHistNo();
            case Constants.JOB_TYPE_XAI:
                return ((XAIInfo)this.info).getXAIHistNo();
            default:
                return "0";
        }
    }

    protected void printContainerInfoDebug() {
        logger.debug("-------------------------------------------");
        logger.debug("In KubeContainer..");
        logger.debug("name : {}", this.name);
        logger.debug("image : {}", this.image);
        logger.debug("command : {}", this.makeCommands().toString());
        logger.debug("volumes : {}, cnt : {}",this.makeVolumeMounts().toString(), this.makeVolumeMounts().size());
        logger.debug("-------------------------------------------");
    }
}
