package RPS;

import JavaNEAT.Genome.Genome;
import JavaNEAT.NEAT.Client;
import JavaNEAT.NEAT.Network;
import JavaNEAT.Utils.Calculator;
import JavaNEAT.Visuals.Frame;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.Scanner;

public class RPSEvolver {


    public static void main(String[] args) {
        RPS game = new RPS();
        Scanner pauser = new Scanner(System.in);

        Network network = new Network(3, 3, 100);

        for (int i = 0; i < network.getClientSize(); i++) {
            determineFitness(game, network.getClient(i));
        }

        for (int i = 0; i < 1000; i++) {
            System.out.println("#################### " + i + " ######################");
            evolve(game, network);
            network.printSpecies();
            System.out.println(network.getBestClient().getScore());
            network.printScoreInformation();
        }

        for (int i = 0; i < network.getClientSize(); i++) {
            determineFitness(game, network.getClient(i));
        }

        new Frame(network.getBestClient().getGenome());
        pauser.nextLine();

        Genome bestGenome = network.getBestClient().getGenome();
        Calculator c = new Calculator(bestGenome);
        for(int i = 0; i <3; i++){
        RPS.GameMoves trainerMove = game.getTrainerMove();
        RPS.GameMoves clientMove = game.outputToMove(c.calculate(game.extractNetworkInput(trainerMove)));
        JOptionPane.showMessageDialog(new JFrame(), "Client: " + clientMove + " ######### Trainer: " + trainerMove);}
    }

    private static void evolve(RPS game, Network network) {
        for (int i = 0; i < network.getClientSize(); i++) {
            determineFitness(game, network.getClient(i));
        }
        network.evolve();
    }

    private static void determineFitness(RPS game, Client client) {
        game.reset();
        game.play(client);
        client.setScore(game.getScore());
    }
}
