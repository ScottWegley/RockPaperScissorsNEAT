package JavaNEAT.Utils;

public final class Constants {
    
    public static final int maxNodes = (int)Math.pow(2,22);

    public static final double C1 = 1, C2 = 1, C3 = 1;

    public static final double survivalPercent = 0.5;
    public static final double distanceThreshold = 4;

    public static final double weightShiftStrength = 0.3;
    public static final double weightRandomStrength = 1;

    public static final double mutateLinkProbability = 0.03;
    public static final double mutateNodeProbability = 0.001;
    public static final double mutateWeightShiftProbability = 0.002;
    public static final double mutateWeightRandomProbability = 0.002;
    public static final double mutateLinkToggleProbability = 0.0;

}
