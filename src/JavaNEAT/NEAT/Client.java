package JavaNEAT.NEAT;

import JavaNEAT.Genome.Genome;
import JavaNEAT.Utils.Calculator;

public class Client {

    private Calculator calculator;

    private Genome genome;
    private double score;
    private Species species;

    public void createCalculator() {
        this.calculator = new Calculator(genome);
    }

    public double[] calculate(double... args) {
        if (this.calculator == null)
            createCalculator();
        return this.calculator.calculate(args);
    }

    public double distance(Client other) {
        return this.getGenome().getDistance(other.getGenome());
    }

    public void mutate() {
        getGenome().mutate();
    }

    public Calculator getCalculator() {
        return this.calculator;
    }

    public Genome getGenome() {
        return genome;
    }

    public double getScore() {
        return this.score;
    }

    public Species getSpecies() {
        return this.species;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }
}
