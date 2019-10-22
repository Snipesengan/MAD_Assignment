package curtin.edu.au.mad_assignment.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Settings;
import curtin.edu.au.mad_assignment.model.Structure;
import curtin.edu.au.mad_assignment.model.StructureData;


public class GameDataCursor extends CursorWrapper {

    // Needs context in order to retrieve the drawables from resources
    private Context context;

    public GameDataCursor(Cursor cursor, Context context) {
        super(cursor);
        this.context = context;
    }

    public int loadMoney(){
        return getInt(getColumnIndex(GameDataSchema.MONEY));
    }

    public int loadGameTime(){
        return getInt(getColumnIndex(GameDataSchema.GAME_TIME));
    }

    public int loadNCommercial() { return getInt(getColumnIndex(GameDataSchema.GAME_N_COMMERCIAL));}

    public int loadNResidential() { return getInt(getColumnIndex(GameDataSchema.GAME_N_RESIDENTIAL));}

    public int loadPopulation() { return getInt(getColumnIndex(GameDataSchema.GAME_POPULATION));}



    /**
     * Loads the setting from the SQL database
     * @return Settings
     */
    public Settings loadSettings(){
        int mapWidth = getInt(getColumnIndex(GameDataSchema.MAP_WIDTH));
        int mapHeight = getInt(getColumnIndex(GameDataSchema.MAP_HEIGHT));
        int initialMoney = getInt(getColumnIndex(GameDataSchema.INITIAL_MONEY));
        int familySize = getInt(getColumnIndex(GameDataSchema.FAMILY_SIZE));
        int shopSize = getInt(getColumnIndex(GameDataSchema.SHOP_SIZE));
        int salary = getInt(getColumnIndex(GameDataSchema.SALARY));
        double taxRate = getDouble(getColumnIndex(GameDataSchema.TAX_RATE));
        int serviceCost = getInt(getColumnIndex(GameDataSchema.SERVICE_COST));
        int commBuildingCost = getInt(getColumnIndex(GameDataSchema.COMM_BUILDING_COST));
        int houseBuildingCost = getInt(getColumnIndex(GameDataSchema.HOUSE_BUILDING_COST));
        int roadBuildingCost = getInt(getColumnIndex(GameDataSchema.ROAD_BUILDING_COST));

        return new Settings(
                mapWidth,
                mapHeight,
                initialMoney,
                familySize,
                shopSize,
                salary,
                taxRate,
                serviceCost,
                houseBuildingCost,
                commBuildingCost,
                roadBuildingCost);
    }

    /**
     * Loads the map element from the SQL database and store it in an array by reference
     * @param mapElements: 2D array to store the loaded mapElement
     */
    public void loadMapElement(MapElement[][] mapElements)
    {
        int xPos = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.X_POSITION));
        int yPos = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.Y_POSITION));
        boolean buildable = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.BUILDABLE)) > 0;
        String ownerName = getString(getColumnIndex(GameDataSchema.MapElementsTable.Cols.OWNER_NAME));
        int imageId = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.STRUCTURE_DRAWABLE_ID));
        int nwImg = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.NW_DRAWABLE_ID));
        int neImg = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.NE_DRAWABLE_ID));
        int swImg = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.SW_DRAWABLE_ID));
        int seImg = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.SE_DRAWABLE_ID));

        mapElements[yPos][xPos] = new MapElement(buildable,nwImg,neImg,swImg,seImg,null,xPos,yPos);

        if(!ownerName.equals("")){
            Structure structure = StructureData.findStructureByID(imageId);
            mapElements[yPos][xPos].setStructure(structure);
            mapElements[yPos][xPos].setOwnerName(ownerName);
        }
    }
}
