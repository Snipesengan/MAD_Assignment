package curtin.edu.au.mad_assignment.model;

import java.io.Serializable;
import java.util.Set;

public class GameData implements Serializable {

    private Settings settings;
    private MapElement[][] mapElements;
    private int money;
    private int gameTime;

    public GameData(Settings settings){
        this.settings = settings;
        mapElements = new MapElement[settings.getMapHeight()][settings.getMapWidth()];
        money = settings.getInitialMoney();
        gameTime = 0;
    }

    public void addMapElement(int x, int y,  MapElement element){
        mapElements[y][x] = element;
    }

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
}
