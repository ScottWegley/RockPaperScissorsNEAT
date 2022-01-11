package JavaNEAT.Genome;

import JavaNEAT.Utils.Constants;

public class ConnectionGene extends Gene {

    private NodeGene fromGene, toGene;

    private double weight;
    private boolean enabled = true;

    private int replaceIndex;

    public ConnectionGene(NodeGene from, NodeGene to) {
        fromGene = from;
        toGene = to;
    }

    public NodeGene getFrom() {
        return fromGene;
    }

    public void setReplaceIndex(int replaceIndex) {
        this.replaceIndex = replaceIndex;
    }

    public int getReplaceIndex() {
        return this.replaceIndex;
    }

    public NodeGene getTo() {
        return toGene;
    }

    public void setFrom(NodeGene from) {
        fromGene = from;
    }

    public void setTo(NodeGene to) {
        toGene = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Will return False if o is not a ConnectionGene
     */
    public boolean equals(Object o) {
        return (o instanceof ConnectionGene)
                ? (this.fromGene == ((ConnectionGene) o).getFrom()
                        && this.toGene == ((ConnectionGene) o).getTo())
                : false;
    }

    /**
     * Will return False if paramters are not ConnectionGenes
     */
    public static boolean isEqual(Object o1, Object o2) {
        return (o1 instanceof ConnectionGene && o2 instanceof ConnectionGene)
                ? (((ConnectionGene) o1).getFrom() == ((ConnectionGene) o2).getFrom()
                        && ((ConnectionGene) o1).getTo() == ((ConnectionGene) o2).getTo())
                : false;
    }

    public int hashCode() {
        return fromGene.getInnovation() * Constants.maxNodes + toGene.getInnovation();
    }
}
