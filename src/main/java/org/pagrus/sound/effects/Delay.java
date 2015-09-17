package org.pagrus.sound.effects;

public class Delay {
  private static final int SAMPLE_RATE = 44100;
  private final int bufferSize;
  private final double[] history;
  private final double originalSignalLevel;
  private final double delayedSignalLevel;
  private int idx;

  public Delay(int milliseconds, double originalSignalLevel, double delayedSignalLevel) {
    bufferSize = milliseconds * SAMPLE_RATE / 1000;
    history = new double[bufferSize];

    this.originalSignalLevel = originalSignalLevel;
    this.delayedSignalLevel = delayedSignalLevel;
  }

  public double apply(double x) {
    // TODO - delay needs a "delayed" sound mixed with the original sound.
    // originalSignalLevel and delayedSignalLevel are the multipliers to consider in the mix
    return x;
  }
}
