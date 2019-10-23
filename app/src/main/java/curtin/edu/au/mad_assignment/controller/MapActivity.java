package curtin.edu.au.mad_assignment.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Structure;

public class MapActivity extends AppCompatActivity implements
        MapFragment.OnFragmentInteractionListener,
        SelectorFragment.OnFragmentInteractionListener,
        MapElementFragment.OnFragmentInteractionListener {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private GameData gameData;
    private MapFragment mapFg;
    private SelectorFragment selFg;
    private MapElementFragment mapEFragment;
    private ImageButton updateGameButton;
    private Button saveGameButton;
    private Button menuButton;
    private MapElement selectedMapElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Finding UI elements
        gameData = GameData.getInstance();
        updateGameButton = findViewById(R.id.updateGameButton);
        saveGameButton = findViewById(R.id.saveGameButton);
        menuButton = findViewById(R.id.menuButton);

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

                try
                {
                    gameData.update();
                    if(gameData.isGameLost())
                    {
                        findViewById(R.id.gameOverText).setAlpha(1);
                    }
                }
                catch(IllegalArgumentException e){
                    Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                // Updating UI Element/Fragments
                mapFg.updateGameDetail();
            }
        });

        // When pressed, the game will attempt to save the game
        saveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {
                    gameData.dropAllDatabases(MapActivity.this);
                    gameData.save(MapActivity.this);

                }
                catch(IllegalArgumentException e){
                   Toast.makeText(MapActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                finally
                {
                    Toast.makeText(MapActivity.this, "Game successfully saved", Toast.LENGTH_SHORT).show();
                }


            }
        });

        // When pressed, finish returns the the menu
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

        selectedMapElement = mapElement;
        // Try to show MapElementFragment
        if(mapElement.getStructure() != null)
        {
            mapEFragment.updateMapElement(mapElement);
            getSupportFragmentManager().beginTransaction().show(mapEFragment).commit();
        }

        // Try to construct structure on the cell
        if(selFg.getSelectedStructure() != null)
        {
            Structure structureToBeBuilt = selFg.getSelectedStructure();
            try
            {
                gameData.buildStructure(xPos, yPos, structureToBeBuilt, structureToBeBuilt.getLabel());
                mapFg.updateGameDetail();
                mapEFragment.updateMapElement(mapElement);
                getSupportFragmentManager().beginTransaction().show(mapEFragment).commit();
            }
            catch(IllegalArgumentException e)
            {
                Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            selFg.deselectStructure();
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
    }

    @Override
    public void onDemolishButtonPressed(MapElement mapElement) {

        /**
         *  Called when the user taps on the demolish button of MapElementFragment
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
        MapActivity.hideSoftKeyboard(this);
    }

    /**
     * Feature for taking a thumbnail photo, by invoking the camera app.
     * @param mapElement Thumbnail photo saved here
     */
    @Override
    public void onUpdateThumbnail(MapElement mapElement) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selectedMapElement.setBitmap(imageBitmap);
            mapFg.notifyMapElementChanged(selectedMapElement.getxPos(),selectedMapElement.getyPos());
        }
    }


    /** REF: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard **/
    public static void hideSoftKeyboard(Activity activity) {
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
