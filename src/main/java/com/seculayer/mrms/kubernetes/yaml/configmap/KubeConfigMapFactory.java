package com.seculayer.mrms.kubernetes.yaml.configmap;

import io.kubernetes.client.openapi.models.V1ConfigMap;

public class KubeConfigMapFactory {
    public static KubeConfigMap get(V1ConfigMap configMap){
        String metaname = configMap.getMetadata().getName();

        switch (metaname){
            case "da-conf":
                return new DAConfigMap().configMap(configMap);
            case "rcmd-conf":
                return null;
            default:
                return null;
        }
    }
}
