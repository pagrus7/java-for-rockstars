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
    x = x * originalSignalLevel + history[idx % bufferSize] * delayedSignalLevel;
    history[idx++ % bufferSize] = x;
    return x;
  }
}
