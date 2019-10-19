package curtin.edu.au.mad_assignment.model;

import java.io.Serializable;

public abstract class Structure implements Serializable {

    private int drawableId;
    private String label;

    public Structure(int drawableId,String label)
    {
        this.drawableId = drawableId;
        this.label = label;
    }

    public Structure(Structure structure){
        drawableId = structure.drawableId;
    }

    public int getDrawableId(){
        return drawableId;
    }
    public String getLabel(){ return label; }
}
