package curtin.edu.au.mad_assignment.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.Serializable;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Structure;

public class MapActivity extends AppCompatActivity implements
        MapFragment.OnFragmentInteractionListener,
        SelectorFragment.OnFragmentInteractionListener,
        MapElementFragment.OnFragmentInteractionListener {


    private GameData gameData;
    private MapFragment mapFg;
    private SelectorFragment selFg;
    private MapElementFragment mapEFragment;
    private ImageButton updateGameButton;
    private Button saveGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        gameData = GameData.getInstance();
        updateGameButton = findViewById(R.id.updateGameButton);
        saveGameButton = findViewById(R.id.saveGameButton);

        /**
         *  This Activity will contain 3 Fragments
         *      - MapFragment
         *      - SelectorFragment
         *      - MapElementFragment
         *
         * Interactions:
         *      --- See Below ---
         */

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

        mapEFragment = (MapElementFragment) fm.findFragmentById(R.id.mapElementFrag_container);
        if(mapEFragment == null){
            mapEFragment = mapEFragment.newInstance(null);
            fm.beginTransaction().
                    add(R.id.mapElementFrag_container,mapEFragment,"MapElementFrag").
                    commit();
            fm.beginTransaction().hide(mapEFragment).commit();
        }

        /**
         *  Hooking up to UI Elements
         *      UI Elements:
         *          - Update Game Button
         *          - Save Game Button
         */

        // When pressed, the game will attempt to update by 1 time step
        updateGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try //TODO: Handle Exceptions for game updates in MapActivity
                {
                    gameData.update();
                }finally{}

                // Updating UI Element/Fragments
                mapFg.updateGameDetail();
            }
        });

        // When pressed, the game will attempt to save the game
        saveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try //TODO: Handle Exceptions for saving game in MapActivity
                {
                    gameData.dropAllDatabases(MapActivity.this);
                    gameData.save(MapActivity.this);

                }finally
                {
                    Toast.makeText(MapActivity.this, "Game successfully saved", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onMapElementSelected(MapElement mapElement, int xPos, int yPos) {
        /**
         *  Called by the mapFragment when the user taps on a cell
         *
         *  1. User can place a structure on the cell if they have selected it
         *
         *  2. User can see the details about a map element when they click on it
         *
         *  Interactions with other fragments:
         *      * SelectorFragment
         *          Try to build selected fragment on the cell
         *
         *      *  MapElementFragment
         *          Pops up a window (MapElementFragment) that shows details
         *
         **/


        // Try to construct structure on the cell
        if(selFg.getSelectedStructure() != null)
        {
            Structure structureToBeBuilt = selFg.getSelectedStructure();
            try
            {
                gameData.buildStructure(xPos, yPos, structureToBeBuilt, structureToBeBuilt.getLabel());
                mapFg.updateGameDetail();
            }
            catch(IllegalArgumentException e)
            {
                Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            selFg.deselectStructure();

        }// Start Structure Details Fragment if they have selected a structure
        else if(mapElement.getStructure() != null)
        {
            FragmentManager fm = getSupportFragmentManager();
            mapEFragment.updateMapElement(mapElement);
            fm.beginTransaction().show(mapEFragment).commit();
        }

        mapFg.deselectMapElement();
    }

    @Override
    public void onStructureSelectedListener(Structure structure) {

        /**
         *  Called when user taps on a structure in the SelectorFragment
         *
         *  Interactions with other fragments
         *      * StructureDetailsFragment
         *          This fragment contains information about the structure
         *
         */

//        if(mapFg.getSelectedMapElement() != null)
//        {
//            mapFg.deselectMapElement();
//        }
    }

    @Override
    public void onDemolishButtonPressed(MapElement mapElement) {

        /**
         *  Called when the user taps on the demolish button of MapELementFragment
         *
         * 
         *
         */

        try
        {
            gameData.demolishStructure(mapElement.getxPos(), mapElement.getyPos());
            mapFg.updateGameDetail();
            mapFg.notifyMapElementChanged(mapElement.getxPos(),mapElement.getyPos());

            // Hides the details screen after pressing DEMOLISH
            getSupportFragmentManager().beginTransaction().hide(mapEFragment).commit();

        }catch(IllegalArgumentException e)
        {
            Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /** Closing the MapElement Details screen
     *  Just hides the MapElementFragment. Nothing special.
     */
    @Override
    public void onCloseMapElementDetails() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().hide(mapEFragment).commit();
    }

    @Override
    public void onUpdateMapElementName(MapElement me, String name)
    {
        try
        {
            me.setOwnerName(name);
        }catch(IllegalArgumentException e){
            Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            Toast.makeText(MapActivity.this, "Name updated", Toast.LENGTH_SHORT).show();
        }
        MapActivity.hideKeyboard(this);
    }


    /** REF: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard **/
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        //Find the currently focused view, so we can grab the correct window token from it.
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
