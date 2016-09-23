package org.pagrus.sound.tone;

import org.pagrus.sound.effects.Delay;

import java.util.stream.DoubleStream;

public class SlightDistortionWithDelay extends SlightDistortion {

  Delay delay = new Delay(300, 1d, 0.5);

  @Override
  public DoubleStream with(DoubleStream input) {
    return super.with(input).map(delay::apply);
  }

}