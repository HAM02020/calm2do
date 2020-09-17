package com.example.jinglaixue;

public class UserAccount {
    private String token;
    private String time_id;
    private static  UserAccount instance = null;
    //服务器地址
    //public final String hostaddr = "http://47.102.212.0";
    //本地服务器地址
    public final String hostaddr = "http://10.0.2.2:8000";
    private  UserAccount(){

    }
    public synchronized static UserAccount shard(){
        if(instance==null){
            instance = new UserAccount();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTime_id() {
        return time_id;
    }

    public void setTime_id(String time_id) {
        this.time_id = time_id;
    }
}
