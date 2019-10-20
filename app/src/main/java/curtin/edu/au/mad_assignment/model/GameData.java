package curtin.edu.au.mad_assignment.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.util.Map;


import curtin.edu.au.mad_assignment.database.GameDataCursor;
import curtin.edu.au.mad_assignment.database.GameDataDbHelper;
import curtin.edu.au.mad_assignment.database.GameDataSchema;


//Singleton but also handles game logic
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

    public void resetGame()
    {
        deleteDatabases(); // SHOULD BE DONE BEFORE UPDATING DATA BASE
        regenerateMap();

        // Reset Game state
        instance.setGameTime(0);
        instance.setMoney(settings.getInitialMoney());
    }

    /** ------- GAME FUNCTIONALITY METHODS -----**/

    /**
     *
     * Place a structure on the map by modifying the contents of gameElements array
     * @param xPos
     * @param yPos
     * @param structure
     *
     * Assertions:
     *  - Player must be able to afford structure
     *  - Cannot build if there is an existing structure
     *  - Buildings can only be placed next to roads & on buildable terrain
     *  - Owner name must not be empty string because that is reserved for identifying no structure
     *    exists
     */
    public void buildStructure(int xPos, int yPos, Structure structure, String ownerName)
    {
        int cost = getCost(structure);
        boolean buildable = mapElements[yPos][xPos].isBuildable();

        if(money < cost)
        {
            throw new IllegalArgumentException("Not enough money");
        }

        if(!buildable)
        {
            throw new IllegalArgumentException("Unbuildable terrain");
        }

        if(mapElements[yPos][xPos].getStructure() != null)
        {
            throw new IllegalArgumentException("A structure already exists");
        }

        if(!StructureData.isRoad(structure) && !isAdjacentToRoad(xPos,yPos))
        {
            throw new IllegalArgumentException("Must be placed next to a road");
        }

        if(ownerName.equals(""))
        {
            throw new IllegalArgumentException("Owner name must not be an empty string");
        }

        mapElements[yPos][xPos].setStructure(structure);
        mapElements[yPos][xPos].setOwnerName(ownerName);
        money = money - cost;
    }

    /**
     *  Regenerates the map. Overwrite the content of mapElements
     *  WARNING: This does not update the Database
     */
    public void regenerateMap()
    {
        MapData mapData = MapData.getInstance();
        mapData.regenerate();

        for(int x = 0; x < settings.getMapWidth(); x++){
            for(int y = 0; y < settings.getMapHeight(); y++){
                mapElements[y][x] = mapData.get(y,x);
            }
        }
    }

    public int getCost(Structure structure) {
        int cost;
        if(StructureData.isRoad(structure))
        {
            cost = settings.getRoadBuildingCost();
        }
        else if(StructureData.isCommercial(structure))
        {
            cost = settings.getCommBuildingsCost();
        }
        else if(StructureData.isResidential(structure))
        {
            cost = settings.getHouseBuildingCost();
        }
        else
        {
            throw new UnknownStructureException("Cost for structure of type " + structure.getLabel() + " not yet defined!");
        }

        return cost;
    }

    /** ------- CUSTOM EXCEPTIONS ------ **/

    /**
     * Throw this exception whenever the game has not implemented the functionality for this
     * structure.
     */
    public class UnknownStructureException extends RuntimeException
    {
        public UnknownStructureException(String errorMsg)
        {
            super(errorMsg);
        }

    }

    /** ------- DATA BASE METHODS ------- **/

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

    /** --------- GETTERS & SETTERS ----------- **/

    public MapElement getMapElement(int x, int y){
        return mapElements[y][x];
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

    public Context getContext(){
        return context;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    /** --------- PRIVATE METHODS ---------- **/

    private boolean isAdjacentToRoad(int xPos, int yPos)
    {
        boolean isAdjacentToRoad = false;

        if(yPos > 0 && mapElements[yPos - 1][xPos].getStructure() != null && StructureData.isRoad(mapElements[yPos - 1][xPos].getStructure()))
        {
            isAdjacentToRoad = true;
        }
        else if(yPos < (settings.getMapHeight() - 1) && mapElements[yPos + 1][xPos].getStructure() != null && StructureData.isRoad(mapElements[yPos + 1][xPos].getStructure()))
        {
            isAdjacentToRoad = true;
        }
        else if(xPos > 0 && mapElements[yPos][xPos - 1].getStructure() != null && StructureData.isRoad(mapElements[yPos][xPos - 1].getStructure()))
        {
            isAdjacentToRoad = true;
        }
        else if(xPos < (settings.getMapWidth() - 1) && mapElements[yPos][xPos + 1].getStructure() != null && StructureData.isRoad(mapElements[yPos][xPos + 1].getStructure()))
        {
            isAdjacentToRoad = true;
        }

        return isAdjacentToRoad;
    }
}
