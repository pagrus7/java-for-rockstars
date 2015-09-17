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
    if (index >= samples.length) {
      return input;
    }
    return input * inputLevel + samples[index++] * mixedLevel;
  }
}