package curtin.edu.au.mad_assignment.database;

public class GameDataSchema {

    public static final String NAME = "GameData";
    public static final String MONEY = "game_money";
    public static final String GAME_TIME = "game_time";
    public static final String GAME_POPULATION = "game_population";
    public static final String GAME_N_RESIDENTIAL = "game_n_residential";
    public static final String GAME_N_COMMERCIAL = "game_n_commercial";
    public static final String MAP_WIDTH = "map_width";
    public static final String MAP_HEIGHT = "map_height";
    public static final String INITIAL_MONEY = "initial_money";
    public static final String FAMILY_SIZE = "family_size";
    public static final String SHOP_SIZE = "shop_size";
    public static final String SALARY = "salary";
    public static final String TAX_RATE = "tax_rate";
    public static final String SERVICE_COST = "service_cost";
    public static final String HOUSE_BUILDING_COST = "house_building_cost";
    public static final String COMM_BUILDING_COST = "comm_building_cost";
    public static final String ROAD_BUILDING_COST = "road_building_cost";

    public static class MapElementsTable
    {
        public static final String NAME = "MapElements";
        public static class Cols
        {
            public static final String X_POSITION = "x_position";
            public static final String Y_POSITION = "y_position";
            public static final String BUILDABLE  = "buildable";
            public static final String OWNER_NAME = "owner_name";
            public static final String STRUCTURE_DRAWABLE_ID = "structure_drawable_id";
            public static final String NW_DRAWABLE_ID = "nw_drawable_id";
            public static final String NE_DRAWABLE_ID = "ne_drawable_id";
            public static final String SW_DRAWABLE_ID = "sw_drawable_id";
            public static final String SE_DRAWABLE_ID = "se_drawable_id";
        }
    }
}
