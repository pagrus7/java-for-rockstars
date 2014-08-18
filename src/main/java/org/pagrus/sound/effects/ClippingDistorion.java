package org.pagrus.sound.effects;

public class ClippingDistorion {
  private final double threshold;
  private final double originalSignalLevel;
  private final double distortedSignalLevel;

  public ClippingDistorion(double threshold, double originalSignalLevel, double distortedSignalLevel) {
    this.threshold = threshold;
    this.originalSignalLevel = originalSignalLevel;
    this.distortedSignalLevel = distortedSignalLevel;
  }

  public double apply(double x) {
    return distortedSignalLevel * Math.signum(x) * (Math.min(Math.abs(x), threshold)) + x * originalSignalLevel;
  }

}
