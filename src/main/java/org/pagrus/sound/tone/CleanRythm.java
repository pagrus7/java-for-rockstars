package org.pagrus.sound.tone;

import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.Delay;
import org.pagrus.sound.effects.Normalizer;

public class CleanRythm implements Tone {

  @Override
  public DoubleStream with(DoubleStream input) {
    return input.map(i -> i * 6);
  }

}