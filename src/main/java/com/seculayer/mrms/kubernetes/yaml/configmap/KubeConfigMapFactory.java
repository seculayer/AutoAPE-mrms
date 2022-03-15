package com.seculayer.mrms.kubernetes.yaml.configmap;

import io.kubernetes.client.openapi.models.V1ConfigMap;

public class KubeConfigMapFactory {
    public static KubeConfigMap get(V1ConfigMap configMap){
        String metaname = configMap.getMetadata().getName();

        switch (metaname){
            case "da-conf":
                return new DAConfigMap().configMap(configMap);
            case "dprs-conf":
                return new DPRSConfigMap().configMap(configMap);
            case "mars-conf":
                return new MARSConfigMap().configMap(configMap);
            case "hprs-conf":
                return new HPRSConfigMap().configMap(configMap);
            case "mlps-conf":
                return new MLPSConfigMap().configMap(configMap);
            case "xai-conf":
                return new XAIConfigMap().configMap(configMap);
            default:
                return null;
        }
    }
}
