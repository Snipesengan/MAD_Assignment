package curtin.edu.au.mad_assignment.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class MapElement implements Serializable {


    // Because Bitmap is not serializable, and i need it to be for my implementation
    private final boolean buildable;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;

    private ProxyBitmap bitmap;
    private Structure structure;
    private String ownerName;


    public MapElement(boolean buildable, int northWest, int northEast,
                      int southWest, int southEast, Structure structure)
    {
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.ownerName = "NULL";
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Bitmap getImage() {
        return bitmap.getBitmap();
    }

    public boolean isBuildable()
    {
        return buildable;
    }

    public int getNorthWest()
    {
        return terrainNorthWest;
    }

    public int getSouthWest()
    {
        return terrainSouthWest;
    }

    public int getNorthEast()
    {
        return terrainNorthEast;
    }

    public int getSouthEast()
    {
        return terrainSouthEast;
    }



    /**
     * Retrieves the structure built on this map element.
     * @return The structure, or null if one is not present.
     */
    public Structure getStructure()
    {
        return structure;
    }

    public void setStructure(Structure structure,Bitmap bitmap)
    {
        this.structure = structure;
        this.bitmap = new ProxyBitmap(bitmap);
    }

    public void setOwnerName(String ownerName){
        this.ownerName = ownerName;
    }

}
