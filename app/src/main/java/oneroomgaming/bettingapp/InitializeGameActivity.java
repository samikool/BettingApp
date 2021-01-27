package oneroomgaming.bettingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;

public class InitializeGameActivity extends AppCompatActivity {
    private NumberFormat numFormat = NumberFormat.getInstance();
    private TextView playerNameInput;
    private TextView anteAmtInput;
    private TextView winAmtInput;
    private TextView loseAmtInput;
    private Button addPlayerButton;
    private Button doneButton;
    private LinearLayout nameListLayout;
    private ArrayList<String> players = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize_game);

        playerNameInput = (TextView) findViewById(R.id.playerNameInput);
        anteAmtInput = (TextView) findViewById(R.id.anteAmtInput);
        winAmtInput = (TextView) findViewById(R.id.winAmtInput);
        loseAmtInput = (TextView) findViewById(R.id.loseAmtInput);
        addPlayerButton = (Button) findViewById(R.id.addPlayerButton);
        doneButton = (Button) findViewById(R.id.doneButton);
        nameListLayout = (LinearLayout) findViewById(R.id.nameListLayout);

        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView newPlayerView = new TextView(getApplicationContext());

                String playerName = playerNameInput.getText().toString();
                newPlayerView.setText(playerName);
                players.add(playerName);

                nameListLayout.addView(newPlayerView);
                newPlayerView.setOnClickListener(new TextViewListener());

                playerNameInput.setText("");
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double anteAmt = Double.parseDouble(anteAmtInput.getText().toString());
                Double winAmt = Double.parseDouble(winAmtInput.getText().toString());
                Double loseAmt = Double.parseDouble(loseAmtInput.getText().toString());


                Intent goToGame = new Intent(getApplicationContext(), GameActivity.class);
                goToGame.putExtra("players", players);
                goToGame.putExtra("anteAmt", anteAmt);
                goToGame.putExtra("loseAmt", loseAmt);
                goToGame.putExtra("winAmt", winAmt);
                startActivity(goToGame);
                finish();
            }

        });
    }

    private class TextViewListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String playerName = ((TextView) v).getText().toString();

            players.remove(playerName);
            nameListLayout.removeViewInLayout(v);
        }
    }

}