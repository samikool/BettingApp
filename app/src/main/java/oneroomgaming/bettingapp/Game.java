package oneroomgaming.bettingapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private final ArrayList<Player> players;
    private final ArrayList<Player> antedPlayers;
    private final ArrayList<Player> losingPlayers;
    private final ArrayList<Player> safePlayers;
    private Player winner;

    private double pot;
    private double winAmt;



    private double anteAmt;
    private double loseAmt;

    public Game(int numPlayers, double anteAmt, double winAmt, double loseAmt){
        players = new ArrayList<Player>(numPlayers);
        antedPlayers = new ArrayList<Player>(numPlayers);
        losingPlayers = new ArrayList<Player>(numPlayers);
        safePlayers = new ArrayList<Player>(numPlayers);

        winner = null;

        this.winAmt = winAmt;
        this.anteAmt = anteAmt;
        this.loseAmt = loseAmt;
        pot = 0;
    }

    public double getPot() {
        return pot;
    }

    public void setWinAmt(double winAmt) {
        this.winAmt = winAmt;
    }
    public void setAnteAmt(double anteAmt) {
        this.anteAmt = anteAmt;
    }
    public void setLoseAmt(double loseAmt) {
        this.loseAmt = loseAmt;
    }
    public double getWinAmt() {
        return winAmt;
    }
    public double getAnteAmt() {
        return anteAmt;
    }
    public double getLoseAmt() {
        return loseAmt;
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public void removePlayer(Player p){
        players.remove(p);
    }

    public void setWinner(Player p){
        resetPlayer(p);
        winner = p;
    }

    public void addLoser(Player p) {
        resetPlayer(p);
        losingPlayers.add(p);
    }

    public void addSafer(Player p) {
        resetPlayer(p);
        safePlayers.add(p);
    }

    public void resetPlayer(Player p){
        if(winner == p) winner = null;
        losingPlayers.remove(p);
        safePlayers.remove(p);
    }

    public ArrayList<Player> getPlayers() { return players; }

    public void startRound(double anteAmount){
        checkAntes();
        pot = anteAmount * antedPlayers.size();
    }

    private void checkAntes(){
        for(Player p : players){
            if(p.isAnted()) antedPlayers.add(p);
        }
    }

    public void payOutToWinner(){
        if(winner == null) return;

        if(pot - winAmt < 0)  {
            winner.win(pot);
            pot = 0;
        }
        else {
            winner.win(winAmt);
            pot -= winAmt;
        }
        winner = null;
    }

    public void takeFromLosers(){
        for(Player loser : losingPlayers){
            pot += loseAmt;
            loser.lose(loseAmt);
        }
        losingPlayers.clear();
    }

    public void antePlayer(Player p){
        p.ante(anteAmt);
        antedPlayers.add(p);
        pot += anteAmt;
    }

    public void unAntePlayers(){
        for(Player p : players){ p.unAnte(); }
    }

    public void endRound(){
        payOutToWinner();
        takeFromLosers();
        unAntePlayers();
        antedPlayers.clear();
    }

    public void printState(){
        for(Player p : players){
            System.out.println(p.getName() + ": " + p.getBalance());
        }

        System.out.println("WINNER:");
        if (winner != null) System.out.println(winner.getName());
        else System.out.println("Not set ");


        System.out.println("LOSERS:");
        for(Player p : losingPlayers){
            System.out.println(p.getName());
        }

        System.out.println("SAFERS:");
        for(Player p : safePlayers){
            System.out.println(p.getName());
        }

        System.out.println("POT: " + pot);
    }

    public void endGame(){
        //don't know if i really need
    }
}
