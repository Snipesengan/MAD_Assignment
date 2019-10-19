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

import java.io.Serializable;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean reset = false;

        // TODO: Initialise Model somewhere else
        GameData gameData = GameData.getInstance();
        gameData.setSettings(new Settings());
        gameData.load(MainActivity.this); // This will initialise the data base


        MapData mapDataInstance = MapData.getInstance();
        mapDataInstance.regenerate();

        for(int x = 0; x < gameData.getSettings().getMapWidth(); x++){
            for(int y = 0; y < gameData.getSettings().getMapHeight(); y++){
                MapElement mapElement = mapDataInstance.get(y,x);
                gameData.addMapElement(x,y,mapElement);
            }
        }

        // For now let's start with the map activity
        startActivity(new Intent(MainActivity.this,MapActivity.class));
    }
}
