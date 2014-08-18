package org.pagrus.sound.effects;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.signum;

public class Normalizer {
  private static final int BUFFER_SIZE = 1160;
  private static final double LOWER_THRESHOLD = 0.05;
  private static final double AMPLIFICATION_FACTOR_INCREASE_STEP = 0.002;

  private double desiredAmplitude;

  private int idx;
  private double[] buffer = new double[BUFFER_SIZE];
  private double runnungMax;
  private double amplificationFactor;

  public Normalizer(double desiredAmplitude) {
    this.desiredAmplitude = desiredAmplitude;
  }

  public double apply(double x) {
    updateAmplificationFactor(x);
    return x * amplificationFactor;
  }

  private void updateAmplificationFactor(double x) {
    double absX = abs(x);

    double past = buffer[idx];
    buffer[idx] = absX;
    idx = (idx + 1) % BUFFER_SIZE;

    if (absX > runnungMax) {
      runnungMax = absX;
    } else if (past >= runnungMax) {
      runnungMax = LOWER_THRESHOLD;
      for (double el : buffer) {
        runnungMax = max(runnungMax, el);
      }
    }

    double desiredAmplificationFactor = desiredAmplitude / runnungMax;

    // increase amplification factor by no more than AMPLIFICATION_FACTOR_INCREASE_STEP at a time. 
    // Otherwise there are audible clicks.
    double delta = desiredAmplificationFactor - amplificationFactor;
    if (delta < 0 || abs(delta) <= AMPLIFICATION_FACTOR_INCREASE_STEP) {
      amplificationFactor = desiredAmplificationFactor;
    } else {
      amplificationFactor = amplificationFactor + signum(delta) * AMPLIFICATION_FACTOR_INCREASE_STEP;
    }

  }

}
