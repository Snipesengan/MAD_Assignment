package curtin.edu.au.mad_assignment.model;

import android.graphics.Bitmap;

public class MapElement {
    private Structure structure;
    private Bitmap image;
    private String ownerName;

    public MapElement(Structure structure, Bitmap image, String ownerName) {
        this.structure = structure;
        this.image = image;
        this.ownerName = ownerName;
    }

    public Structure getStructure() {
        return structure;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getOwnerName() {
        return ownerName;
    }

}
