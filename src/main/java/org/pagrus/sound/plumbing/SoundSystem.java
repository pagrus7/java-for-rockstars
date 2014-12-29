package org.pagrus.sound.plumbing;

import org.pagrus.sound.SoundProcessor;
import org.pagrus.sound.plumbing.audioservers.AudioServersSoundSystem;

public interface SoundSystem {
  public void start(SoundProcessor soundProcessor);

  public void stop();

  public void terminate();

  public static SoundSystem get() {
    return AudioServersSoundSystem.INSTANCE;
  }

}