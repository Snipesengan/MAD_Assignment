package curtin.edu.au.mad_assignment.model;

import java.io.Serializable;

import static androidx.core.graphics.drawable.IconCompat.getResources;

public abstract class Structure implements Serializable {

    private int imageId;

    public Structure(int imageId)
    {
        this.imageId = imageId;
    }

    public Structure(Structure structure){
        imageId = structure.imageId;
    }
}
