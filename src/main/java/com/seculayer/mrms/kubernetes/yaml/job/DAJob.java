package com.seculayer.mrms.kubernetes.yaml.job;

import com.seculayer.mrms.info.DAInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1Volume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAJob extends KubeJob {

    @Override
    protected List<String> makeConfigMapName() {
        return KubeUtil.mlConfigMapNames();
    }

    @Override
    public V1Job make() {
        this.metaname = String.format("%s-%s-%s", jobType, ((DAInfo)this.info).getDatasetId(), this.workerIdx);
        this.labels = this.makeLabels();
        this.containers = this.makeContainers();
        this.volumes = this.makeVolumes();
        return this.makeJob();
    }

    @Override
    protected List<V1Volume> makeVolumes() {
        List<V1Volume> volumes = KubeUtil.daVolumes();

        for (KubeConfigMap configMap : this.configMapList){
            volumes.add(configMap.getVolume());
        }

        return volumes;
    }

    @Override
    protected Map<String, String> makeLabels() {
        Map<String, String> labels = new HashMap<>();
        return labels;
    }

    @Override
    protected List<V1Container> makeContainers() {
        return this.daContainers(jobType);
    }
}
