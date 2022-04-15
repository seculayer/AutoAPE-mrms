package com.seculayer.mrms.kubernetes.yaml.configmap;

public class APEFlowConfigMap  extends KubeConfigMap {
    public APEFlowConfigMap() {
        this.metaname = "apeflow-conf";
        this.volumeName = this.metaname + "-v";
        this.configPath = "/eyeCloudAI/app/ape/apeflow/conf";
    }
}
