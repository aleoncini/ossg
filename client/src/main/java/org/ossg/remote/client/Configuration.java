package org.ossg.remote.client;

public class Configuration {

    private boolean authenticated = false;
    private String protocol;
    private String username;
    private String password;
    private String host;
    private int port;

    public final static String HTTP = "http";
    public final static String SECURE_HTTP = "https";

    public Configuration(){
        // default configuration create a connection
        // to localhost, port 8080, with no SSL and no authentication
        this.protocol = HTTP;
        this.host = "localhost";
        this.port = 8080;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public Configuration setAuthenticated() {
        this.authenticated = true;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public Configuration setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Configuration setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public Configuration setPort(int port) {
        this.port = port;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Configuration setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Configuration setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getBaseUrl() {
        StringBuffer buffer = new StringBuffer(protocol)
                .append("://")
                .append(host);
        if (port != 80){
            buffer.append(":").append(port);
        }
        return buffer.toString();
    }

}
