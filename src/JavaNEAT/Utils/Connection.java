package JavaNEAT.Utils;

public class Connection {
    
    private Node fromGene, toGene;

    private double weight;
    private boolean enabled = true;

    public Connection(Node from, Node to) {
        fromGene = from;
        toGene = to;
    }

    public Node getFrom() {
        return fromGene;
    }

    public Node getTo() {
        return toGene;
    }

    public void setFrom(Node from) {
        fromGene = from;
    }

    public void setTo(Node to) {
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

}
