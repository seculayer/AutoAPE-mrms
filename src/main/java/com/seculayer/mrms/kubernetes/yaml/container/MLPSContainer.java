package com.seculayer.mrms.kubernetes.yaml.container;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.InferenceInfo;
import com.seculayer.mrms.info.LearnInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import com.seculayer.mrms.managers.MRMServerManager;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ContainerPort;
import io.kubernetes.client.openapi.models.V1EnvVar;
import io.kubernetes.client.openapi.models.V1VolumeMount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MLPSContainer extends KubeContainer {

    public MLPSContainer(String jobType) {
        super(jobType);

        this.name = "mlps";
        this.image = registryURL + "/automl-mlps:"
            + Constants.MLPS_IMAGE_VERSION;
    }

    @Override
    public V1Container make() {
        return this.makeContainer();
    }

    @Override
    protected List<String> makeConfigMapName() {
        List<String> configMapNameList = new ArrayList<>();
        configMapNameList.add("mlps-conf");
        return configMapNameList;
    }

    @Override
    protected List<V1VolumeMount> makeVolumeMounts() {
        // TODO Auto-generated method stub
        List<V1VolumeMount> volumeMounts = super.makeVolumeMounts();

        for (KubeConfigMap configMap : this.configMapList){
            volumeMounts.add(configMap.getVolumeMount());
        }
        return volumeMounts;
    }

    @Override
    protected Map<String, Quantity> makeLimits() {
        Map<String, Quantity> limits = new HashMap<>();

        // for gpu pod
        switch (jobType) {
            case Constants.JOB_TYPE_LEARN:
                if (((LearnInfo) this.info).getGpuUse()){
                    String gpuMemLimit = Constants.GPU_MEM_LIMIT_LEARN;
                    limits.put("nvidia.com/gpu-mem", Quantity.fromString(gpuMemLimit));
                }
                break;
            case Constants.JOB_TYPE_INFERENCE:
                if (((InferenceInfo) this.info).getGpuUse()){
                    String gpuMemLimit = Constants.GPU_MEM_LIMIT_INFERENCE;
                    limits.put("nvidia.com/gpu-mem", Quantity.fromString(gpuMemLimit));
                }
                break;
        }

        // cpu limit
        String cpuLimit = MRMServerManager.getInstance().getConfiguration().get("kube.pod.cpu.limit", "1200");
        String memoryLimit = MRMServerManager.getInstance().getConfiguration().get("kube.pod.memory.limit", "0");

        if (Constants.JOB_TYPE_LEARN.equals(this.jobType)) {
            memoryLimit = String.valueOf(Integer.parseInt(memoryLimit) * 2);
        }

        limits.put("cpu", Quantity.fromString(cpuLimit + "m"));
        limits.put("memory", Quantity.fromString(memoryLimit + "Gi"));

        return limits;
    }

    @Override
    protected List<String> makeCommands() {
        List<String> commands = new ArrayList<>();
        commands.add("/bin/bash");
        commands.add("./mlps.sh");
        commands.add(this.getProcessKey());
        if (Constants.JOB_TYPE_LEARN.equals(this.jobType)){
            commands.add(Integer.toString(workerIdx));
        } else {
            commands.add("0");
        }
        commands.add(this.jobType);
        return commands;
    }

    @Override
    protected List<V1EnvVar> makeEnv() {
        String key = this.getProcessKey();

        List<V1EnvVar> envList = super.makeEnv();
        if (Constants.JOB_TYPE_LEARN.equals(this.jobType) && "Y".equals(((LearnInfo) this.info).getAlgInfo().get("dist_yn").toString())){
            int numWorker = ((LearnInfo) this.info).getNumWorker();
            // learning - distributed support
            envList.add(new V1EnvVar()
                .name("TF_CONFIG")
                .value(KubeUtil.generateTFConfig(jobType, key, numWorker, workerIdx)));
        } else {
            envList.add(new V1EnvVar()
                .name("TF_CONFIG")
                .value(KubeUtil.generateTFConfigSingle(jobType, key, workerIdx)));
        }

        if (!this.getGpuUse()) {
            envList.add(new V1EnvVar()
                .name("CUDA_VISIBLE_DEVICES")
                .value("-1"));
        }
        return envList;
    }

    private boolean getGpuUse() {
        switch (jobType) {
            case Constants.JOB_TYPE_LEARN:
                return ((LearnInfo) this.info).getGpuUse();
            case Constants.JOB_TYPE_INFERENCE:
                return ((InferenceInfo) this.info).getGpuUse();
            default:
                return false;
        }
    }

    @Override
    protected List<V1ContainerPort> makePorts() {
        List<V1ContainerPort> ports = new ArrayList<>();
        try {
            ports.add(new V1ContainerPort()
                .containerPort(Constants.GRPC_PORT));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ports;
    }
}
