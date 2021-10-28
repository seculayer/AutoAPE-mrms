package com.seculayer.mrms.kubernetes;

import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMapFactory;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class KubeUtil {
    static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // Kubernetes Pod Utils
    public static V1ConfigMapList getAllConfigMapList(String namespace){
        CoreV1Api api = new CoreV1Api();
        try {
            V1ConfigMapList configMapList = api.listNamespacedConfigMap(
                    namespace, null, null, null, null,
                    null, null, null, null, null);
            return configMapList;
        } catch(ApiException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static V1ConfigMap getConfigMap(String metaname, V1ConfigMapList configMapList){
        for (V1ConfigMap configMap : configMapList.getItems()){
            if (configMap.getMetadata().getName().equals(metaname))
                return configMap;
        }
        return null;
    }

    public static List<KubeConfigMap> getConfMapList(String namespace, List<String> metanameList){
        List<KubeConfigMap> configMapList = new ArrayList<>();
        V1ConfigMapList allConfigMapList = KubeUtil.getAllConfigMapList(namespace);

        for (String metaname : metanameList){
            V1ConfigMap tempConfigMap = KubeUtil.getConfigMap(metaname, allConfigMapList);
            configMapList.add(KubeConfigMapFactory.get(tempConfigMap));

        }

        return configMapList;
    }

    public static KubeConfigMap getKubeConfigMap(String metaname, List<KubeConfigMap> configMapList){
        for (KubeConfigMap configMap : configMapList){
            if (configMap.getMetaname().equals(metaname))
                return configMap;
        }
        return null;
    }

    public static List<KubeConfigMap> getKubeConfMapList(List<KubeConfigMap> podConfigMapList, List<String> metanameList){
        List<KubeConfigMap> configMapList = new ArrayList<>();
        for (String metaname : metanameList){
            KubeConfigMap tempConfigMap = KubeUtil.getKubeConfigMap(metaname, podConfigMapList);
            configMapList.add(tempConfigMap);
        }
        return configMapList;
    }

    public static V1Volume getV1VolumeEmptyDir(String name){
        return new V1Volume()
                .name(name)
                .emptyDir(new V1EmptyDirVolumeSource());
    }

    public static V1Volume getV1VolumeFromHostPath(String name, String path){
        return new V1Volume()
                .name(name)
                .hostPath(new V1HostPathVolumeSource()
                        .path(path)
                        .type("Directory"));
    }

    public static V1Volume getV1VolumeFromHostPathFile(String name, String path){
        return new V1Volume()
                .name(name)
                .hostPath(new V1HostPathVolumeSource()
                        .path(path));
    }

    // ml common kubernetes objects
    public static List<V1Volume> volumes(){
        List<V1Volume> volumes = new ArrayList<>();
        volumes.add(KubeUtil.getV1VolumeEmptyDir("features"));
        volumes.add(KubeUtil.getV1VolumeEmptyDir("verifys"));
        volumes.add(KubeUtil.getV1VolumeEmptyDir("temp"));
        volumes.add(KubeUtil.getV1VolumeFromHostPath("models", "/eyeCloudAI/data/processing/ape/temp"));
        volumes.add(KubeUtil.getV1VolumeFromHostPath("division", "/eyeCloudAI/data/processing/ape/division"));
        volumes.add(KubeUtil.getV1VolumeFromHostPath("errors", "/eyeCloudAI/data/processing/ape/errors"));
        volumes.add(KubeUtil.getV1VolumeFromHostPath("rtdetect", "/eyeCloudAI/data/processing/ape/rtdetect"));
        volumes.add(KubeUtil.getV1VolumeFromHostPath("detects", "/eyeCloudAI/data/processing/ape/detects"));
        volumes.add(KubeUtil.getV1VolumeFromHostPath("results", "/eyeCloudAI/data/processing/ape/results"));
        volumes.add(KubeUtil.getV1VolumeFromHostPath("storage", "/eyeCloudAI/data/storage"));
        volumes.add(KubeUtil.getV1VolumeFromHostPathFile("tz", "/usr/share/zoneinfo/Asia/Seoul"));
        return volumes;
    }

    public static List<V1Volume> daVolumes(){
        List<V1Volume> volumes = new ArrayList<>();
        volumes.add(KubeUtil.getV1VolumeEmptyDir("temp"));
        volumes.add(KubeUtil.getV1VolumeFromHostPathFile("tz", "/usr/share/zoneinfo/Asia/Seoul"));
        return volumes;
    }

    public static V1VolumeMount getVolumeMountFromPath(String name, String path){
        return new V1VolumeMount()
                .name(name)
                .mountPath(path);
    }

    public static List<String> mlConfigMapNames(){
        List<String> configMapNameList = new ArrayList<>();
//        configMapNameList.add("da-conf");

        return configMapNameList;
    }

    public static void main(String[] args) {
        KubernetesManager.getInstance().initialize();
        String namespace = "apeautoml";
        String metaName = "mrms-conf";
    }
}
