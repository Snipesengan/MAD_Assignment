package curtin.edu.au.mad_assignment.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MapElement implements Serializable {
    private Structure structure;
    // Because Bitmap is not serializable, and i need it to be for my implementation
    private ProxyBitmap bitmap;
    private String ownerName;

    public MapElement(Structure structure, Bitmap image, String ownerName) {
        this.structure = structure;
        this.bitmap = new ProxyBitmap(image);
        this.ownerName = ownerName;
    }

    public Structure getStructure() {
        return structure;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Bitmap getImage() {
        return bitmap.getBitmap();
    }

}
