package com.seculayer.mrms.kubernetes.yaml.configmap;

public class HPRSConfigMap extends KubeConfigMap {
    public HPRSConfigMap(){
        this.metaname = "hprs-conf";
        this.volumeName = this.metaname + "-v";
        this.configPath = "/eyeCloudAI/app/ape/hprs/conf";
    }
}
