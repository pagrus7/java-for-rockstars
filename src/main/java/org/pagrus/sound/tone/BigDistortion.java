package org.pagrus.sound.tone;

import org.pagrus.sound.effects.ClippingDistorion;
import org.pagrus.sound.effects.Delay;

import java.util.stream.DoubleStream;

public class BigDistortion extends SlightDistortion {

  ClippingDistorion secondDistortion = new ClippingDistorion(0.4, 1, 1.2);

  @Override
  public DoubleStream with(DoubleStream input) {
    return super.with(input).map(secondDistortion::apply);
  }

}