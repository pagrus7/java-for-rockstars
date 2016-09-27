package org.pagrus.sound.tone;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.ClippingDistorion;
import org.pagrus.sound.effects.Delay;

import java.util.stream.DoubleStream;

public class BigDistortionWithReverb extends BigDistortion {

  Delay delay = new Delay(300, 1d, 0.5);
  Delay secondWave = new Delay(600, 1d, 0.2);

  Amplifier preamp = new Amplifier(7);
  ClippingDistorion distorion = new ClippingDistorion(0.05, 0.05, 1);
  Amplifier postamp = new Amplifier(3);

  @Override
  public DoubleStream with(DoubleStream input) {
    return input
            .map(preamp::amplify)
            .map(distorion::apply)
            .map(postamp::amplify)
            .map(delay::apply)
            .map(secondWave::apply);
  }

}