package com.seculayer.mrms.kubernetes.yaml.configmap;

import io.kubernetes.client.openapi.models.*;

import java.util.ArrayList;
import java.util.List;

public abstract class KubeConfigMap {
    protected V1ConfigMap configMap;
    protected String configPath;
    protected String metaname;
    protected V1Volume volume;
    protected String volumeName;
    protected V1VolumeMount volumeMount;

    public KubeConfigMap() { }

    public KubeConfigMap configMap(V1ConfigMap configMap){
        this.configMap = configMap;
        this.volume = this.makeVolume();
        this.volumeMount = this.makeVolumeMount();
        return this;
    }

    //------------------------------------------------------------------------------------------------
    // private functions
    private V1Volume makeVolume(){
        return new V1Volume()
                .name(this.volumeName)
                .configMap(
                        new V1ConfigMapVolumeSource()
                                .name(this.metaname)
                                .items(this.getKeyToPathList()));
    }

    private List<V1KeyToPath> getKeyToPathList(){
        List<V1KeyToPath> keyToPathList = new ArrayList<>();
        for (String key : this.configMap.getData().keySet()){
            keyToPathList.add(new V1KeyToPath()
                    .key(key)
                    .path(key));
        }
        return keyToPathList;
    }

    private V1VolumeMount makeVolumeMount(){
        return new V1VolumeMount()
                .name(this.volumeName)
                .mountPath(this.configPath);
    }

    //------------------------------------------------------------------------------------------------
    //getter and setter
    public V1ConfigMap getConfigMap() { return configMap; }
    public String getConfigPath() { return configPath; }
    public String getMetaname() { return metaname; }
    public V1Volume getVolume() { return volume; }
    public String getVolumeName() { return this.volumeName; }
    public V1VolumeMount getVolumeMount() { return this.volumeMount; }
}
