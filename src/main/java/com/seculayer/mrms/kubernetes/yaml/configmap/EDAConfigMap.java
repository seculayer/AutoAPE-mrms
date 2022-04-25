package com.seculayer.mrms.kubernetes.yaml.configmap;

public class EDAConfigMap extends KubeConfigMap {
    public EDAConfigMap() {
        this.metaname = "eda-conf";
        this.volumeName = this.metaname + "-v";
        this.configPath = "/eyeCloudAI/app/ape/eda/conf";
    }
}
