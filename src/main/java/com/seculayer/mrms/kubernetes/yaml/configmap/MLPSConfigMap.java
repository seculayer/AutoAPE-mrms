package com.seculayer.mrms.kubernetes.yaml.configmap;

public class MLPSConfigMap extends KubeConfigMap {
    public MLPSConfigMap() {
        this.metaname = "mlps-conf";
        this.volumeName = this.metaname + "-v";
        this.configPath = "/eyeCloudAI/app/ape/mlps/conf";
    }
}
