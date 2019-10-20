package curtin.edu.au.mad_assignment.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import java.io.Serializable;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Structure;

public class MapActivity extends AppCompatActivity implements MapFragment.OnMapElementSelectedListener, SelectorFragment.OnStructureSelectedListener, Serializable {


    private GameData gameData;
    private MapFragment mapFg;
    private SelectorFragment selFg;

    Structure selectedStructure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        gameData = GameData.getInstance(MapActivity.this);

        // Adding fragment to activity
        FragmentManager fm = getSupportFragmentManager();

        selFg = (SelectorFragment) fm.findFragmentById(R.id.selectorFrag_container);
        if(selFg == null){
            selFg = SelectorFragment.newInstance();
            fm.beginTransaction().
                    add(R.id.selectorFrag_container,selFg,"SelectorFrag").
                    commit();
        }

        mapFg = (MapFragment) fm.findFragmentById(R.id.mapFrag_container);
        if(mapFg == null){
            mapFg = MapFragment.newInstance();
            fm.beginTransaction().
                    add(R.id.mapFrag_container,mapFg,"MapFrag").
                    commit();
        }
    }

    @Override
    public void onMapElementSelected(MapElement mapElement, int xPos, int yPos) {
        /** GRID CELL EVENTS
         *  Handles interaction with individual cells on the Map Fragment
         *
         *  1. When user tap a cell, it selects the cell and stores a reference
         *    to the MapElement contained in the cell. Retrieve it by call
         *    getSelectedMapElement()
         *
         *  Interactions with other fragments:
         *      * SelectorFragment
         *          Try to build selected fragment on the cell
         *
         *      *  MapElementFragment
         *          This fragment will contain information relating to the selected structure
         *
         **/

        if(selFg.getSelectedStructure() != null)
        {
            Structure structureToBeBuilt = selFg.getSelectedStructure();
            try
            {
                gameData.buildStructure(xPos, yPos, structureToBeBuilt, structureToBeBuilt.getLabel());
            }
            catch(IllegalArgumentException e)
            {
                Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            selFg.deselectStructure();
        }
    }

    @Override
    public void onStructureSelectedListener(Structure structure) {

        /** SELECTOR CELL EVENTS
         *
         *  1. When user tap an item in the list, stores the reference to the structure
         *    contained in the list. Retrieve it by call getSelectedMapElement()
         *
         *  Interactions with other fragments
         *      * StructureDetailsFragment
         *          This fragment contains information about the structure
         *
         */

        if(mapFg.getSelectedMapElement() != null)
        {
            mapFg.deselectMapElement();
        }
    }
}
