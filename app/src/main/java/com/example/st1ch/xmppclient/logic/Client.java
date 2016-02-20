package com.example.st1ch.xmppclient.logic;


/**
 * Created by st1ch on 09.10.15.
 */
public class Client {

    private static String hostIP;
    private static String username;
    private static String password;
    private static String serviceName;
    private static int port;

    public static Client instance = new Client();

    public Client(){
    }



    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public static Client getInstance() {
        return instance;
    }
}
