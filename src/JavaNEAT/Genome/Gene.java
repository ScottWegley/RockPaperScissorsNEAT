package JavaNEAT.Genome;

public class Gene {
    //Unique Identifier within the Gene Types
    protected int innovation;

    public Gene(int innovation) {
        this.innovation = innovation;
    }

    public Gene(){
        
    }

    public int getInnovation(){
        return innovation;
    }

    public void setInnovation(int innovation){
        this.innovation = innovation;
    }


}
