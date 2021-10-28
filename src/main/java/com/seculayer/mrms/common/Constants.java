package com.seculayer.mrms.common;

public class Constants {
    public static final String IMAGE_VERSION = "1.0.0";

    public static final String REST_PACKAGES = "com.seculayer.mrms.rest.servlet";

    public static final String[] REST_SUB_PACKS = new String[]{
        "delete", "insert", "select", "update", "etc"
    };

    public static final String KUBE_EYECLOUDAI_NAMESPACE = "apeautoml";

    public static final String JOB_TYPE_DA_CHIEF = "da-chief";
    public static final String JOB_TYPE_DA_WORKER = "da-worker";
    public static final String JOB_TYPE_RCMD = "recommend";

    public static final String STATUS_DA_REQ = "5";
    public static final String STATUS_DA_ING = "6";
    public static final String STATUS_DA_ERROR = "8";

    public static final String STATUS_RCMD_REQ = "3";
    public static final String STATUS_RCMD_ING = "4";
    public static final String STATUS_COMPLETE = "6";

}
