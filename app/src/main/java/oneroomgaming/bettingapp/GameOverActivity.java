package oneroomgaming.bettingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GameOverActivity extends AppCompatActivity {
    private DecimalFormat decFormat = new DecimalFormat("$0.00;$-0.00");
    private LinearLayout gameOverPlayerLayout;
    private Game game;
    private ArrayList<Player> players;

    private Button exitButton;
    private Button newGameButton;

    private Button awardPotButton;
    private TextView awardPotAmountView;
    private Spinner awardPlayerPotSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        newGameButton = findViewById(R.id.newGameButton);
        exitButton = findViewById(R.id.exitButton);

        awardPotButton = (Button) findViewById(R.id.awardPotButton);
        awardPlayerPotSpinner = (Spinner) findViewById(R.id.awardPotPlayerSelector);
        awardPotAmountView = (TextView) findViewById(R.id.potAwardAmountView);

        gameOverPlayerLayout = findViewById(R.id.gameOverPlayerLayout);
        game = (Game) getIntent().getExtras().get("game");
        players = (ArrayList<Player>) getIntent().getExtras().get("players");

        LayoutInflater inflater = getLayoutInflater();
        for(Player p : game.getPlayers()){
            View v = inflater.inflate(R.layout.game_over_player_row,null);

            TextView playerName = (TextView) v.findViewById(R.id.gameOverPlayerNameView);
            TextView playerBalance = (TextView) v.findViewById(R.id.gameOverPlayerBalanceView);

            playerName.setText(p.getName());
            playerBalance.setText(decFormat.format(p.getBalance()));

            if(p.getBalance() < 0){
                playerBalance.setTextColor(getColor(R.color.negative));
            }else{
                playerBalance.setTextColor(getColor(R.color.positive));
            }

            gameOverPlayerLayout.addView(v);
        }

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToStart = new Intent(getApplicationContext(), InitializeGameActivity.class);
                startActivity(goToStart);
                finish();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        ArrayList<String> playerNames = new ArrayList<String>();
        for(Player p : players){
            playerNames.add(p.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, playerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        awardPlayerPotSpinner.setAdapter(adapter);

        awardPotAmountView.setText(decFormat.format(game.getPot()));

        awardPotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = awardPlayerPotSpinner.getSelectedItem().toString();

                for(Player p : players){
                    if(p.getName() == playerName){
                        System.out.println("Winner was:" + playerName);

                        p.win(game.getPot());
                        game.setPot(0);

                        awardPotAmountView.setText(decFormat.format(game.getPot()));

                        for(int i=0; i<gameOverPlayerLayout.getChildCount(); i++){
                            View playerRow = gameOverPlayerLayout.getChildAt(i);
                            TextView playerNameView =(TextView) playerRow.findViewById(R.id.gameOverPlayerNameView);

                            if(playerNameView.getText().toString().equals(playerName)){
                                TextView balance = playerRow.findViewById(R.id.gameOverPlayerBalanceView);
                                balance.setText(decFormat.format(p.getBalance()));
                                if(p.getBalance() < 0){
                                    balance.setTextColor(getColor(R.color.negative));
                                }else{
                                    balance.setTextColor(getColor(R.color.positive));
                                }
                                break;
                            }
                        }
                    }
                }

            }
        });

    }
}