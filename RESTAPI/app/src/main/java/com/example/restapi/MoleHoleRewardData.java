package com.example.restapi;

public class MoleHoleRewardData implements Comparable<MoleHoleRewardData> {

    private String name;
    private String count;

    public MoleHoleRewardData(){
    }
    public MoleHoleRewardData(String name, String count){
        this.name = name;
        this.count = count;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public int compareTo(MoleHoleRewardData moleHoleRewardData) {
        if (Integer.parseInt(this.count) < Integer.parseInt(moleHoleRewardData.count)){
            return 1;
        }
        else if (Integer.parseInt(this.count) > Integer.parseInt(moleHoleRewardData.count)){
            return -1;
        }
        else{
            return 0;
        }
    }
}
