package com.seculayer.mrms.rest;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.util.conf.Configuration;
import com.seculayer.mrms.rest.ServletFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class HTTPServerManager {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Configuration conf = MRMServerManager.getInstance().getConfiguration();

    private final Server server;

    public HTTPServerManager(){
        // Configurations
        final int port = conf.getInt("control.http.port", 9200);
        final boolean isSSL = conf.getBoolean("control.ssl.use", false);
        HttpConfiguration httpConfig = this.setHttpConfig(port);

        // init server
        this.server = new Server(port);

        // Connector
        ServerConnector serverConnector = this.createConnector(server, isSSL, port, httpConfig);
        this.server.setConnectors(new Connector[] { serverConnector });

        // handlers
        ServletHandler handler = ServletFactory.createServletHandler();
        this.server.setHandler(handler);
        this.server.setSessionIdManager(new HashSessionIdManager(new Random()));
        this.server.setStopAtShutdown(true);

        logger.info("--------------------------------------------");
        logger.info("init jetty http server...");
        logger.info("http control on port {}, SSL is {}", port, isSSL);
        logger.info("--------------------------------------------");
    }

    private HttpConfiguration setHttpConfig(int port){
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme("https");
        httpConfig.setSecurePort(port);
        httpConfig.setOutputBufferSize(32 * 1024);
        httpConfig.setRequestHeaderSize(8 * 1024);
        httpConfig.setResponseHeaderSize(8 * 1024);
        httpConfig.setSendServerVersion(false);
        httpConfig.setSendDateHeader(false);
        httpConfig.setSendXPoweredBy(false);
        httpConfig.addCustomizer(new SecureRequestCustomizer());
        return httpConfig;
    }

    private ServerConnector createConnector(Server server, boolean isSSL, int port, HttpConfiguration httpConfig){
        ServerConnector serverConnector = this.createServerConnector(server, isSSL, httpConfig);
        serverConnector.setPort(port);
        serverConnector.setReuseAddress(true);
        return serverConnector;
    }

    private ServerConnector createServerConnector(Server server, boolean isSSL, HttpConfiguration httpConfig){
        if (isSSL){
            return this.createSSLServerConnector(server, httpConfig);
        } else {
            return new ServerConnector(server);
        }
    }
    private ServerConnector createSSLServerConnector(Server server, HttpConfiguration httpConfig){
        SslContextFactory sslCtxFactory = new SslContextFactory();
        sslCtxFactory.setKeyStorePath("./conf/apeautoml.keystore");
        sslCtxFactory.setTrustStorePath("./conf/apeautoml.truststore");
        sslCtxFactory.setKeyManagerPassword("seculayer!@#");
        sslCtxFactory.setKeyStorePassword("seculayer!@#");
        sslCtxFactory.setTrustStorePassword("seculayer!@#");
        return new ServerConnector(server,
                new SslConnectionFactory(sslCtxFactory, "http/1.1"),
                new HttpConnectionFactory(httpConfig));
    }

    public void start() throws Exception {
        logger.info("start jetty http server...");
        this.server.start();
    }

    public void stop() throws Exception {
        this.server.stop();
        logger.info("jetty http server terminated...");
    }
}
