package JavaNEAT.NEAT;

import JavaNEAT.Utils.Constants;
import JavaNEAT.Utils.CustomHashSet;
import java.util.Comparator;
import JavaNEAT.Genome.Genome;

public class Species {
    private CustomHashSet<Client> clients = new CustomHashSet<>();
    private Client representative;
    private double score;

    public Species(Client representative) {
        this.representative = representative;
        this.representative.setSpecies(this);
        clients.add(this.representative);
    }

    public boolean put(Client client) {
        if (client.distance(representative) < Constants.distanceThreshold) {
            client.setSpecies(this);
            clients.add(client);
            return true;
        }
        return false;
    }

    public void forcePut(Client client) {
        client.setSpecies(this);
        clients.add(client);
    }

    public void goExtinct() {
        for (Client c : clients.getList()) {
            c.setSpecies(null);
        }
    }

    public void evaluateScore() {
        double v = 0;
        for (Client c : clients.getList()) {
            v += c.getScore();
        }
        score = v / clients.size();
    }

    public double highsetScore(){
        double score = 0;
        for(Client c:clients.getList()){
            if(c.getScore() > score){
                score = c.getScore();
            }
        }
        return score;
    }

    public Genome highestScore() {
        Client client = clients.get(0);
        for(Client c:clients.getList()){
            if(c.getScore() > client.getScore()){
                client = c;
            }
        }
        return client.getGenome();
    }

    public void reset() {
        representative = clients.randomElement();
        for (Client c : clients.getList()) {
            c.setSpecies(null);
        }
        clients.clear();

        clients.add(representative);
        representative.setSpecies(this);
        score = 0;
    }

    public void kill(double percentage) {
        clients.getList().sort(new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2){
                return Double.compare(o1.getScore(), o2.getScore());
            }
        });
        double amount = percentage * this.clients.size();
        for(int i = 0; i < amount; i++){
            clients.get(0).setSpecies(null);
            clients.remove(0);
        }
    }

    public Genome breed() {
        Client c1 = clients.randomElement();
        Client c2 = clients.randomElement();

        return Genome.breed(c1.getGenome(), c2.getGenome());
    }

    public int size() {
        return clients.size();
    }

    public CustomHashSet<Client> getClients() {
        return clients;
    }

    public Client getRepresentative() {
        return representative;
    }

    public double getScore() {
        return score;
    }
}
