package curtin.edu.au.mad_assignment.model;

import java.io.Serializable;

public class Commercial extends Structure implements Serializable {

    public Commercial(int imageId) {

        super(imageId);
    }

    //Copy Constructor
    public Commercial(Commercial commercial){
        super(commercial);
    }
}
