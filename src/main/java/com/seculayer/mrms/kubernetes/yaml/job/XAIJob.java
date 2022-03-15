package com.seculayer.mrms.kubernetes.yaml.job;

import com.seculayer.mrms.info.XAIInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1Volume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XAIJob extends KubeJob {
    @Override
    protected List<String> makeConfigMapName() {
        List<String> configMapNameList = new ArrayList<>();
        configMapNameList.add("xai-conf");

        return configMapNameList;
    }

    @Override
    public V1Job make() {
        this.metaname = String.format("%s-%s-%s", jobType, ((XAIInfo) this.info).getXAIHistNo(), 0);
        this.labels = this.makeLabels();
        this.containers = this.makeContainers();
        this.volumes = this.makeVolumes();
        return this.makeJob();
    }

    @Override
    protected List<V1Volume> makeVolumes() {
        List<V1Volume> volumes = KubeUtil.xaiVolumes();

        for (KubeConfigMap configMap : this.configMapList){
            volumes.add(configMap.getVolume());
        }

        return volumes;
    }

    @Override
    protected Map<String, String> makeLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("name", String.format("%s-%s", jobType, ((XAIInfo) this.info).getXAIHistNo()));
        labels.put("job", "worker");
        labels.put("task", "0");
        return labels;
    }

    @Override
    protected List<V1Container> makeContainers() {
        return this.xaiContainers(jobType);
    }
}
