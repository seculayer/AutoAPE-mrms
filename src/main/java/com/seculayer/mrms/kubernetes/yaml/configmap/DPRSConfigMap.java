package com.seculayer.mrms.kubernetes.yaml.configmap;

public class DPRSConfigMap extends KubeConfigMap {
    public DPRSConfigMap(){
        this.metaname = "dprs-conf";
        this.volumeName = this.metaname + "-v";
        this.configPath = "/eyeCloudAI/app/ape/dprs/conf";
    }
}
