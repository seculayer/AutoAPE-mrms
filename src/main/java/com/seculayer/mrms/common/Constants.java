package com.seculayer.mrms.common;

import com.seculayer.mrms.managers.MRMServerManager;

public class Constants {
    public static final String REGISTRY_URL = MRMServerManager.getInstance().getConfiguration().get("registry.url", "registry.seculayer.com:31500/ape");
    public static final String DA_IMAGE_VERSION = MRMServerManager.getInstance().getConfiguration().get("da.tag", "1.0-2023.1.0301");
    public static final String EDA_IMAGE_VERSION = MRMServerManager.getInstance().getConfiguration().get("eda.tag", "1.0-2023.1.0301");
    public static final String DPRS_IMAGE_VERSION = MRMServerManager.getInstance().getConfiguration().get("dprs.tag", "1.0-2023.1.0301");
    public static final String MARS_IMAGE_VERSION = MRMServerManager.getInstance().getConfiguration().get("mars.tag", "1.0-2023.1.0301");
    public static final String HPRS_IMAGE_VERSION = MRMServerManager.getInstance().getConfiguration().get("hprs.tag", "1.0-2023.1.0301");
    public static final String MLPS_IMAGE_VERSION = MRMServerManager.getInstance().getConfiguration().get("mlps.tag", "3.0-2023.1.0301");
    public static final String XAI_IMAGE_VERSION = MRMServerManager.getInstance().getConfiguration().get("xai.tag", "1.0-2023.1.0301");

    public static final String REST_PACKAGES = "com.seculayer.mrms.rest.servlet";

    public static final String[] REST_SUB_PACKS = new String[]{
        "delete", "insert", "select", "update", "etc"
    };

    public static final String KUBE_EYECLOUDAI_NAMESPACE = "apeautoml";

    public static final String JOB_TYPE_DA_CHIEF = "da-chief";
    public static final String JOB_TYPE_DA_WORKER = "da-worker";
    public static final String JOB_TYPE_EDA_CHIEF = "eda-chief";
    public static final String JOB_TYPE_EDA_WORKER = "eda-worker";
    public static final String JOB_TYPE_DPRS = "dprs";
    public static final String JOB_TYPE_MARS = "mars";
    public static final String JOB_TYPE_HPRS = "hprs";
    public static final String JOB_TYPE_LEARN = "learn";
    public static final String JOB_TYPE_INFERENCE = "inference";
    public static final String JOB_TYPE_XAI = "xai";

    public static final int GRPC_PORT = 9304;

    public static final String STATUS_DA_REQ = "5";
    public static final String STATUS_DA_ING = "6";
    public static final String STATUS_DA_COMPLETE = "7";
    public static final String STATUS_DA_ERROR = "8";
    public static final String STATUS_DA_RM_REQ = "9";

    public static final String STATUS_EDA_REQ = "1";
    public static final String STATUS_EDA_ING = "2";
    public static final String STATUS_EDA_COMPLETE = "5";
    public static final String STATUS_EDA_ERROR = "6";

    public static final String STATUS_PROJECT_RCMD_REQ = "3";
    public static final String STATUS_PROJECT_RCMD_ING = "4";
    public static final String STATUS_PROJECT_LEARN_REQ = "6";
    public static final String STATUS_PROJECT_LEARN_ING = "7";
    public static final String STATUS_PROJECT_COMPLETE = "8";
    public static final String STATUS_PROJECT_ERROR = "9";

    public static final String STATUS_LEARN_HIST_INIT = "1";
    public static final String STATUS_LEARN_COMPLETE = "6";
    public static final String STATUS_LEARN_ERROR = "7";

    public static final String STATUS_INFERENCE_REQ = "1";
    public static final String STATUS_INFERENCE_INIT = "2";
    public static final String STATUS_INFERENCE_ING = "5";
    public static final String STATUS_INFERENCE_COMPLETE = "6";
    public static final String STATUS_INFERENCE_ERROR = "7";

    public static final String STATUS_XAI_REQ = "1";
    public static final String STATUS_XAI_INIT = "2";
    public static final String STATUS_XAI_ING = "5";
    public static final String STATUS_XAI_COMPLETE = "6";
    public static final String STATUS_XAI_ERROR = "7";

    public static final String LIB_TYPE_TFV1 = "1";
    public static final String LIB_TYPE_KERAS = "2";
    public static final String LIB_TYPE_TFV2 = "3";
    public static final String LIB_TYPE_GS = "4";
    public static final String LIB_TYPE_SKL = "5";

    public static final String MODELING_MODE_BASIC = "1";
    public static final String MODELING_MODE_RELEARN = "2";
}
