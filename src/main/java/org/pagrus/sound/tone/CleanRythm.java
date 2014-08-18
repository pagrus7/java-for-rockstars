package org.pagrus.sound.tone;

import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.Normalizer;

public class CleanRythm implements Tone {
  private Normalizer normalizer = new Normalizer(0.6);

  @Override
  public DoubleStream with(DoubleStream input) {
    return input.map(normalizer::apply);
  }

}
