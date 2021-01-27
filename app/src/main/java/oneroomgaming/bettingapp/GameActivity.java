package oneroomgaming.bettingapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class GameActivity extends AppCompatActivity {
    private DecimalFormat decFormat = new DecimalFormat("$0.00;$-0.00");
    private EditText potAmount;
    private EditText anteAmountInput;
    private TextView currentAnteAmountLabel;
    private Button anteButton;
    private Button backButton;
    private Button optionButton;
    private Button endGameButton;
    private Button endRoundButton;
    private LinearLayout playerStateLayout;
    private ArrayList<CheckBox> playerBoxes;
    private HashMap<String, Player> playerMap;
    private HashMap<Player, TextView> playerBalances;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        potAmount = (EditText) findViewById(R.id.potAmount);
        //anteAmountInput = (EditText) findViewById(R.id.anteAmountInput);
        currentAnteAmountLabel = (TextView) findViewById(R.id.currentAnteAmtLabel);
        backButton = (Button) findViewById(R.id.backButton);
        anteButton = (Button) findViewById(R.id.anteButton);
        optionButton = (Button) findViewById(R.id.optionButton);
        endRoundButton = (Button) findViewById(R.id.endRoundButton);
        endGameButton = (Button) findViewById(R.id.endGameButton);
        playerStateLayout = (LinearLayout) findViewById(R.id.playerStateLayout);

        playerMap = new HashMap<String, Player>();
        playerBalances = new HashMap<Player, TextView>();

        ArrayList<String> playerNames = getIntent().getStringArrayListExtra("players");
        ArrayList<Player> players = new ArrayList<Player>();

        double anteAmt = getIntent().getDoubleExtra("anteAmt", 0);
        double winAmt = getIntent().getDoubleExtra("winAmt", 0);
        double loseAmt = getIntent().getDoubleExtra("loseAmt", 0);

        game = new Game(players.size(), anteAmt, winAmt, loseAmt);

        //add player to game
        for(String name : playerNames){
            Player p = new Player(name,0);
            players.add(p);
            playerMap.put(name, p);
            game.addPlayer(p);
        }

        //create player rows
        playerBoxes = new ArrayList<CheckBox>();
        LayoutInflater inflater = getLayoutInflater();
        for(Player p : players){
            addRowToLayout(inflater, playerStateLayout, p);
        }

        potAmount.setText(decFormat.format(game.getPot()));
        currentAnteAmountLabel.append(decFormat.format(game.getAnteAmt()));

        anteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(CheckBox box : playerBoxes){
                    if (box.isChecked()){
                        String name = box.getText().toString();
                        Player p = playerMap.get(name);
                        game.antePlayer(p);
                    }
                }

                potAmount.setText(decFormat.format(game.getPot()));
                updateBalances();
            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                LayoutInflater inflater = getLayoutInflater();
                View optionView = inflater.inflate(R.layout.dialog_game_options, null);

                EditText anteView = (EditText) optionView.findViewById(R.id.anteAmtOption);
                EditText loseView = (EditText) optionView.findViewById(R.id.loseAmtOption);
                EditText winView = (EditText) optionView.findViewById(R.id.winAmtOption);

                anteView.setText(decFormat.format(game.getAnteAmt()));
                loseView.setText(decFormat.format(game.getLoseAmt()));
                winView.setText(decFormat.format(game.getWinAmt()));

                builder.setView(optionView);

                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ante = anteView.getText().toString();
                        String win = winView.getText().toString();
                        String lose = loseView.getText().toString();

                        if(ante.startsWith("$")) ante = ante.substring(1);
                        if(win.startsWith("$")) win = win.substring(1);
                        if(lose.startsWith("$")) lose = lose.substring(1);

                        double newAnteAmt = Double.parseDouble(ante);
                        double newWinAmt = Double.parseDouble(win);
                        double newLoseAmt = Double.parseDouble(lose);

                        game.setAnteAmt(newAnteAmt);
                        game.setWinAmt(newWinAmt);
                        game.setLoseAmt(newLoseAmt);

                        String s = "Current Ante Amount: " + decFormat.format(newAnteAmt);
                        currentAnteAmountLabel.setText(s);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                builder.create().show();
            }
        });

        endRoundButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                ArrayList<String> tempList = new ArrayList<>();
                playerMap.forEach((String name, Player player) -> {
                    if(player.isAnted()) tempList.add(name);
                });

                CharSequence[] antedPlayers = new CharSequence[tempList.size()];
                for(int i=0; i<tempList.size(); i++){
                    antedPlayers[i] = tempList.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_select_winner, null);

                LinearLayout layout = dialogView.findViewById(R.id.dialogSelectWinnerLayout);

                playerMap.forEach((String name, Player p) -> {
                    if(p.isAnted()){
                        addRowToWinnerLayout(inflater, layout, p);
                    }
                });

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        game.endRound();
                        resetRound();
                    }
                });


                builder.setView(dialogView);
                builder.create().show();


            }
        });

        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.endGame();
                Intent goToGameOver = new Intent(getApplicationContext(), GameOverActivity.class);
                goToGameOver.putExtra("game", game);
                goToGameOver.putExtra("players", players);
                startActivity(goToGameOver);
                finish();
            }
        });
    }

    public void updateBalances(){
        playerBalances.forEach((Player p, TextView v) -> {
            v.setText(decFormat.format(p.getBalance()));
        });
    }

    public void addRowToLayout(LayoutInflater inflater, LinearLayout layout, Player p){
        View row = inflater.inflate(R.layout.player_info_row, null);

        CheckBox box = row.findViewById(R.id.playnameCheckbox);
        TextView balance = row.findViewById(R.id.playerBalanceView);

        box.setText(p.getName());
        balance.setText(decFormat.format(p.getBalance()));

        playerBoxes.add(box);
        playerBalances.put(p, balance);

        layout.addView(row);
    }

    public void addRowToWinnerLayout(LayoutInflater inflater, LinearLayout layout, Player p){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(layout.getContext(), R.array.round_end_options, R.layout.support_simple_spinner_dropdown_item);

        String playerName = p.getName();
        Double playerBalance = p.getBalance();

        View row = inflater.inflate(R.layout.select_winner_row, null);
        TextView nameView = (TextView) row.findViewById(R.id.nameTextView);
        TextView balanceView = (TextView) row.findViewById(R.id.balanceTextView);
        Spinner spinner = (Spinner) row.findViewById(R.id.winSelectionSpinner);

        nameView.setText(playerName);
        balanceView.setText(decFormat.format(playerBalance));
        spinner.setAdapter(adapter);
        spinner.setSelection(2);

        layout.addView(row);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConstraintLayout par = (ConstraintLayout) parent.getParent();
                String playerName = ((TextView) par.getViewById(R.id.nameTextView)).getText().toString();
                Player p = playerMap.get(playerName);

                if(position == 0) game.setWinner(p);
                else if(position == 1) game.addLoser(p);
                else if(position == 2) game.addSafer(p);

                game.printState();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


    }

    public void resetRound(){
        for(CheckBox box : playerBoxes){
            box.setChecked(false);
        }
        potAmount.setText(decFormat.format(game.getPot()));
        updateBalances();
    }


}