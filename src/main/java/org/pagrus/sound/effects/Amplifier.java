package org.pagrus.sound.effects;

public class Amplifier {
  private double amplificationFactor;

  public Amplifier(double amplificationFactor) {
    this.amplificationFactor = amplificationFactor;
  }

  public double apply(double x) {
    return x * amplificationFactor;
  }

}
