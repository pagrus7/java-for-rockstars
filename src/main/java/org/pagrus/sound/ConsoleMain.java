package org.pagrus.sound;

import java.io.IOException;

import org.pagrus.sound.plumbing.asio.AsioSoundSystem;

public class ConsoleMain {

  public static void main(String[] args) throws IOException {
    AsioSoundSystem soundSystem = AsioSoundSystem.INSTANCE;
    soundSystem.start();

    System.in.read();

    soundSystem.terminate();
  }
}
