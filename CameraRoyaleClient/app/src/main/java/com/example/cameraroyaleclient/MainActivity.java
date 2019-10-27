package com.example.cameraroyaleclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public List<String> games;
    public static String game;
    public static String playerID;
    private RecyclerView recyclerView;
    private RadioAdapter mAdapter;
    //private Spinner spinner;
    //private ArrayAdapter<Integer> spinnerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ServerCommunicator communicator;

    public static final Integer[] PLAYERCOUNTS = {2,3,4,5,6,7,8,9,10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        communicator = new ServerCommunicator(this);

        games = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            games.add("aaa" + i);
        }

        //setting up recycler of radio buttons with game name strings
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RadioAdapter(games);
        recyclerView.setAdapter(mAdapter);

        //updateGames();

        /*spinner = (Spinner) findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PLAYERCOUNTS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        Log.d("YEET", spinnerAdapter.getItem(0).toString());*/
    }
    public void refresh(View view) {
        updateGames();
    }
    private void updateGames() {
        communicator.getGameList(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject responseobject = ServerCommunicator.getJSON(response);
                Log.d("YEET", responseobject.toString());
                JSONArray jsonArray = null;
                try {
                    jsonArray = responseobject.getJSONArray("gameList");
                    Log.d("YEET", jsonArray.toString());
                } catch (JSONException e) {
                    Log.d("YEET", "Failrue");
                    Log.e("YEET", e.getStackTrace().toString());
                    e.printStackTrace();
                }
                games.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        games.add(jsonArray.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    public void joinGame(View view) {
        //This is the onclick method for the join game button.
        //Log.d("YEET", spinnerAdapter.getItem(0).toString());
        if (mAdapter.getCurrentSelected() != null) {
            game = mAdapter.getCurrentSelected();
            //TODO: UNCOMMENT THIS NEXT LINE WHEN THE BACKEND WORKS
            //playerIDInput();
            //TODO: REMOVE/COMMENT OUT THIS DEBUG BYPASS TO THE GAMEPLAY ACTIVITY
            startGameAct();

        } else {
            makeToast("Please select a game.");
        }
    }
    private void startGameAct() {
        Intent intent = new Intent(this, GameplayActivity.class);
        startActivity(intent);
    }
    public void newGame(View view) {
        //this is the onclick method for the new game button.
        //int number = (Integer) spinner.getSelectedItem();
        gameNameInput();
    }
    public void serverTest(View view) {
        communicator.serverTest(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                makeToast(response);
            }
        });
    }
    private void playerIDInput() {
        /**
         * SOURCE: https://stackoverflow.com/questions/10903754/input-text-dialog-android
         * COPY PASTE REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEee
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your Player ID:");

// Set up the input
        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerID = input.getText().toString();
                dialog.dismiss();
                communicator.joinGame(game, playerID, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject json = ServerCommunicator.getJSON(response);
                        boolean success = false;
                        try {
                            success = json.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (success) {
                            startGameAct();
                        }

                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void gameNameInput() {
        /**
         * SOURCE: https://stackoverflow.com/questions/10903754/input-text-dialog-android
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name your new game:");

// Set up the input
        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(25)});
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        int thiswhich = -111;
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                game = input.getText().toString();
                dialog.dismiss();
                communicator.createNewGame(game, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("YEET", response);
                        updateGames();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void makeToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
