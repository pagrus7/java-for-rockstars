package org.pagrus.sound.effects;

import static java.lang.Math.PI;

public class Flanger {
  private static final int SAMPLE_RATE = 44100;
  private final int bufferSize;
  final double[] history;
  private final int offsetDelta;
  private final int offsetBase;
  private final double originalSignalLevel;
  private final double flangerSignalLevel;
  private final double oscillationStep;

  int idx;

  public Flanger(double flangerOffsetMillis, double flangerRangeMillis, double freq, double originalSignalLevel, double flangerSignalLevel) {
    this.originalSignalLevel = originalSignalLevel;
    this.flangerSignalLevel = flangerSignalLevel;
    this.offsetBase = (int) (flangerOffsetMillis * SAMPLE_RATE / 1000);
    this.offsetDelta = (int) (flangerRangeMillis * SAMPLE_RATE / 1000);
    this.oscillationStep = (2 * PI * freq) / SAMPLE_RATE;

    bufferSize = offsetBase + offsetDelta;
    history = new double[bufferSize];
    idx = bufferSize - 1;
  }


  public double apply(double x) {
    int historicalIdx = (int) (idx - offsetBase + offsetDelta * Math.sin(idx * oscillationStep));

    x = x * originalSignalLevel + history[historicalIdx % bufferSize] * flangerSignalLevel;
    history[idx++ % bufferSize] = x;
    return x;
  }

}
