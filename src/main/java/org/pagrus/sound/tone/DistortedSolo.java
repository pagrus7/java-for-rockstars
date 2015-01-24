package org.pagrus.sound.tone;

import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.ClippingDistorion;
import org.pagrus.sound.effects.Normalizer;

public class DistortedSolo implements Tone {

  private Normalizer inNormalizer = new Normalizer(0.2);
  private ClippingDistorion distortion = new ClippingDistorion(0.05, 0.05, 2);
  private Normalizer outNormalizer = new Normalizer(0.35);

  @Override
  public DoubleStream with(DoubleStream input) {
    return input.map(inNormalizer::apply).map(distortion::apply).map(outNormalizer::apply);
  }

}