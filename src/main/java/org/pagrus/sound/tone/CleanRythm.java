package org.pagrus.sound.tone;

import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.Delay;
import org.pagrus.sound.effects.Normalizer;
import rx.Observable;

public class CleanRythm implements Tone {
  private Normalizer normalizer = new Normalizer(0.6);
  private Delay shortDelay = new Delay(30, 1, 0.5);

  @Override
  public Observable<Double> applyTo(Observable<Double> input) {
    return input
        .map(normalizer::apply)
        .map(shortDelay::apply);
  }

}
