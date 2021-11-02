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

public class RcmdContainer extends KubeContainer {

    public RcmdContainer(String jobType) {
        super(jobType);
        this.name = jobType;

        switch (jobType) {
            case Constants.JOB_TYPE_DPRS:
                this.image = registryURL + "/automl-dprs:"
                                + Constants.DPRS_IMAGE_VERSION;
                break;
            case Constants.JOB_TYPE_MARS:
                this.image = registryURL + "/automl-mars:"
                    + Constants.MARS_IMAGE_VERSION;
                break;
            case Constants.JOB_TYPE_HPRS:
                this.image = registryURL + "/automl-hprs:"
                    + Constants.HPRS_IMAGE_VERSION;
                break;
            default:
                this.image = "NoImage";
        }
    }
    @Override
    public V1Container make() {
        return this.makeContainer();
    }

    @Override
    protected List<String> makeConfigMapName() {
        List<String> configMapNameList = new ArrayList<>();
        configMapNameList.add(this.jobType + "-conf");

        return configMapNameList;
    }

    @Override
    protected List<V1VolumeMount> makeVolumeMounts(){
        List<V1VolumeMount> volumeMounts = super.makeRcmdVolumeMounts();
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
            case Constants.JOB_TYPE_DPRS:
                commands.add("/bin/bash");
                commands.add("./dprs.sh");
                commands.add(this.getProcessKey());
                commands.add(Integer.toString(this.workerIdx));
                return commands;
            case Constants.JOB_TYPE_MARS:
                commands.add("/bin/bash");
                commands.add("./mars.sh");
                commands.add(this.getProcessKey());
                commands.add(Integer.toString(this.workerIdx));
                return commands;
            case Constants.JOB_TYPE_HPRS:
                commands.add("/bin/bash");
                commands.add("./hprs.sh");
                commands.add(this.getProcessKey());
                commands.add(Integer.toString(this.workerIdx));
                return commands;
            default:
        }

        return commands;
    }

    @Override
    protected List<V1ContainerPort> makePorts() {
        return null;
    }
}
