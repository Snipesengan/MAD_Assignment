package curtin.edu.au.mad_assignment.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;


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
    private int population;
    private int nResidential, nCommercial;
    boolean isGameLost;

    public GameData(Settings settings) {
        this.settings = settings;
        mapElements = new MapElement[settings.getMapHeight()][settings.getMapWidth()];
        money = settings.getInitialMoney();
        gameTime = 0;
        nCommercial = 0;
        nResidential = 0;
        population = 0;
        isGameLost = false;
    }

    public static GameData getInstance(){
        if(instance == null){
            instance = new GameData(new Settings());
        }
        return instance;
    }

    public void resetGame()
    {
        mapElements = new MapElement[settings.getMapHeight()][settings.getMapWidth()];
        money = settings.getInitialMoney();
        gameTime = 0;
        nCommercial = 0;
        nResidential = 0;
        population = 0;
        isGameLost = false;
        regenerateMap();
    }

    public void update()
    {
        if(isGameLost){
            throw new IllegalArgumentException("Game lost");
        }
        gameTime += 1;
        money += getIncome();
        population = nResidential * settings.getFamilySize();

        if(money == 0)
        {
            isGameLost = true;
        }
    }

    // ------- GAME FUNCTIONALITY METHODS -----

    /**
     *
     * Place a structure on the map by modifying the contents of gameElements array.
     * @param xPos y map element
     * @param yPos x position of map element
     * @param structure: structure to be built
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
        int cost = getStructureCost(structure);
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


        if(StructureData.isResidential(structure))
        {
            nResidential ++;
        }
        else if(StructureData.isCommercial(structure))
        {
            nCommercial ++;
        }
        else if(StructureData.isRoad(structure)){}
        else
        {
            throw new UnknownStructureException("Unknown structure of type " + structure + structure.getLabel());
        }
    }


    /**
     *  Demolishes a structure.
     * @param xPos x position
     * @param yPos y position
     *
     * Assertions:
     *  - A structure must exists at mapElement[yPos][xPos]
     *  - Cannot destroy a Road structure when there are adjacent building
     */
    public void demolishStructure(int xPos, int yPos) {
        MapElement me = mapElements[yPos][xPos];
        Structure toBDemo = me.getStructure();
        if (toBDemo == null) {
            throw new IllegalArgumentException("Structure DNE at: " + mapElements[yPos][xPos].getLocationString());
        }

        if (StructureData.isRoad(toBDemo)){
            if(isAdjacentToBuilding(xPos,yPos)){
                throw new IllegalArgumentException("Cannot destroy road while there are adjacent buildings");
            }
        }

        me.setStructure(null);
        me.setOwnerName("");

        if(StructureData.isResidential(toBDemo))
        {
            nResidential --;
        }
        else if(StructureData.isCommercial(toBDemo))
        {
            nCommercial --;
        }
        else if(StructureData.isRoad(toBDemo)){}
        else
        {
            throw new UnknownStructureException("Unknown structure of type " + toBDemo + toBDemo.getLabel());
        }
    }

    /**
     *  Regenerates the map. Overwrite the content of mapElements
     */
    private void regenerateMap()
    {
        MapData mapData = MapData.getInstance();
        mapData.regenerate();

        for(int x = 0; x < settings.getMapWidth(); x++){
            for(int y = 0; y < settings.getMapHeight(); y++){
                mapElements[y][x] = mapData.get(y,x);
            }
        }
    }

    /**
     * Returns cost of a structure. This method couples with StructureData in order to determine the
     * type of structure.
     * @return Cost of the structure
     */
    public int getStructureCost(Structure structure) {

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

    public double getEmploymentRate() // Need to double check
    {
        double rate = 0;

        if(population > 0)
        {
            rate = (double) nCommercial * (double) settings.getShopSize() / (double) population;
        }
        return 1 < rate ? rate : 1;
    }

    public double getIncome()
    {
        return population * (getEmploymentRate() * settings.getSalary() * settings.getTaxRate() - settings.getServiceCost());
    }

    // Exception classes

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

    public void dropAllDatabases(Context context)
    {
        SQLiteDatabase db = new GameDataDbHelper(
                context.getApplicationContext()
        ).getWritableDatabase();

        String dropGameState = "DROP TABLE IF EXISTS "+ GameDataSchema.NAME;
        db.execSQL(dropGameState);
        String dropMapElements = "DROP TABLE IF EXISTS " + GameDataSchema.MapElementsTable.NAME;
        db.execSQL(dropMapElements);
        db.close();
    }

    /**
     * Saves the game.
     * WARNING: This will re-create all the database by explicitly calling SQLiteDBHelper onCreate()
     * May have un-intended consequences?
     */
    public void save(Context context) {

        if(isGameLost){
            throw new IllegalArgumentException("Cannot save a lost game");
        }

        ContentValues cv;
        dropAllDatabases(context);
        GameDataDbHelper dbHelper = new GameDataDbHelper(
                context.getApplicationContext()
        );
        dbHelper.onCreate(dbHelper.getWritableDatabase());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Saving Settings
        cv = new ContentValues();
        cv.put(GameDataSchema.MAP_WIDTH,settings.getMapWidth());
        cv.put(GameDataSchema.MAP_HEIGHT,settings.getMapHeight());
        cv.put(GameDataSchema.INITIAL_MONEY,settings.getInitialMoney());
        cv.put(GameDataSchema.FAMILY_SIZE,settings.getFamilySize());
        cv.put(GameDataSchema.SHOP_SIZE,settings.getShopSize());
        cv.put(GameDataSchema.SALARY,settings.getSalary());
        cv.put(GameDataSchema.TAX_RATE,settings.getTaxRate());
        cv.put(GameDataSchema.SERVICE_COST,settings.getServiceCost());
        cv.put(GameDataSchema.COMM_BUILDING_COST,settings.getCommBuildingsCost());
        cv.put(GameDataSchema.HOUSE_BUILDING_COST,settings.getHouseBuildingCost());
        cv.put(GameDataSchema.ROAD_BUILDING_COST,settings.getRoadBuildingCost());

        // Saving player's progress
        cv.put(GameDataSchema.MONEY,money);
        cv.put(GameDataSchema.GAME_N_COMMERCIAL,nCommercial);
        cv.put(GameDataSchema.GAME_N_RESIDENTIAL,nResidential);
        cv.put(GameDataSchema.GAME_TIME,gameTime);
        cv.put(GameDataSchema.GAME_POPULATION,population);

        db.insert(GameDataSchema.NAME,null,cv);

        // Saving content of MapElement[][]
        for(int x = 0; x < settings.getMapWidth(); x++)
        {
            for(int y = 0; y < settings.getMapHeight(); y++)
            {
                cv = new ContentValues();
                MapElement element = mapElements[y][x];

                cv.put(GameDataSchema.MapElementsTable.Cols.X_POSITION,x);
                cv.put(GameDataSchema.MapElementsTable.Cols.Y_POSITION,y);
                cv.put(GameDataSchema.MapElementsTable.Cols.NE_DRAWABLE_ID,element.getNorthEast());
                cv.put(GameDataSchema.MapElementsTable.Cols.NW_DRAWABLE_ID,element.getNorthWest());
                cv.put(GameDataSchema.MapElementsTable.Cols.SE_DRAWABLE_ID,element.getSouthEast());
                cv.put(GameDataSchema.MapElementsTable.Cols.SW_DRAWABLE_ID,element.getSouthWest());
                cv.put(GameDataSchema.MapElementsTable.Cols.BUILDABLE,element.isBuildable());
                cv.put(GameDataSchema.MapElementsTable.Cols.OWNER_NAME,element.getOwnerName());
                if(!element.getOwnerName().equals(""))
                {
                    cv.put(GameDataSchema.MapElementsTable.Cols.STRUCTURE_DRAWABLE_ID,
                            element.getStructure().getDrawableId());
                }
                db.insert(GameDataSchema.MapElementsTable.NAME,null,cv);
            }
        }
    }

    /**
     * Loads the game
     */
    public void load(Context context) {
        SQLiteDatabase db = new GameDataDbHelper(
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
            this.nResidential = gameDataCursor.loadNResidential();
            this.nCommercial = gameDataCursor.loadNResidential();
            this.population = gameDataCursor.loadPopulation();
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
                mapElementCursor.loadMapElement(mapElements);
                mapElementCursor.moveToNext();
            }
        } finally {
            mapElementCursor.close();
        }
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

    public void setMoney(int money) {
        this.money = money;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int getPopulation() {
        return population;
    }

    public boolean isGameLost(){
        return isGameLost;
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

    private boolean isAdjacentToBuilding(int xPos, int yPos)
    {
        boolean isAdjacentToRoad = false;

        if(yPos > 0 && mapElements[yPos - 1][xPos].getStructure() != null && !StructureData.isRoad(mapElements[yPos - 1][xPos].getStructure()))
        {
            isAdjacentToRoad = true;
        }
        else if(yPos < (settings.getMapHeight() - 1) && mapElements[yPos + 1][xPos].getStructure() != null && !StructureData.isRoad(mapElements[yPos + 1][xPos].getStructure()))
        {
            isAdjacentToRoad = true;
        }
        else if(xPos > 0 && mapElements[yPos][xPos - 1].getStructure() != null && !StructureData.isRoad(mapElements[yPos][xPos - 1].getStructure()))
        {
            isAdjacentToRoad = true;
        }
        else if(xPos < (settings.getMapWidth() - 1) && mapElements[yPos][xPos + 1].getStructure() != null && !StructureData.isRoad(mapElements[yPos][xPos + 1].getStructure()))
        {
            isAdjacentToRoad = true;
        }

        return isAdjacentToRoad;
    }
}
