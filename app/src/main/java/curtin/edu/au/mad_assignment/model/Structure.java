package curtin.edu.au.mad_assignment.model;

import java.io.Serializable;

public abstract class Structure implements Serializable {

    private int imageId;

    public Structure(int imageId)
    {
        this.imageId = imageId;
    }

    public Structure(Structure structure){
        imageId = structure.imageId;
    }

    public int getImageId(){
        return imageId;
    }
}
