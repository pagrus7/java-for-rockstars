package org.pagrus.sound.tone;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.ClippingDistorion;

import java.util.stream.DoubleStream;

public class SlightDistortion implements Tone {

  Amplifier preamp = new Amplifier(7);
  ClippingDistorion distorion = new ClippingDistorion(0.14, 0.05, 1);

  @Override
  public DoubleStream with(DoubleStream input) {
    return input
            .map(preamp::amplify)
            .map(distorion::apply);
  }

}