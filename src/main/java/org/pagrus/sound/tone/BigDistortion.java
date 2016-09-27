package org.pagrus.sound.tone;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.ClippingDistorion;
import org.pagrus.sound.effects.Delay;

import java.util.stream.DoubleStream;

public class BigDistortion implements Tone{

  Amplifier preamp = new Amplifier(7);
  ClippingDistorion distorion = new ClippingDistorion(0.05, 0.05, 1);
  Amplifier postamp = new Amplifier(4);

  @Override
  public DoubleStream with(DoubleStream input) {
    return input
            .map(preamp::amplify)
            .map(distorion::apply)
            .map(postamp::amplify);
  }

}