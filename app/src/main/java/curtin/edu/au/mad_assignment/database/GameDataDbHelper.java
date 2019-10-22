package curtin.edu.au.mad_assignment.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDataDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 3;
    private static final String DATABASE_NAME = "GameData.db";

    public GameDataDbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create Table for GameData
        sqLiteDatabase.execSQL("CREATE TABLE " + GameDataSchema.NAME + " (" +
                GameDataSchema.GAME_TIME + " INTEGER, "+
                GameDataSchema.MONEY + " INTEGER, " +
                GameDataSchema.GAME_POPULATION + " INTEGER, " +
                GameDataSchema.GAME_N_COMMERCIAL + " INTEGER, " +
                GameDataSchema.GAME_N_RESIDENTIAL + " INTEGER, " +
                GameDataSchema.MAP_WIDTH + " INTEGER, " +
                GameDataSchema.MAP_HEIGHT + " INTEGER, " +
                GameDataSchema.INITIAL_MONEY + " INTEGER, " +
                GameDataSchema.FAMILY_SIZE + " INTEGER, " +
                GameDataSchema.SHOP_SIZE + " INTEGER, " +
                GameDataSchema.SALARY + " INTEGER, " +
                GameDataSchema.TAX_RATE + " REAL, " +
                GameDataSchema.SERVICE_COST + " INTEGER, " +
                GameDataSchema.HOUSE_BUILDING_COST + " INTEGER, " +
                GameDataSchema.COMM_BUILDING_COST + " INTEGER, " +
                GameDataSchema.ROAD_BUILDING_COST + " INTEGER" + ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + GameDataSchema.MapElementsTable.NAME + " (" +
                GameDataSchema.MapElementsTable.Cols.X_POSITION + " INTEGER, "  +
                GameDataSchema.MapElementsTable.Cols.Y_POSITION + " INTEGER, "  +
                GameDataSchema.MapElementsTable.Cols.OWNER_NAME + " TEXT, " +
                GameDataSchema.MapElementsTable.Cols.BUILDABLE + " INTEGER, " +
                GameDataSchema.MapElementsTable.Cols.STRUCTURE_DRAWABLE_ID + " INTEGER, " +
                GameDataSchema.MapElementsTable.Cols.NW_DRAWABLE_ID + " INTEGER, " +
                GameDataSchema.MapElementsTable.Cols.NE_DRAWABLE_ID + " INTEGER, " +
                GameDataSchema.MapElementsTable.Cols.SW_DRAWABLE_ID + " INTEGER, " +
                GameDataSchema.MapElementsTable.Cols.SE_DRAWABLE_ID + " INTEGER" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
