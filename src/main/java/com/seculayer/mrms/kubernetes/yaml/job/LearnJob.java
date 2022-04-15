package com.seculayer.mrms.kubernetes.yaml.job;

import com.seculayer.mrms.info.LearnInfo;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1Volume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearnJob extends KubeJob {
    @Override
    protected List<String> makeConfigMapName() {
        List<String> configMapNameList = new ArrayList<>();
        configMapNameList.add("mlps-conf");
        configMapNameList.add("apeflow-conf");

        return configMapNameList;
    }

    @Override
    public V1Job make() {
        this.metaname = String.format("%s-%s-%s", jobType, ((LearnInfo)this.info).getLearnHistNo(), this.workerIdx);
        this.labels = this.makeLabels();
        this.containers = this.makeContainers();
        this.volumes = this.makeVolumes();
        return this.makeJob();
    }

    @Override
    protected List<V1Volume> makeVolumes() {
        List<V1Volume> volumes = super.makeVolumes();

        for (KubeConfigMap configMap : this.configMapList){
            volumes.add(configMap.getVolume());
        }

        return volumes;
    }

    @Override
    protected Map<String, String> makeLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("name", String.format("%s-%s", jobType, ((LearnInfo)this.info).getLearnHistNo()));
        labels.put("job", "worker");
        labels.put("task", Integer.toString(workerIdx));
        return labels;
    }

    @Override
    protected List<V1Container> makeContainers() {
        return this.mlpsContainers(jobType);
    }
}
