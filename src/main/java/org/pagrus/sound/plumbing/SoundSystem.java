package org.pagrus.sound.plumbing;

import java.util.function.Consumer;

import org.pagrus.sound.plumbing.asio.AsioSoundSystem;

public interface SoundSystem {
  public void start();

  public void stop();

  public void terminate();

  public void setSampleSniffer(Consumer<double[]> sniffer);

  public static SoundSystem get() {
    return AsioSoundSystem.INSTANCE;
  }
}