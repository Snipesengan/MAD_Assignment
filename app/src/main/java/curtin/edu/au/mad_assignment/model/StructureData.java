package curtin.edu.au.mad_assignment.model;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


//Singleton
public class StructureData implements Serializable {

    private static StructureData instance;

    private List<Residential> residential;
    private List<Commercial> commercials;
    private List<Road> roads;

    public StructureData(){
        residential = new ArrayList<>();
        commercials  = new ArrayList<>();
        roads        = new ArrayList<>();
    }

    public static StructureData getInstance(){
        if(instance == null){
            instance = new StructureData();
        }

        return instance;
    }


    public void addStructure(Residential r){
        residential.add(r);
    }

    public void addStructure(Commercial c){
        commercials.add(c);
    }

    public void addStructure(Road r){
        roads.add(r);
    }


    // Methods to return deep copy of the structure lists

    public List<Residential> getResidential(){
        List<Residential> cloneList = new ArrayList<>();
        for(Residential r : residential){
            cloneList.add(new Residential(r));
        }

        return cloneList;
    }

    public List<Commercial> getCommercials(){
        List<Commercial> cloneList = new ArrayList<>();
        for(Commercial c : commercials){
            cloneList.add(new Commercial(c));
        }

        return cloneList;
    }

    public List<Road> getRoads(){
        List<Road> cloneList = new ArrayList<>();
        for(Road r : roads){
            cloneList.add(new Road(r));
        }

        return cloneList;
    }

    public Structure getStructure(String structureType, int structureId)
    {

        switch(structureType.toUpperCase())
        {
            case "RESIDENTIAL":
                return getResidential(structureId);

            case "COMMERCIAL":
                return getCommercial(structureId);

            case "ROAD":
                return getRoad(structureId);

            default:
                throw new NoSuchElementException("Structure type of " + structureType + " does not exists.");
        }
    }

    public Commercial getCommercial(int structureId)
    {
        for(Commercial commercial : commercials){
            if(commercial.getImageId() == structureId){
                return commercial;
            }
        }

        throw new NoSuchElementException("Commercial Structure with ID " + structureId  + " does not exist.");
    }

    public Road getRoad(int structureId)
    {
        for(Road road : roads){
            if(road.getImageId() == structureId){
                return road;
            }
        }

        throw new NoSuchElementException("Road Structure with ID " + structureId  + " does not exist.");
    }

    public Residential getResidential(int structureId)
    {
        for(Residential residential : residential){
            if(residential.getImageId() == structureId){
                return residential;
            }
        }

        throw new NoSuchElementException("Residential Structure with ID " + structureId  + " does not exist.");
    }
}
