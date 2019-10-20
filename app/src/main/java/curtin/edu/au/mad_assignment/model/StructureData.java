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

    public static final String ROAD_LABEL = "ROAD";
    public static final String COMMERCIAL_LABEL = "COMMERCIAL";
    public static final String RESIDENTIAL_LABEL = "RESIDENTIAL";


    private static List<Structure> structureList = Arrays.asList(new Structure[]{
            new Commercial(R.drawable.ic_building_commercial1,COMMERCIAL_LABEL),
            new Commercial(R.drawable.ic_building_commercial2,COMMERCIAL_LABEL),
            new Commercial(R.drawable.ic_building_commercial3,COMMERCIAL_LABEL),
            new Commercial(R.drawable.ic_building_commercial4,COMMERCIAL_LABEL),
            new Commercial(R.drawable.ic_building_commercial5,COMMERCIAL_LABEL),
            new Road(R.drawable.ic_road_ns,ROAD_LABEL),
            new Road(R.drawable.ic_road_ew,ROAD_LABEL),
            new Road(R.drawable.ic_road_nsew,ROAD_LABEL),
            new Road(R.drawable.ic_road_ne,ROAD_LABEL),
            new Road(R.drawable.ic_road_nw,ROAD_LABEL),
            new Road(R.drawable.ic_road_se,ROAD_LABEL),
            new Road(R.drawable.ic_road_sw,ROAD_LABEL),
            new Road(R.drawable.ic_road_n,ROAD_LABEL),
            new Road(R.drawable.ic_road_e,ROAD_LABEL),
            new Road(R.drawable.ic_road_s,ROAD_LABEL),
            new Road(R.drawable.ic_road_w,ROAD_LABEL),
            new Road(R.drawable.ic_road_nse,ROAD_LABEL),
            new Road(R.drawable.ic_road_nsw,ROAD_LABEL),
            new Road(R.drawable.ic_road_new,ROAD_LABEL),
            new Road(R.drawable.ic_road_sew,ROAD_LABEL),
            new Residential(R.drawable.ic_building_residential1,RESIDENTIAL_LABEL),
            new Residential(R.drawable.ic_building_residential2,RESIDENTIAL_LABEL),
            new Residential(R.drawable.ic_building_residential3,RESIDENTIAL_LABEL)
    });

    public static Structure get(int index)
    {
        return structureList.get(index);
    }

    public static int count()
    {
        return structureList.size();
    }

    public static Structure findStructureByID(int drawableId)
    {
        for(Structure structure : structureList)
        {
            if(structure.getDrawableId() == drawableId){
                return structure;
            }
        }

        throw new NoSuchElementException("Structure with drawableID: " + drawableId + " DNE!");
    }

    public static boolean isRoad(Structure structure)
    {
        return structure.getLabel().equals(ROAD_LABEL);
    }

    public static boolean isCommercial(Structure structure)
    {
        return structure.getLabel().equals(COMMERCIAL_LABEL);
    }

    public static boolean isResidential(Structure structure)
    {
        return structure.getLabel().equals(RESIDENTIAL_LABEL);
    }
}
