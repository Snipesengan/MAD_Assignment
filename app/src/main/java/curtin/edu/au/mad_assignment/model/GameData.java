package curtin.edu.au.mad_assignment.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;


import curtin.edu.au.mad_assignment.database.GameDataCursor;
import curtin.edu.au.mad_assignment.database.GameDataDbHelper;
import curtin.edu.au.mad_assignment.database.GameDataSchema;

//Singleton
public class  GameData implements Serializable {

    private static GameData instance = null;
    private Settings settings;
    private MapElement[][] mapElements;
    private int money;
    private int gameTime;
    private SQLiteDatabase db;
    private Context context; //Useful for getting resources

    public GameData(Context context, Settings settings) {
        this.settings = settings;
        mapElements = new MapElement[settings.getMapHeight()][settings.getMapWidth()];
        money = settings.getInitialMoney();
        gameTime = 0;
        this.context = context;

        this.db = new GameDataDbHelper(
                context.getApplicationContext()
        ).getWritableDatabase();
    }

    public static GameData getInstance(Context context){
        if(instance == null){
            instance = new GameData(context, new Settings());
        }
        return instance;
    }

    public GameData resetGame(Settings settings)
    {
        deleteDatabases();
        instance = new GameData(context, settings);
        return instance;
    }


    // Load GameData from database and StructureData
    public void load(Context context) {
        this.db = new GameDataDbHelper(
                context.getApplicationContext()
        ).getWritableDatabase();

        // --------- LOAD SETTINGS & GAME STATE ---------------
        GameDataCursor gameDataCursor = new GameDataCursor(
                db.query(GameDataSchema.NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null),
                context
        );

        try {
            gameDataCursor.moveToFirst();
            this.settings = gameDataCursor.loadSettings();
            this.money = gameDataCursor.loadMoney();
            this.gameTime = gameDataCursor.loadGameTime();
            this.mapElements = new MapElement[settings.getMapHeight()][settings.getMapWidth()];
        } finally {
            gameDataCursor.close();
        }

        // -------- LOAD MAP ELEMENTS -------------------------
        GameDataCursor mapElementCursor = new GameDataCursor(
                db.query(GameDataSchema.MapElementsTable.NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null),
                context
        );

        try{
            mapElementCursor.moveToFirst();
            while(!mapElementCursor.isAfterLast()){
                mapElementCursor.loadMapElement(mapElements,context);
                mapElementCursor.moveToNext();
            }
        } finally {
            mapElementCursor.close();
        }
    }

    public void deleteDatabases()
    {
        String dropGameState = "DELETE FROM "+ GameDataSchema.NAME;
        db.execSQL(dropGameState);
        String dropMapElements = "DELETE FROM " + GameDataSchema.MapElementsTable.NAME;
        db.execSQL(dropMapElements);
    }

    /**
     * Populate MapElement[][] at position (x,y) with an instance of MapElement
     * @param x: x position
     * @param y: y position
     * @param element: map element to be added
     *
     * Add to the SQL database whenever an element is added.
     * SHOULD ONLY BE USED TO CREATE WHEN A NEW DATA BASE IS CREATE BECAUSE THIS WILL APPEND
     * TO THE DATABASE NOT UPDATE IT
     */
    public void addMapElement(int x, int y,  MapElement element){

        if(element == null){
            throw new IllegalArgumentException("Element must not be null");
        }

        mapElements[y][x] = element;

        //Update the database
        ContentValues cv = new ContentValues();
        cv.put(GameDataSchema.MapElementsTable.Cols.X_POSITION,x);
        cv.put(GameDataSchema.MapElementsTable.Cols.Y_POSITION,y);
        cv.put(GameDataSchema.MapElementsTable.Cols.NE_DRAWABLE_ID,element.getNorthEast());
        cv.put(GameDataSchema.MapElementsTable.Cols.NW_DRAWABLE_ID,element.getNorthWest());
        cv.put(GameDataSchema.MapElementsTable.Cols.SE_DRAWABLE_ID,element.getSouthEast());
        cv.put(GameDataSchema.MapElementsTable.Cols.SW_DRAWABLE_ID,element.getSouthWest());
        cv.put(GameDataSchema.MapElementsTable.Cols.OWNER_NAME,
                element.getOwnerName());
        if(element.getStructure() != null)
        {
            cv.put(GameDataSchema.MapElementsTable.Cols.STRUCTURE_DRAWABLE_ID,
                    element.getStructure().getDrawableId());
        }
        db.insert(GameDataSchema.MapElementsTable.NAME, null, cv);
    }

    public MapElement getMapElement(int x, int y){
        return mapElements[y][x];
    }

    public MapElement[][] getMapElements(){
        return mapElements;
    }

    public Settings getSettings() {
        return settings;
    }

    public int getMoney() {
        return money;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Bitmap getBitMapResources(int drawableId)
    {
        return BitmapFactory.decodeResource(context.getResources(),drawableId);
    }
}
