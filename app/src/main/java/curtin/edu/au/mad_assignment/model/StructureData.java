package curtin.edu.au.mad_assignment.model;

import android.speech.RecognizerIntent;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.Commercial;
import curtin.edu.au.mad_assignment.model.Residential;
import curtin.edu.au.mad_assignment.model.Road;
import curtin.edu.au.mad_assignment.model.Structure;

/**
 * Stores the list of possible structures. This has a static get() method for retrieving an
 * instance, rather than calling the constructor directly.
 *
 * The remaining methods -- get(int), size(), add(Structure) and remove(int) -- provide
 * minimalistic list functionality.
 *
 * There is a static int array called DRAWABLES, which stores all the drawable integer references,
 * some of which are not actually used (yet) in a Structure object.
 **/

public class StructureData
{


    private List<Structure> structureList = Arrays.asList(new Structure[]{
            new Commercial(R.drawable.ic_building_commercial1,"Commercial"),
            new Commercial(R.drawable.ic_building_commercial2,"Commercial"),
            new Commercial(R.drawable.ic_building_commercial3,"Commercial"),
            new Commercial(R.drawable.ic_building_commercial4,"Commercial"),
            new Commercial(R.drawable.ic_building_commercial5,"Commercial"),
            new Road(R.drawable.ic_road_ns,"Road"),
            new Road(R.drawable.ic_road_ew,"Road"),
            new Road(R.drawable.ic_road_nsew,"Road"),
            new Road(R.drawable.ic_road_ne,"Road"),
            new Road(R.drawable.ic_road_nw,"Road"),
            new Road(R.drawable.ic_road_se,"Road"),
            new Road(R.drawable.ic_road_sw,"Road"),
            new Road(R.drawable.ic_road_n,"Road"),
            new Road(R.drawable.ic_road_e,"Road"),
            new Road(R.drawable.ic_road_s,"Road"),
            new Road(R.drawable.ic_road_w,"Road"),
            new Road(R.drawable.ic_road_nse,"Road"),
            new Road(R.drawable.ic_road_nsw,"Road"),
            new Road(R.drawable.ic_road_new,"Road"),
            new Road(R.drawable.ic_road_sew,"Road"),
            new Residential(R.drawable.ic_building_residential1,"Residential"),
            new Residential(R.drawable.ic_building_residential2,"Residential"),
            new Residential(R.drawable.ic_building_residential3,"Residential")
    });

    private static StructureData instance = null;

    public static StructureData getInstance()
    {
        if(instance == null)
        {
            instance = new StructureData();
        }
        return instance;
    }

    protected StructureData() {}

    public Structure get(int index)
    {
        return structureList.get(index);
    }

    public int count()
    {
        return structureList.size();
    }

    public Structure findStructureByID(int drawableId)
    {
        for(Structure structure : structureList)
        {
            if(structure.getDrawableId() == drawableId){
                return structure;
            }
        }

        throw new NoSuchElementException("Structure with drawableID: " + drawableId + " DNE!");
    }

}
