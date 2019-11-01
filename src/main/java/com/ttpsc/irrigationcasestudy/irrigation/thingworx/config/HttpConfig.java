package com.ttpsc.irrigationcasestudy.irrigation.thingworx.config;

import java.text.MessageFormat;

public class HttpConfig {
    private static final String urlPattern = "http://{0}:{1}{2}";

    private String host;

    private String port;

    private String resourceUri;

    private String appKey;


    public HttpConfig(String host, String port, String appKey) {
        this.host = host;
        this.port = port;
        this.resourceUri = "";
        this.appKey = appKey;
    }

    public String getAppKey() {
        return appKey;
    }

    /**
     * Be advised! Input uri format must start with '/'
     * @param resourceUri
     */
    public String getResourceUrl(String resourceUri) {
        return composeUrl(host, port, resourceUri);
    }

    private String composeUrl(String host, String port, String uri) {
        return MessageFormat.format(urlPattern, host, port, uri);
    }
}
