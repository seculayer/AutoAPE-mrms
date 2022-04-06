package com.seculayer.mrms.kubernetes.yaml.container;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.LearnInfo;
import com.seculayer.mrms.info.XAIInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ContainerPort;
import io.kubernetes.client.openapi.models.V1EnvVar;
import io.kubernetes.client.openapi.models.V1VolumeMount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XAIContainer extends KubeContainer {

    public XAIContainer(String jobType) {
        super(jobType);

        this.name = "xai";
        this.image = registryURL + "/automl-xai:"
            + Constants.XAI_IMAGE_VERSION;
    }

    @Override
    public V1Container make() {
        return this.makeContainer();
    }

    @Override
    protected List<String> makeConfigMapName() {
        List<String> configMapNameList = new ArrayList<>();
        configMapNameList.add("xai-conf");
        return configMapNameList;
    }

    @Override
    protected List<V1VolumeMount> makeVolumeMounts() {
        // TODO Auto-generated method stub
        List<V1VolumeMount> volumeMounts = super.makeXAIVolumeMounts();

        for (KubeConfigMap configMap : this.configMapList){
            volumeMounts.add(configMap.getVolumeMount());
        }
        return volumeMounts;
    }

    @Override
    protected Map<String, Quantity> makeLimits() {
        Map<String, Quantity> limits = new HashMap<>();

        return limits;
    }

    @Override
    protected List<String> makeCommands() {
        List<String> commands = new ArrayList<>();
        commands.add("/bin/bash");
        commands.add("./xai.sh");
        commands.add(this.getProcessKey());
        commands.add("0");

        return commands;
    }

    @Override
    protected List<V1ContainerPort> makePorts() {
        List<V1ContainerPort> ports = new ArrayList<>();

        return ports;
    }

    @Override
    protected List<V1EnvVar> makeEnv() {
        String key = this.getProcessKey();
        List<V1EnvVar> envList = super.makeEnv();

        envList.add(new V1EnvVar()
            .name("TF_CONFIG")
            .value(KubeUtil.generateTFConfigSingle(jobType, key, workerIdx)));

        if (!this.getGpuUse()) {
            envList.add(new V1EnvVar()
                .name("CUDA_VISIBLE_DEVICES")
                .value("-1"));
        }
        return envList;
    }

    private boolean getGpuUse() {
        return ((XAIInfo) this.info).getGpuUse();
    }
}
