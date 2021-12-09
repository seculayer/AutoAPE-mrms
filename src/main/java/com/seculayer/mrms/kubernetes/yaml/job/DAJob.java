package com.seculayer.mrms.kubernetes.yaml.job;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.DAInfo;
import com.seculayer.mrms.kubernetes.KubeUtil;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import com.seculayer.mrms.managers.MRMServerManager;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1Volume;
import io.sundr.shaded.org.apache.velocity.texen.util.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAJob extends KubeJob {

    @Override
    protected List<String> makeConfigMapName() {
        List<String> configMapNameList = new ArrayList<>();
        configMapNameList.add("da-conf");

        return configMapNameList;
    }

    @Override
    public V1Job make() {
        this.mkdirInitFolder();

        this.metaname = String.format("%s-%s-%s", jobType, ((DAInfo)this.info).getDatasetId(), this.workerIdx);
        this.labels = this.makeLabels();
        this.containers = this.makeContainers();
        this.volumes = this.makeVolumes();
        return this.makeJob();
    }

    public void mkdirInitFolder() {
        String folderPath = MRMServerManager.getInstance().getConfiguration().get("ape.da.dir") +
            "/" + ((DAInfo)this.info).getDatasetId();
        if (jobType.equals(Constants.JOB_TYPE_DA_WORKER)) {
            folderPath += "/" + this.workerIdx;
        }
        FileUtil.mkdir(folderPath);
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
