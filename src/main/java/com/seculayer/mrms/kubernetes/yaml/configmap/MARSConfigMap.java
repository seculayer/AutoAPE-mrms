package com.seculayer.mrms.kubernetes.yaml.configmap;

public class MARSConfigMap extends KubeConfigMap {
    public MARSConfigMap(){
        this.metaname = "mars-conf";
        this.volumeName = this.metaname + "-v";
        this.configPath = "/eyeCloudAI/app/ape/mars/conf";
    }
}
