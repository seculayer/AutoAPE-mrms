package com.seculayer.mrms.kubernetes.yaml.container;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ContainerPort;
import io.kubernetes.client.openapi.models.V1VolumeMount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EDAContainer extends KubeContainer {

    public EDAContainer(String jobType) {
        super(jobType);

        this.name = "eda";
        this.image = registryURL + "/automl-eda:"
                        + Constants.EDA_IMAGE_VERSION;
    }

    @Override
    public V1Container make() { return this.makeContainer(); }

    @Override
    protected List<String> makeConfigMapName() {
        List<String> configMapNameList = new ArrayList<>();
        configMapNameList.add("eda-conf");
        return configMapNameList;
    }

    @Override
    protected List<V1VolumeMount> makeVolumeMounts(){
        List<V1VolumeMount> volumeMounts = super.makeVolumeMounts();
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
        switch (jobType) {
            case Constants.JOB_TYPE_EDA_CHIEF:
                commands.add("/bin/bash");
                commands.add("./eda.sh");
                commands.add(this.getProcessKey());
                commands.add("0");
                commands.add("chief");
                return commands;
            case Constants.JOB_TYPE_EDA_WORKER:
                commands.add("/bin/bash");
                commands.add("./eda.sh");
                commands.add(this.getProcessKey());
                commands.add(Integer.toString(workerIdx));
                commands.add("worker");
                return commands;
            default:
        }

        return commands;
    }

    @Override
    protected List<V1ContainerPort> makePorts() {
        List<V1ContainerPort> ports = new ArrayList<>();

        return ports;
    }
}
