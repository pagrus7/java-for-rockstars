package org.pagrus.sound.tone;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.ClippingDistorion;
import org.pagrus.sound.effects.Delay;

import java.util.stream.DoubleStream;

public class SlightDistortionWithDelay implements Tone {

  Amplifier preamp = new Amplifier(7);
  ClippingDistorion distorion = new ClippingDistorion(0.2, 0.05, 1);

  Delay delay = new Delay(30, 1d, 0.5);
  Amplifier postamp = new Amplifier(2);

  @Override
  public DoubleStream with(DoubleStream input) {
    return input
            .map(preamp::amplify)
            .map(distorion::apply)
            .map(delay::apply)
            .map(postamp::amplify);
  }
}