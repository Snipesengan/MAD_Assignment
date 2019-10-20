package curtin.edu.au.mad_assignment.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.Set;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.Commercial;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapData;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Residential;
import curtin.edu.au.mad_assignment.model.Road;
import curtin.edu.au.mad_assignment.model.Settings;
import curtin.edu.au.mad_assignment.model.Structure;
import curtin.edu.au.mad_assignment.model.StructureData;

public class MainActivity extends AppCompatActivity implements Serializable {

    private static final int REQUEST_CODE_SETTINGS =  0;

    private Settings settings;
    GameData gameData;
    MapData mapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button newGameButton = findViewById(R.id.newGameButton);
        Button loadGameButton = findViewById(R.id.loadGameButton);
        GameData gamedataInstance = null;

        //Setting up event handlers
        newGameButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                //TODO: New Game Event

                settings  = new Settings();
                Intent settingsIntent = SettingsActivity.getIntent(MainActivity.this, settings);
                // Start SettingsActivity for result
                startActivityForResult(settingsIntent,REQUEST_CODE_SETTINGS);
                // See onActivityForResult to see what happens next
            }

        });

        loadGameButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                //TODO: Load Game Event
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData)
    {
        super.onActivityResult(requestCode, resultCode, returnData);

        // START GAME FROM SETTINGS SCREEN
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_SETTINGS)
        {
            settings = SettingsActivity.getSettings(returnData);
            gameData = GameData.getInstance(MainActivity.this);
            gameData.setSettings(settings);
            gameData.resetGame();

            startActivity(new Intent(MainActivity.this, MapActivity.class));
            finish();
        } // RETURN TO MENU FROM SETTINGS SCREEN
        else if(resultCode != RESULT_OK && requestCode == REQUEST_CODE_SETTINGS)
        {

        }
    }

}
