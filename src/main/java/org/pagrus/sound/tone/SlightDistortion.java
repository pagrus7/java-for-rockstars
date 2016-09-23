package org.pagrus.sound.tone;

import org.pagrus.sound.effects.ClippingDistorion;

import java.util.stream.DoubleStream;

public class SlightDistortion implements Tone {

  ClippingDistorion distorion = new ClippingDistorion(0.4, 1, 1);

  @Override
  public DoubleStream with(DoubleStream input) {
    return input.map(distorion::apply);
  }

}