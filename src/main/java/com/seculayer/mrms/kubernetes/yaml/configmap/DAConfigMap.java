package com.seculayer.mrms.kubernetes.yaml.configmap;

public class DAConfigMap extends KubeConfigMap {
    public DAConfigMap(){
        this.metaname = "da-conf";
        this.volumeName = this.metaname + "-v";
        this.configPath = "/eyeCloudAI/app/ape/da/conf";
    }
}
