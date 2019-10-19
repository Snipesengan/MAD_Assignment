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
    private SelectorFragment selFg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        gameData = GameData.getInstance();
        structureData = StructureData.getInstance();

        // Adding fragment to activity
        FragmentManager fm = getSupportFragmentManager();

        mapFg = (MapFragment) fm.findFragmentById(R.id.mapFrag_container);
        if(mapFg == null){
            mapFg = MapFragment.newInstance(); //better than constructor
            fm.beginTransaction().
                    add(R.id.mapFrag_container,mapFg,"MapFrag").
                    commit();
        }

        selFg = (SelectorFragment) fm.findFragmentById(R.id.selectorFrag_container);
        if(selFg == null){
            selFg = SelectorFragment.newInstance(); //better than constructor
            fm.beginTransaction().
                    add(R.id.selectorFrag_container,selFg,"SelectorFrag").
                    commit();
        }

    }
}
