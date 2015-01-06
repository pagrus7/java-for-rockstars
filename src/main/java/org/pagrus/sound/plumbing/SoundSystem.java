package org.pagrus.sound.plumbing;

import java.lang.reflect.Field;

import org.pagrus.sound.SoundProcessor;

public interface SoundSystem {
  public void start(SoundProcessor soundProcessor);

  public void stop();

  public void terminate();

  public static SoundSystem get() {
    SoundSystem soundSystem = firstAvailable(
        "org.pagrus.sound.plumbing.asio.AsioSoundSystem",
        "org.pagrus.sound.plumbing.audioservers.AudioServersSoundSystem"
        );
    System.out.println("Using " + soundSystem);
    return soundSystem;
  }

  static SoundSystem firstAvailable(String... implClassNames) {
    for (String className : implClassNames) {
      try {
        Class<?> clazz = Class.forName(className);
        Field instanceField = clazz.getDeclaredField("INSTANCE");
        SoundSystem instance = (SoundSystem) instanceField.get(null);
        return instance;
      } catch (Throwable t) {
        System.out.println("Could not initialize " + className);
      }
    }
    throw new IllegalStateException("No sound system implementations available");

  }

}