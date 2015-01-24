package org.pagrus.sound.tone;

import java.util.stream.DoubleStream;

public interface Tone {

  public DoubleStream with(DoubleStream input);
}