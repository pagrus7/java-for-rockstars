package org.pagrus.sound.tone;

import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.ClippingDistorion;
import org.pagrus.sound.effects.Delay;
import org.pagrus.sound.effects.Normalizer;
import rx.Observable;

public class DistortedSolo implements Tone {

  private Normalizer inNormalizer = new Normalizer(0.2);
  private ClippingDistorion distortion = new ClippingDistorion(0.05, 0, 1);
  private Amplifier postAmp = new Amplifier(8);
  private Delay shortDelay = new Delay(20, 1, 0.5);


  @Override
  public Observable<Double> applyTo(Observable<Double> input) {
    return input
        .map(inNormalizer::apply)
        .map(distortion::apply)
        .map(shortDelay::apply)
        .map(postAmp::amplify);
  }

}
