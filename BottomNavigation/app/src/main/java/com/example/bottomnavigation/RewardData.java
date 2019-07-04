package com.example.bottomnavigation;

public class RewardData implements Comparable<RewardData> {

    private String name;
    private String sec;
    private String mil;

    public RewardData(){
    }
    public RewardData(String name, String sec, String mil){
        this.name = name;
        this.sec = sec;
        this.mil = mil;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSec() {
        return sec;
    }

    public void setSec(String title) {
        this.sec = title;
    }

    public String getMil() {
        return mil;
    }

    public void setMil(String content) {
        this.mil = content;
    }

    @Override
    public int compareTo(RewardData rewardData) {
        if (Integer.parseInt(this.sec) < Integer.parseInt(rewardData.sec)){
            return -1;
        }
        else if (Integer.parseInt(this.sec) > Integer.parseInt(rewardData.sec)){
            return 1;
        }
        else if (Integer.parseInt(this.mil) > Integer.parseInt(rewardData.mil)){
            return 1;
        }
        else if (Integer.parseInt(this.mil) < Integer.parseInt(rewardData.mil)){
            return -1;
        }
        else{
            return 0;
        }
    }
}
