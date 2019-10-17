package curtin.edu.au.mad_assignment.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.StructureData;

public class MapActivity extends AppCompatActivity implements Serializable {


    private GameData gameData;
    private StructureData structureData;
    private MapFragment mapFg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        gameData = GameData.getInstance();
        structureData = StructureData.getInstance();

        // Adding fragment to activity
        FragmentManager fm = getSupportFragmentManager();

        // If Fragment is already there
        mapFg = (MapFragment) fm.findFragmentById(R.id.mapFrag_container);
        // Otherwise create the new fragment
        if(mapFg == null){
            mapFg = MapFragment.newInstance(); //better than constructor
            fm.beginTransaction().
                    add(R.id.mapFrag_container,mapFg,"MapFrag").
                    commit();
        }
    }
}
