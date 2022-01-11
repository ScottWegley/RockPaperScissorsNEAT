package JavaNEAT.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import JavaNEAT.Genome.ConnectionGene;
import JavaNEAT.Genome.Genome;
import JavaNEAT.Genome.NodeGene;

public class Calculator {

    private ArrayList<Node> inputNodes = new ArrayList<>();
    private ArrayList<Node> hiddenNodes = new ArrayList<>();
    private ArrayList<Node> outputNodes = new ArrayList<>();

    public Calculator(Genome g) {

        CustomHashSet<NodeGene> nodes = g.getNodes();
        CustomHashSet<ConnectionGene> connections = g.getConnections();

        HashMap<Integer, Node> nodeHashMap = new HashMap<>();

        for(NodeGene n:nodes.getList()){
            Node node = new Node(n.getX());
            nodeHashMap.put(n.getInnovation(),node);

            if(n.getX() <= 0.1){
                inputNodes.add(node);
            }else if(n.getX() >= 0.9){
                outputNodes.add(node);
            }else{
                hiddenNodes.add(node);
            }
        }

        hiddenNodes.sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.compareTo(o2);
            }
        });

        for(ConnectionGene c:connections.getList()){
            NodeGene from = c.getFrom();
            NodeGene to = c.getTo();

            Node nodeFrom = nodeHashMap.get(from.getInnovation());
            Node nodeTo = nodeHashMap.get(to.getInnovation());

            Connection con = new Connection(nodeFrom, nodeTo);
            con.setWeight(c.getWeight());
            con.setEnabled(c.isEnabled());

            nodeTo.getConnections().add(con);
        }
    }

    public double[] calculate(double... input){
        if(input.length != inputNodes.size()) throw new RuntimeException("Data doesn't fit");
        for(int i = 0; i < inputNodes.size(); i ++){
            inputNodes.get(i).setOutput(input[i]);
        }
        for(Node n:hiddenNodes){
            n.calculate();
        }
        
        double[] output = new double[outputNodes.size()];
        for(int i = 0; i < outputNodes.size(); i++){
            outputNodes.get(i).calculate();
            output[i] = outputNodes.get(i).getOutput();
        }
        return output;
    }

}
