package org.pagrus.sound.tone;

import org.pagrus.sound.effects.Delay;
import org.pagrus.sound.effects.Flanger;
import org.pagrus.sound.effects.Normalizer;

import java.util.stream.DoubleStream;

public class CleanFlanger implements Tone {
  private Normalizer normalizer = new Normalizer(0.6);
  private Flanger flanger = new Flanger(30d,400d,200d,0.7, 0.3);

  @Override
  public DoubleStream with(DoubleStream input) {
    return input
        .map(normalizer::apply)
        .map(flanger::apply);
  }

}
