package com.seculayer.mrms.kubernetes;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMap;
import com.seculayer.mrms.kubernetes.yaml.configmap.KubeConfigMapFactory;
import com.seculayer.util.StringUtil;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.proto.V1;
import org.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.*;

public class KubeUtil {
    static Logger logger = LogManager.getLogger();
    public static final int TF_CONTAINER_PORT = 9304;

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

    public static List<V1Volume> rcmdVolumes(){
        List<V1Volume> volumes = new ArrayList<>();
        volumes.add(KubeUtil.getV1VolumeEmptyDir("temp"));
        volumes.add(KubeUtil.getV1VolumeFromHostPathFile("tz", "/usr/share/zoneinfo/Asia/Seoul"));
        return volumes;
    }

    public static List<V1Volume> xaiVolumes() {
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

    public static String generateTFConfig(String prefix, String key, int numWorker, int workerIdx){
        //---- TF Config
        Map<String, Object> tfConfig = new HashMap<>();

        //--- make Cluster
        Map<String, Object> cluster = new HashMap<>();

        //-- make worker
        List<String> worker = new ArrayList<>();
        for (int i = 0; i < numWorker; i++){
            String workerName;
            workerName = String.format(
                "%s:%s",
                KubeUtil.makeWorkerName(prefix, key, i),
                KubeUtil.TF_CONTAINER_PORT
            );
            worker.add(workerName);
        }

        cluster.put("worker", worker);
        tfConfig.put("cluster", cluster);

        //--- make task
        Map<String, Object> task = new HashMap<>();
        task.put("type", "worker");
        task.put("index", String.valueOf(workerIdx));

        tfConfig.put("task", task);

        try {
            return new JSONObject(tfConfig).toString();
        } catch(Exception e){
            return "";
        }
    }

    public static String generateTFConfigSingle(String prefix, String key, int workerIdx){
        //---- TF Config
        Map<String, Object> tfConfig = new HashMap<>();

        //--- make Cluster
        Map<String, Object> cluster = new HashMap<>();

        //-- make worker
        List<String> worker = new ArrayList<>();
        String workerName;
        workerName = String.format(
            "%s:%s",
            KubeUtil.makeWorkerName(prefix, key, workerIdx),
            KubeUtil.TF_CONTAINER_PORT
        );
        worker.add(workerName);

        cluster.put("worker", worker);
        tfConfig.put("cluster", cluster);

        //--- make task
        Map<String, Object> task = new HashMap<>();
        task.put("type", "worker");
        task.put("index", String.valueOf(0));

        tfConfig.put("task", task);

        try {
            return new JSONObject(tfConfig).toString();
        } catch(Exception e){
            return "";
        }

    }

    private static String makeWorkerName(String prefix, String key, int workerIdx) {
        return String.format("%s-%s-%s", prefix, key, workerIdx);
    }

    public static String getJobLogs(String jobName, String containerName, boolean tail){
        try {
            CoreV1Api coreV1Api = new CoreV1Api();

            V1PodList podList = coreV1Api.listNamespacedPod(
                Constants.KUBE_EYECLOUDAI_NAMESPACE, "true", null,null, null,
                null, null, null, null, null
            );

            for (V1Pod pod : podList.getItems()){
                String podName = StringUtil.get(Objects.requireNonNull(pod.getMetadata()).getName());
                if (podName.contains(jobName)){
                    Integer tailVal;
                    if (tail) {
                        tailVal = 100;
                    } else{
                        tailVal = null;
                    }
                    return coreV1Api.readNamespacedPodLog(
                        podName, Constants.KUBE_EYECLOUDAI_NAMESPACE, containerName, false,
                        null, null, "true", false,
                        null, tailVal, null
                    );
                }
            }

            return "";

        } catch(ApiException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
    public static void main(String[] args) {
        KubernetesManager.getInstance().initialize();
        String namespace = "apeautoml";
        String metaName = "mrms-conf";
    }
}
