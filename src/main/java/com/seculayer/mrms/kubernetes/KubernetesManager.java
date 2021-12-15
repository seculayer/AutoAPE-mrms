package com.seculayer.mrms.kubernetes;

import com.seculayer.mrms.managers.MRMServerManager;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.FileReader;
import java.io.IOException;

public class KubernetesManager {
    static Logger logger = LogManager.getLogger();

    public KubernetesManager(){ }

    public void initialize(){
        try {
            this.inOfClusterConfig();
            logger.info("in cluster client initialized.");
        }catch (Exception e) {
            try {
                this.outOfClusterConfig();
                logger.info("kube config file client initialized.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    private static class Singleton {
        private static final KubernetesManager instance = new KubernetesManager();
    }

    public static KubernetesManager getInstance() {
        return Singleton.instance;
    }

    private void inOfClusterConfig() throws IOException {
        ApiClient client = ClientBuilder.cluster().build();
        String debugYN = MRMServerManager.getInstance().getConfiguration().get("kubernetes.log.debug.yn", "N");
        if ("y".equals(debugYN.toLowerCase())) {
            client.setDebugging(true);
        }
        Configuration.setDefaultApiClient(client);
    }

    private void outOfClusterConfig() throws IOException {
        String kubeConfigPath = System.getenv("KUBE_CONFIG");
        KubeConfig kubeConfig = KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath));
        ClientBuilder clientBuilder = ClientBuilder.kubeconfig(kubeConfig);
        ApiClient client = clientBuilder.build();
        String debugYN = MRMServerManager.getInstance().getConfiguration().get("kubernetes.log.debug.yn", "N");
        if ("y".equals(debugYN.toLowerCase())) {
            client.setDebugging(true);
        }
        Configuration.setDefaultApiClient(client);
    }
}
