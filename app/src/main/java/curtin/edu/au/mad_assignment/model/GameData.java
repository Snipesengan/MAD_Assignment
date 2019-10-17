package curtin.edu.au.mad_assignment.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    public GameData() {
        this.settings = new Settings();
        mapElements = new MapElement[settings.getMapHeight()][settings.getMapWidth()];
        money = settings.getInitialMoney();
        gameTime = 0;
    }

    public static GameData getInstance(){
        if(instance == null){
            instance = new GameData();
        }
        return instance;
    }

    // Load GameData from database and StructureData
    public void load(Context context, StructureData structureData) {
        this.db = new GameDataDbHelper(
                context.getApplicationContext()
        ).getWritableDatabase();

        // --------- LOAD SETTINGS ---------------
        GameDataCursor gameDataCursor = new GameDataCursor(
                db.query(GameDataSchema.NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)
        );

        GameDataCursor mapElementCursor = new GameDataCursor(
                db.query(GameDataSchema.MapElementsTable.NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)
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

        try{
            mapElementCursor.moveToFirst();
            while(!mapElementCursor.isAfterLast()){
                mapElementCursor.loadMapElement(mapElements,structureData);
                mapElementCursor.moveToNext();
            }
        } finally {
            mapElementCursor.close();
        }
    }


    public void addMapElement(int x, int y,  MapElement element){
        mapElements[y][x] = element;
        ContentValues cv = new ContentValues();
        cv.put(GameDataSchema.MapElementsTable.Cols.X_POSITION,x);
        cv.put(GameDataSchema.MapElementsTable.Cols.Y_POSITION,y);
        cv.put(GameDataSchema.MapElementsTable.Cols.IMAGE_ID,element.getStructure().getImageId());
        cv.put(GameDataSchema.MapElementsTable.Cols.OWNER_NAME, element.getOwnerName());

    }

    public void updateDb(){}

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
}
