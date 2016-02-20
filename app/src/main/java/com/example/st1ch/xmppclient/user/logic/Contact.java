package com.example.st1ch.xmppclient.user.logic;

/**
 * Created by st1ch on 01.12.15.
 */
public class Contact {
    private String jid;
    private String name;
    private int imgId;
    private boolean isOnline;

    private static int id;


    public Contact(String jid, String name){
        this.jid = jid;
        this.name = name;
    }

    public Contact(String jid, String name, int img){
        this.jid = jid;
        this.name = name;
        this.imgId = img;
        this.isOnline = false;
    }

    public String getJID() {
        return jid;
    }

    public void setJID(String name) {
        this.jid = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String user) {
        this.name = user;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "jid='" + jid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
