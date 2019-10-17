package curtin.edu.au.mad_assignment.model;

import java.io.Serializable;

public class Settings implements Serializable {


    public static final int DEFAULT_MAP_WIDTH = 50;
    public static final int DEFAULT_MAP_HEIGHT = 10;
    public static final int DEFAULT_INITIAL_MONEY = 1000;
    public static final int DEFAULT_FAMILY_SIZE = 4;
    public static final int DEFAULT_SHOP_SIZE = 6;
    public static final int DEFAULT_SALARY = 10;
    public static final double DEFAULT_TAX_RATE = 0.3;
    public static final int DEFAULT_SERVICE_COST = 2;
    public static final int DEFAULT_HOUSE_BUILDING_COST = 100;
    public static final int DEFAULT_COMM_BUILDING_COST = 500;
    public static final int DEFAULT_ROAD_BUILDING_COST = 20;


    private int mapWidth,mapHeight,initialMoney,familySize,shopSize,salary;
    private double taxRate;
    private int serviceCost, houseBuildingCost,commBuildingsCost,roadBuildingCost;


    public Settings()
    {
        this(DEFAULT_MAP_WIDTH,DEFAULT_MAP_HEIGHT,DEFAULT_INITIAL_MONEY,DEFAULT_FAMILY_SIZE,
                DEFAULT_SHOP_SIZE,DEFAULT_SALARY,DEFAULT_TAX_RATE,DEFAULT_SERVICE_COST,
                DEFAULT_HOUSE_BUILDING_COST,DEFAULT_COMM_BUILDING_COST,DEFAULT_ROAD_BUILDING_COST);
    }

    public Settings(int mapWidth, int mapHeight, int initialMoney, int familySize, int shopSize,
                    int salary, double taxRate, int serviceCost, int houseBuildingCost,
                    int commBuildingsCost, int roadBuildingCost) {
        setMapWidth(mapWidth);
        setMapHeight(mapHeight);
        setInitialMoney(initialMoney);
        setFamilySize(familySize);
        setShopSize(shopSize);
        setSalary(salary);
        setTaxRate(taxRate);
        setServiceCost(serviceCost);
        setHouseBuildingCost(houseBuildingCost);
        setCommBuildingsCost(commBuildingsCost);
        setRoadBuildingCost(roadBuildingCost);
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public void setInitialMoney(int initialMoney) {
        this.initialMoney = initialMoney;
    }

    public int getFamilySize() {
        return familySize;
    }

    public void setFamilySize(int familySize) {
        this.familySize = familySize;
    }

    public int getShopSize() {
        return shopSize;
    }

    public void setShopSize(int shopSize) {
        this.shopSize = shopSize;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(int serviceCost) {
        this.serviceCost = serviceCost;
    }

    public int getHouseBuildingCost() {
        return houseBuildingCost;
    }

    public void setHouseBuildingCost(int houseBuildingCost) {
        this.houseBuildingCost = houseBuildingCost;
    }

    public int getCommBuildingsCost() {
        return commBuildingsCost;
    }

    public void setCommBuildingsCost(int commBuildingsCost) {
        this.commBuildingsCost = commBuildingsCost;
    }

    public int getRoadBuildingCost() {
        return roadBuildingCost;
    }

    public void setRoadBuildingCost(int roadBuildingCost) {
        this.roadBuildingCost = roadBuildingCost;
    }
}
