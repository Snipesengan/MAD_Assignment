package curtin.edu.au.mad_assignment.model;

import java.io.Serializable;

public class Road extends Structure implements Serializable {

    public Road(int imageId,String label) {

        super(imageId,label);
    }

    // Copy Constructor
    public Road(Road road){
        super(road);
    }

}
