package com.example.cameraroyaleclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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

        /*spinner = (Spinner) findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PLAYERCOUNTS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        Log.d("YEET", spinnerAdapter.getItem(0).toString());*/
    }
    public void joinGame(View view) {
        //This is the onclick method for the join game button.
        //Log.d("YEET", spinnerAdapter.getItem(0).toString());
        if (mAdapter.getCurrentSelected() != null) {
            //TODO: THIIIIIIIINGS
            playerIDInput();

        } else {
            makeToast("Please select a device.");
        }
    }
    public void newGame(View view) {
        //this is the onclick method for the new game button.
        //int number = (Integer) spinner.getSelectedItem();
        gameNameInput();
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
                game = input.getText().toString();
                dialog.dismiss();
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
                playerID = input.getText().toString();
                dialog.dismiss();
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
