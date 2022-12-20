package com.example.messbook.Model;

public class MemberModel {
    private String name;
    private int money;
    private float meal;


    public MemberModel(String name, int money, float meal) {
        this.name = name;
        this.money = money;
        this.meal = meal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public float getMeal() {
        return meal;
    }

    public void setMeal(float meal) {
        this.meal = meal;
    }
}
