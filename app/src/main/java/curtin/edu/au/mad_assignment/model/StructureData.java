package curtin.edu.au.mad_assignment.model;
import android.content.RestrictionEntry;
import android.content.res.Resources;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import curtin.edu.au.mad_assignment.R;


public class StructureData {

    private List<Residential> residentials;
    private List<Commercial> commercials;
    private List<Road> roads;

    public StructureData(){
        residentials = new ArrayList<>();
        commercials  = new ArrayList<>();
        roads        = new ArrayList<>();
    }

    // I want to figure out a OO design structure such that I don't hae to add a new addStructure
    // everytime i create a new structure subclass.

    public void addStructure(Residential r){
        residentials.add(r);
    }

    public void addStructure(Commercial c){
        commercials.add(c);
    }

    public void addStructure(Road r){
        roads.add(r);
    }


    // Methods to return deep copy of the structure lists

    public List<Residential> getResidentials(){
        List<Residential> cloneList = new ArrayList<>();
        for(Residential r : residentials){
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
}
