package com.example.jinglaixue;

import java.util.Date;


public class FailTimes {

    public Double duration;
    public String time;
    public Double set_time;

    private String durationStr;
    private String set_timeStr;

    public void formatData(){
        //2020-06-07 15:17:55
        time = time.replace("T"," ").substring(0,19);

        durationStr = secondToTime(duration.longValue());
        set_timeStr = secondToTime(set_time.longValue());
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public String getSet_timeStr() {
        return set_timeStr;
    }

    public void setSet_timeStr(String set_timeStr) {
        this.set_timeStr = set_timeStr;
    }

    /**
     * 返回日时分秒
     * @param second
     * @return
     */
    private String secondToTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days){
            return days + "天，"+hours+"小时，"+minutes+"分，"+second+"秒";
        }else if (hours == 0){
            return minutes+"分，"+second+"秒";
        }else if(minutes == 0){
            return second+"秒";
        }else {
            return hours+"小时，"+minutes+"分，"+second+"秒";
        }
    }

}
