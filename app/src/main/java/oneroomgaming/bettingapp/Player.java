package oneroomgaming.bettingapp;

import java.io.Serializable;

public class Player implements Serializable {


    private String name;
    private double balance;
    private boolean anted;

    public Player(String name, double balance){
        this.name = name;
        this.balance = balance;

        this.anted = false;
    }

    public String getName() { return name; }

    public double getBalance() { return balance; }

    public void addToBalance(double amt){ balance += amt; }

    public void subtractFromBalance(double amt){ balance -= amt; }

    public void ante(double amt){
        subtractFromBalance(amt);
        anted = true;
    }

    public void lose(double amt){
        subtractFromBalance(amt);
    }

    public void safe(double amt){

    }

    public void unAnte(){
        anted = false;
    }

    public void win(double amt){
        addToBalance(amt);
    }

    public boolean isAnted() { return anted; }
}
