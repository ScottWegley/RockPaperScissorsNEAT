package RPS;

import java.util.Random;
import JavaNEAT.NEAT.Client;

public class RPS {

    private Random random;
    private int score;

    public RPS() {
        reset();
    }

    public void play(Client client, int i) {
        for (int q = 0; q < i; q++)
            play(client);
    }

    public void play(Client client) {
        GameMoves trainerMove = getTrainerMove();

        double output[] = client.calculate(this.extractNetworkInput(trainerMove));
        GameMoves clientMove = outputToMove(output);
        if (winner(clientMove, trainerMove) == -1)
            score += ((winner(clientMove, trainerMove) == -1) ? 1 : 0);
    }

    public GameMoves outputToMove(double[] output) {
        if (output[0] > output[1] && output[0] > output[2])
            return GameMoves.ROCK;
        if (output[1] > output[0] && output[1] > output[2])
            return GameMoves.PAPER;
        if (output[2] > output[0] && output[2] > output[1])
            return GameMoves.SCISSORS;
        return null;
    }

    public GameMoves getTrainerMove() {
        switch (random.nextInt(3)) {
            default: // Substitue for case 0
                return GameMoves.ROCK;
            case 1:
                return GameMoves.PAPER;
            case 2:
                return GameMoves.SCISSORS;
        }
    }

    public double[] extractNetworkInput(GameMoves t) {
        double[] out = new double[3];
        out[0] = (t == GameMoves.ROCK) ? 1 : 0;
        out[1] = (t == GameMoves.PAPER) ? 1 : 0;
        out[2] = (t == GameMoves.SCISSORS) ? 1 : 0;
        return out;
    }

    public void reset() {
        random = new Random();
        score = 0;
    }

    public int getScore() {
        return this.score;
    }

    public enum GameMoves {
        ROCK, PAPER, SCISSORS
    }

    /**
     * @return -1 if the first parameter wins, 1 if the second, 0 if tie
     */
    private int winner(GameMoves g1, GameMoves g2) {
        if (g1 == g2) {
            return 0;
        } else if ((g1 == GameMoves.ROCK && g2 == GameMoves.SCISSORS)
                || (g1 == GameMoves.SCISSORS && g2 == GameMoves.PAPER)
                || (g1 == GameMoves.PAPER && g2 == GameMoves.ROCK)) {
            return -1;
        } else {
            return 1;
        }
    }

}
