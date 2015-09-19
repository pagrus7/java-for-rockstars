package org.pagrus.sound.effects;

public class ClippingDistorion {
  private final double threshold;
  private final double originalLevel;
  private final double distortedLevel;

  public ClippingDistorion(double threshold, double originalSignalLevel, double distortedSignalLevel) {
    this.threshold = threshold;
    this.originalLevel = originalSignalLevel;
    this.distortedLevel = distortedSignalLevel;
  }

  public double apply(double x) {
    return distortedLevel * Math.signum(x) * (Math.min(Math.abs(x), threshold)) + x * originalLevel;
  }

}
