package JavaNEAT.Genome;

public class NodeGene extends Gene {

    //Used for Displaying Nodes
    private double x, y;

    public NodeGene(int innovation) {
        super(innovation);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Returns False if o is not a NodeGene
     */
    public boolean equals(Object o) {
        return (o instanceof NodeGene) ? innovation == ((NodeGene) o).getInnovation() : false;
    }

    /**
     * Returns False if parameters are not NodeGenes
     */
    public static boolean isEqaul(Object o1, Object o2) {
        return (o1 instanceof NodeGene && o2 instanceof NodeGene)
                ? ((NodeGene) o1).getInnovation() == ((NodeGene) o2).getInnovation()
                : false;
    }

    public int hashCode() {
        return innovation;
    }
}
