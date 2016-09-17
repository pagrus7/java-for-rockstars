package org.pagrus.sound.tone;

import rx.Observable;

public interface Tone {
  public Observable<Double> applyTo(Observable<Double> input);
}
