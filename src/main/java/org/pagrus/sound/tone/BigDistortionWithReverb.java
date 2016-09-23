package org.pagrus.sound.tone;

import org.pagrus.sound.effects.Delay;

import java.util.stream.DoubleStream;

public class BigDistortionWithReverb extends BigDistortion {

  Delay delay = new Delay(300, 1d, 0.5);
  Delay secondWave = new Delay(600, 1d, 0.2);

  @Override
  public DoubleStream with(DoubleStream input) {
    return super.with(input).map(delay::apply).map(secondWave::apply);
  }

}