package curtin.edu.au.mad_assignment.model;

import java.io.Serializable;

public class Residential extends Structure implements Serializable {

    public Residential(int imageId, String label)
    {
        super(imageId,label);
    }

    //Copy Constructor
    public Residential(Residential residential){
        super(residential);
    }


}
