package com.daledawson.products.somedemo.RxJavaDemo;

/**
 * Created by Administrator on 2017/12/1/001.
 */

public class Swordsman {
    private String name;
    private String rank;

    public Swordsman(String name, String rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Swordsman{" +
                "name='" + name + '\'' +
                ", rank='" + rank + '\'' +
                '}';
    }
}
