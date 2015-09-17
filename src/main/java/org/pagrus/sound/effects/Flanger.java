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
    // Here. Calculate the historical index, or "how far back the sound to mix with should be picked".

    // Flanger is similar to delay, but the amount of delay is variable: 
    // - there is a fixed component, "offsetBase"
    // - and a variable component, which fluctuates up and down between [-offsetDelta, offsetDelta] with a frequency "freq"
    // the "oscillationStep" is a conveniently precalculated value: 
    // it indicates how much the sinus argument needs to change for each new sample

    int historicalIdx = (int) (0);  // TODO

    x = x * originalSignalLevel + history[historicalIdx % bufferSize] * flangerSignalLevel;
    history[idx++ % bufferSize] = x;
    return x;
  }

}
