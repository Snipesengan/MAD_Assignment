package curtin.edu.au.mad_assignment.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.security.InvalidParameterException;

import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Settings;
import curtin.edu.au.mad_assignment.model.Structure;
import curtin.edu.au.mad_assignment.model.StructureData;


public class GameDataCursor extends CursorWrapper {


    public GameDataCursor(Cursor cursor) {
        super(cursor);
    }

    public int loadMoney(){
        return getInt(getColumnIndex(GameDataSchema.MONEY));
    }

    public int loadGameTime(){
        return getInt(getColumnIndex(GameDataSchema.GAME_TIME));
    }

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
    public void loadMapElement(MapElement[][] mapElements,StructureData structureData){

        Structure structure;

        int xPos = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.X_POSITION));
        int yPos = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.Y_POSITION));
        String ownerName = getString(getColumnIndex(GameDataSchema.MapElementsTable.Cols.OWNER_NAME));
        String structureType = getString(getColumnIndex(GameDataSchema.MapElementsTable.Cols.STRUCTURE_TYPE));
        int imageId = getInt(getColumnIndex(GameDataSchema.MapElementsTable.Cols.IMAGE_ID));
        structure = structureData.getStructure(structureType,imageId);

        mapElements[yPos][xPos] = new MapElement(structure,null,ownerName);
    }
}
