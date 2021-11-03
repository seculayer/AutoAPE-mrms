package com.seculayer.mrms.kubernetes.yaml.svc;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.LearnInfo;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServicePort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MLPSService extends KubeService {
    @Override
    public V1Service make() {
        String key = "";
        switch (jobType) {
            case Constants.JOB_TYPE_LEARN:
                key = ((LearnInfo) this.info).getLearnHistNo();
                break;
        }

        this.metaname = String.format("%s-%s-%s", jobType, key, this.workerIdx);
        this.selector = this.makeSelector();
        this.ports = this.makePorts();
        return this.makeService();
    }

    @Override
    protected Map<String, String> makeSelector() {
        String key = "";
        switch (jobType) {
            case Constants.JOB_TYPE_LEARN:
                key = ((LearnInfo) this.info).getLearnHistNo();
                break;
        }

        Map<String, String> selector = new HashMap<>();
        selector.put("name", String.format("%s-%s", jobType, key));
        selector.put("job", "worker");
        selector.put("task", Integer.toString(this.workerIdx));
        return selector;
    }

    @Override
    protected List<V1ServicePort> makePorts() {
        List<V1ServicePort> ports = new ArrayList<>();
        ports.add(new V1ServicePort().port(Constants.GRPC_PORT));
        return ports;
    }
}
