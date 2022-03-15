package com.seculayer.mrms.kubernetes.yaml.configmap;

public class XAIConfigMap extends KubeConfigMap {
    public XAIConfigMap(){
        this.metaname = "xai-conf";
        this.volumeName = this.metaname + "-v";
        this.configPath = "/eyeCloudAI/app/ape/xai/conf";
    }
}
