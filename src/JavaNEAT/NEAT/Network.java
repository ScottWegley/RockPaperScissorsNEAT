package JavaNEAT.NEAT;

import java.util.HashMap;
import JavaNEAT.Genome.*;
import JavaNEAT.Utils.Constants;
import JavaNEAT.Utils.CustomHashSet;
import JavaNEAT.Utils.RandomSelector;

public class Network {

    private HashMap<ConnectionGene, ConnectionGene> connectionCollection = new HashMap<>();
    private CustomHashSet<NodeGene> nodeCollection = new CustomHashSet<>();
    private CustomHashSet<Client> clients = new CustomHashSet<>();
    private CustomHashSet<Species> species = new CustomHashSet<>();

    private int inputSize, outputSize, clientSize;

    public int getClientSize() {
        return this.clientSize;
    }

    public int getInputSize() {
        return this.inputSize;
    }

    public int getOutputSize() {
        return this.outputSize;
    }

    public Network(int inputSize, int outputSize, int clientSize) {
        this.reset(inputSize, outputSize, clientSize);
    }

    public Genome newGenome() {
        Genome genome = new Genome(this);
        for (int i = 0; i < inputSize + outputSize; i++) {
            genome.getNodes().add(getNode(i + 1));
        }
        return genome;
    }

    public void evolve() {
        genSpecies();
        kill();
        removeExtinct();
        reproduce();
        mutate();
        for (Client c : clients.getList()) {
            c.createCalculator();
        }
    }

    private void mutate() {
        for (Client c : clients.getList()) {
            c.mutate();
        }
    }

    private void reproduce() {
        RandomSelector<Species> selector = new RandomSelector<>();
        for (Species s : species.getList()) {
            selector.add(s, s.getScore());
        }

        for (Client c : clients.getList()) {
            if (c.getSpecies() == null) {
                Species s = selector.random();
                c.setGenome(s.breed());
                s.forcePut(c);
            }
        }
    }

    private void removeExtinct() {
        for (int i = species.size() - 1; i >= 0; i--) {
            if (species.get(i).size() <= 1) {
                species.get(i).goExtinct();
                species.remove(i);
            }
        }
    }

    private void kill() {
        for (Species s : species.getList()) {
            s.kill(1 - Constants.survivalPercent);
        }
    }

    private void genSpecies() {
        for (Species s : species.getList()) {
            s.reset();
        }

        for (Client c : clients.getList()) {
            if (c.getSpecies() != null)
                continue;

            boolean found = false;

            for (Species s : species.getList()) {
                if (s.put(c)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                species.add(new Species(c));
            }
        }

        for (Species s : species.getList()) {
            s.evaluateScore();
        }
    }

    public static void main(String[] args) {
        Network network = new Network(10, 10, 1000);
        double[] in = new double[10];
        for (int i = 0; i < in.length; i++) {
            in[i] = Math.random();
        }

        for (int i = 0; i < 1000; i++) {
            for (Client c : network.clients.getList()) {
                double score = c.calculate(in)[0];
                c.setScore(score);
            }
            network.evolve();
            System.out.println("#########################################");
            System.out.println("Generation #" + i);
            network.printSpecies();
        }

    }

    public void printSpecies() {
        System.out.println("#########################################");
        for (Species s : this.species.getList()) {
            System.out.println(s + "   " + s.getScore() + "     " + s.size());
        }
    }

    public void printScoreInformation() {
        double avg = 0.0D;
        double min = 1.0E8D;
        double max = 0.0D;
        for (Client c : this.clients.getList()) {
            if (c.getScore() < min)
                min = c.getScore();
            if (c.getScore() > max)
                max = c.getScore();
            avg += c.getScore();
        }
        System.out.println("Average: " + (avg / this.clients.size()));
        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
    }

    public Client getBestClient() {
        int best = 0;
        for (int i = 0; i < this.clients.size(); i++) {
            if (((Client) this.clients.get(i)).getScore() > ((Client) this.clients.get(best))
                    .getScore())
                best = i;
        }
        return (Client) this.clients.get(best);
    }

    public void reset(int inputSize, int outputSize, int clientSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.clientSize = clientSize;

        connectionCollection.clear();
        nodeCollection.clear();
        this.clients.clear();

        for (int i = 0; i < inputSize; i++) {
            NodeGene n = createNode();
            n.setX(0.1);
            n.setY((i + 1) / (double) (inputSize + 1));
        }

        for (int i = 0; i < outputSize; i++) {
            NodeGene n = createNode();
            n.setX(0.9);
            n.setY((i + 1) / (double) (outputSize + 1));
        }

        for (int i = 0; i < clientSize; i++) {
            Client c = new Client();
            c.setGenome(newGenome());
            c.createCalculator();
            this.clients.add(c);
        }
    }

    public void setReplaceIndex(NodeGene node1, NodeGene node2, int index) {
        connectionCollection.get(new ConnectionGene(node1, node2)).setReplaceIndex(index);
    }

    public int getReplaceIndex(NodeGene node1, NodeGene node2) {
        ConnectionGene con = new ConnectionGene(node1, node2);
        ConnectionGene data = connectionCollection.get(con);
        if (data == null)
            return 0;
        return data.getReplaceIndex();
    }

    public Client getClient(int index) {
        return clients.get(index);
    }

    public NodeGene createNode() {
        NodeGene newNode = new NodeGene(nodeCollection.size() + 1);
        nodeCollection.add(newNode);
        return newNode;
    }

    public NodeGene getNode(int id) {
        return (id <= nodeCollection.size()) ? nodeCollection.get(id - 1) : createNode();
    }

    public ConnectionGene createConnection(NodeGene startGene, NodeGene endGene) {
        ConnectionGene newConnection = new ConnectionGene(startGene, endGene);

        if (connectionCollection.containsKey(newConnection)) {
            newConnection.setInnovation(connectionCollection.get(newConnection).getInnovation());
        } else {
            newConnection.setInnovation(connectionCollection.size() + 1);
            connectionCollection.put(newConnection, newConnection);
        }

        return newConnection;
    }

    public static ConnectionGene copyConnection(ConnectionGene toCopy) {
        ConnectionGene duplicate = new ConnectionGene(toCopy.getFrom(), toCopy.getTo());
        duplicate.setInnovation(toCopy.getInnovation());
        duplicate.setWeight(toCopy.getWeight());
        duplicate.setEnabled(toCopy.isEnabled());
        return duplicate;
    }
}
