package com.seculayer.mrms.kubernetes;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ConfigMapList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class KubeUtil {
    static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static V1ConfigMap getConfigMap(String metaName, String namespace){
        CoreV1Api api = new CoreV1Api();
        try {
            return api.readNamespacedConfigMap(metaName, namespace, null, null, null);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return new V1ConfigMap();
    }

    public static V1ConfigMapList getConfigMapList(String namespace) {
        CoreV1Api api = new CoreV1Api();
        try {
            return api.listNamespacedConfigMap(namespace, null,
                    null, null, null,
                    null, null, null, null, null);

        } catch (ApiException e) {
            e.printStackTrace();
        }
        return new V1ConfigMapList();
    }

    public static void main(String[] args) {
        KubernetesManager.getInstance().initialize();
        String namespace = "apeautoml";
        String metaName = "mrms-conf";

        System.out.println(getConfigMap(metaName, namespace));
        System.out.println(getConfigMapList(namespace).toString());
    }
}
