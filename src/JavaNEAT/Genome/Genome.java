package JavaNEAT.Genome;

import JavaNEAT.Utils.Calculator;
import JavaNEAT.Utils.Constants;
import JavaNEAT.Utils.CustomHashSet;
import JavaNEAT.NEAT.Network;

public class Genome {
    /*
     * Genome is a collection of all Genes. There are Node Genes, representing input, output, and
     * the intersection of inputs and other intersections There are also Connection genes which
     * represent the connection between any genes.
     */
    CustomHashSet<NodeGene> nodes = new CustomHashSet<NodeGene>();
    CustomHashSet<ConnectionGene> connections = new CustomHashSet<ConnectionGene>();

    private Network network;


    public Genome(Network network) {
        this.network = network;
    }



    public CustomHashSet<NodeGene> getNodes() {
        return nodes;
    }

    public CustomHashSet<ConnectionGene> getConnections() {
        return connections;
    }

    public void mutate() {
        if (Constants.mutateLinkProbability > Math.random()) {
            mutateLink();
        }
        if (Constants.mutateNodeProbability > Math.random()) {
            mutateNode();
        }
        if (Constants.mutateWeightShiftProbability > Math.random()) {
            mutateWeightShift();
        }
        if (Constants.mutateWeightRandomProbability > Math.random()) {
            mutateWeightRandom();
        }
        if (Constants.mutateLinkToggleProbability > Math.random()) {
            mutateLinkToggle();
        }
    }

    public void mutateLink() {
        for (int i = 0; i < 100; i++) {
            NodeGene a = nodes.randomElement();
            NodeGene b = nodes.randomElement();

            if (a.getX() == b.getX()) {
                continue;
            }

            ConnectionGene connectionGene;
            if (a.getX() < b.getX()) {
                connectionGene = new ConnectionGene(a, b);
            } else {
                connectionGene = new ConnectionGene(b, a);
            }

            if (connections.containts(connectionGene)) {
                continue;
            }

            connectionGene =
                    network.createConnection(connectionGene.getFrom(), connectionGene.getTo());
            connectionGene.setWeight((Math.random() * 2 - 1) * Constants.weightRandomStrength);

            connections.addSorted(connectionGene);
            return;
        }
    }

    public void mutateNode() {
        ConnectionGene connectionGene = connections.randomElement();
        if (connectionGene == null)
            return;
        NodeGene from = connectionGene.getFrom();
        NodeGene to = connectionGene.getTo();

        int replaceIndex = network.getReplaceIndex(from, to);
        NodeGene middle;
        if (replaceIndex == 0) {
            middle = network.createNode();
            middle.setX((from.getX() + to.getX()) / 2);
            middle.setY((from.getY() + to.getY()) / 2 + Math.random() * 0.1 - 0.05);
        } else {
            middle = network.getNode(replaceIndex);
        }

        ConnectionGene con1 = network.createConnection(from, middle);
        ConnectionGene con2 = network.createConnection(middle, to);

        con1.setWeight(1);
        con2.setWeight(connectionGene.getWeight());
        con2.setEnabled(connectionGene.isEnabled());

        connections.remove(connectionGene);
        connections.add(con1);
        connections.add(con2);
        nodes.add(middle);
    }

    public void mutateWeightShift() {
        ConnectionGene connectionGene = connections.randomElement();
        if (!(connectionGene == null)) {
            connectionGene.setWeight(connectionGene.getWeight()
                    + (Math.random() * 2 - 1) * Constants.weightShiftStrength);
        }
    }

    public void mutateWeightRandom() {
        ConnectionGene connectionGene = connections.randomElement();
        if (!(connectionGene == null)) {
            connectionGene.setWeight((Math.random() * 2 - 1) * Constants.weightRandomStrength);
        }
    }

    public void mutateLinkToggle() {
        ConnectionGene connectionGene = connections.randomElement();
        if (!(connectionGene == null)) {
            connectionGene.setEnabled(!connectionGene.isEnabled());
        }
    }

    public double getDistance(Genome g2) {
        Genome g1 = separateGenomes(this, g2, true);
        g2 = separateGenomes(this, g2, false);

        int indexG1 = 0, indexG2 = 0, disjoints = 0, excess = 0, similar = 0;
        double weightDiff = 0;

        while (indexG1 < g1.getConnections().size() && indexG2 < g2.getConnections().size()) {

            ConnectionGene gene1 = g1.getConnections().get(indexG1);
            ConnectionGene gene2 = g2.getConnections().get(indexG2);

            int in1 = gene1.getInnovation();
            int in2 = gene2.getInnovation();

            if (in1 == in2) {
                // The connections at this index are similar
                indexG1++;
                indexG2++;
                similar++;
                weightDiff += Math.abs(gene1.getWeight() - gene2.getWeight());
            } else if (in1 > in2) {
                // Parent 2 has a disjointed gene
                indexG2++;
                disjoints++;
            } else {
                // Parent 1 has a disjointed gene
                indexG1++;
                disjoints++;
            }

        }

        weightDiff /= Math.max(1, similar);
        excess = g1.getConnections().size() - indexG1;

        double N = Math.max(g1.getConnections().size(), g2.getConnections().size());
        N = (N < 20) ? 1 : N;

        return Constants.C1 * disjoints / N + Constants.C2 * excess / N + Constants.C3 * weightDiff;
    }

