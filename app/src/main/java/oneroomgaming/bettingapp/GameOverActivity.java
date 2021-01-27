package oneroomgaming.bettingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        newGameButton = findViewById(R.id.newGameButton);
        exitButton = findViewById(R.id.exitButton);

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
    }
}