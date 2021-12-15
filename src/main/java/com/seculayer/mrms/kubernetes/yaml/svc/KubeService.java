package com.seculayer.mrms.kubernetes.yaml.svc;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.info.InfoAbstract;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServicePort;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Map;

public abstract class KubeService {
    private Logger logger = LogManager.getLogger();

    protected String metaname;
    protected String type = "LoadBalancer";
    protected List<V1ServicePort> ports;
    protected Map<String, String> selector;

    public KubeService() {}

    protected InfoAbstract info;
    public KubeService info(InfoAbstract info){
        this.info = info;
        return this;
    }

    protected int workerIdx = 0;
    public KubeService workerIdx(int workerIdx){
        this.workerIdx = workerIdx;
        return this;
    }

    protected String jobType;
    public KubeService jobType(String jobType){
        this.jobType = jobType;
        return this;
    }

    protected V1Service makeService() {
        logger.debug("-------------------------------------------");
        logger.debug("In KubeService..");
        logger.debug("name : {}", this.metaname);
        logger.debug("type : {}", this.type);
        logger.debug("selector : {}", this.selector.toString());
        logger.debug("ports : {}, cnt : {}",this.ports.toString(), this.ports.size());
        logger.debug("-------------------------------------------");

        return new V1Service()
                .apiVersion("v1")
                .kind("Service")
                .metadata(new V1ObjectMeta()
                        .name(this.metaname)
                        .namespace(Constants.KUBE_EYECLOUDAI_NAMESPACE))
                .spec(new V1ServiceSpec()
                        .type(type)
                        .ports(ports)
                        .selector(selector));
    }

    public abstract V1Service make();
    protected abstract Map<String, String> makeSelector();
    protected abstract List<V1ServicePort> makePorts();
}
