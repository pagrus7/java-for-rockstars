package org.pagrus.sound.effects;

/**
 * Mixes in samples from the provided double array into input data.
 */
public class SoundMixer {
  private double[] samples;
  private double inputLevel;
  private double mixedLevel;
  private int index;

  public SoundMixer(double inputLevel, double mixedLevel, double[] samples) {
    this.inputLevel = inputLevel;
    this.mixedLevel = mixedLevel;
    this.samples = samples;
  }

  public double mix(double input) {
    double sample = samples[Math.min(index++, samples.length - 1)];
    return input * inputLevel + sample * mixedLevel;
  }
}
