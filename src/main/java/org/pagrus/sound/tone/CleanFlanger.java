package org.pagrus.sound.tone;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.Delay;
import org.pagrus.sound.effects.Flanger;
import org.pagrus.sound.effects.Normalizer;

import java.util.stream.DoubleStream;

public class CleanFlanger implements Tone {
  private Flanger flanger = new Flanger(4, 1 ,2, 0.5, 0.5);
  private Amplifier preamp = new Amplifier(6);

  @Override
  public DoubleStream with(DoubleStream input) {
    return input
        .map(preamp::amplify)
        .map(flanger::apply);
  }

}