    public static double getDistance(Genome genome1, Genome genome2) {
        Genome g1 = separateGenomes(genome1, genome2, true);
        Genome g2 = separateGenomes(genome1, genome2, false);

        int indexG1 = 0, indexG2 = 0, disjoints = 0, excess = 0, similar = 0;
        double weightDiff = 0;

        while (indexG1 < g1.getConnections().size() && indexG2 < g2.getConnections().size()) {

            ConnectionGene gene1 = g1.getConnections().get(indexG1);
            ConnectionGene gene2 = g2.getConnections().get(indexG2);

            int in1 = gene1.getInnovation();
            int in2 = gene2.getInnovation();

            if (in1 == in2) {
                // The connections at this index are similar
                indexG1++;
                indexG2++;
                similar++;
                weightDiff += Math.abs(gene1.getWeight() - gene2.getWeight());
            } else if (in1 > in2) {
                // Parent 2 has a disjointed gene
                indexG2++;
                disjoints++;
            } else {
                // Parent 1 has a disjointed gene
                indexG1++;
                disjoints++;
            }

        }

        weightDiff /= Math.max(1, similar);
        excess = g1.getConnections().size() - indexG1;

        double N = Math.max(g1.getConnections().size(), g2.getConnections().size());
        N = (N < 20) ? 1 : N;

        return Constants.C1 * disjoints / N + Constants.C2 * excess / N + Constants.C3 * weightDiff;
    }

    public static Genome breed(Genome genome1, Genome genome2) {
        int indexG1 = 0;
        int indexG2 = 0;
        Genome g1 = separateGenomes(genome1, genome2, true);
        Genome g2 = separateGenomes(genome1, genome2, false);

        Genome child = g1.getNetwork().newGenome();

        while (indexG1 < g1.getConnections().size() && indexG2 < g2.getConnections().size()) {

            ConnectionGene gene1 = g1.getConnections().get(indexG1);
            ConnectionGene gene2 = g2.getConnections().get(indexG2);

            int in1 = gene1.getInnovation();
            int in2 = gene2.getInnovation();

            if (in1 == in2) {
                // The connections at this index are similar
                // Randomly take the first gene or second
                if (Math.random() > 0.5) {
                    child.getConnections().add(Network.copyConnection(gene1));
                } else {
                    child.getConnections().add(Network.copyConnection(gene2));
                }

                indexG1++;
                indexG2++;
            } else if (in1 > in2) {
                // Parent 2 has a disjointed gene
                indexG2++;
            } else {
                // Parent 1 has a disjointed gene
                // Take only disjointed genes from the bigger parent
                child.getConnections().add(Network.copyConnection(gene1));
                indexG1++;
            }

        }

        // Take excess genes from the bigger parent
        while (indexG1 < g1.getConnections().size()) {
            ConnectionGene gene1 = g1.getConnections().get(indexG1);
            child.getConnections().add(Network.copyConnection(gene1));
            indexG1++;
        }

        // Derive all nodes from connections and store them
        for (ConnectionGene c : child.getConnections().getList()) {
            child.getNodes().add(c.getFrom());
            child.getNodes().add(c.getTo());
        }
        return child;
    }

    /**
     * Returns the genome with the most or least nodes
     * 
     * @param highLow True for the bigger genome, false for the smaller
     */
    public static Genome separateGenomes(Genome g1, Genome g2, Boolean highLow) {
        int highNumG1 = 0;
        int highNumG2 = 0;

        if (g1.getConnections().size() != 0) {
            highNumG1 = g1.getConnections().get(g1.getConnections().size() - 1).getInnovation();
        }
        if (g2.getConnections().size() != 0) {
            highNumG2 = g2.getConnections().get(g2.getConnections().size() - 1).getInnovation();
        }
        // If you want higher, check for higher and return it
        // If you want lower, check for lower and return it
        if (highNumG1 == highNumG2 && highLow)
            return g1;
        if (highNumG1 == highNumG2 && !highLow)
            return g2;
        return (highLow) ? ((highNumG1 < highNumG2) ? g2 : g1)
                : ((highNumG1 < highNumG2) ? g1 : g2);

    }

    public Network getNetwork() {
        return network;
    }
}
