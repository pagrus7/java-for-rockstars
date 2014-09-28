package org.pagrus.sound;

import java.io.IOException;

import org.pagrus.sound.plumbing.SoundSystem;

public class ConsoleMain {

  public static void main(String[] args) throws IOException {
    SoundSystem soundSystem = SoundSystem.get();
    soundSystem.start();

    System.in.read();

    soundSystem.terminate();
  }
}
