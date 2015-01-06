package org.pagrus.sound.plumbing.audioservers;

import org.jaudiolibs.audioservers.AudioConfiguration;
import org.jaudiolibs.audioservers.AudioServer;
import org.jaudiolibs.audioservers.jack.JackAudioServer;
import org.pagrus.sound.SoundProcessor;
import org.pagrus.sound.plumbing.SoundSystem;

public class AudioServersSoundSystem implements SoundSystem, Runnable {
  public static final SoundSystem INSTANCE = new AudioServersSoundSystem();
  private static final int MAX_BUFFER_SIZE = 512;
  private static final int BIT_RATE = 44100;
  private AudioServer server;
  private Client audioClient = new Client();

  private AudioServersSoundSystem() {
    init();
  }

  private void init() {
    System.out.println("Initializing Jack audio server");
    AudioConfiguration config = new AudioConfiguration(BIT_RATE, 1, 2, MAX_BUFFER_SIZE, true);
    server = JackAudioServer.create("Java", config, true, audioClient);
  }

  @Override
  public void start(SoundProcessor soundProcessor) {
    audioClient.setSoundProcessor(soundProcessor);
    Thread audioThread = new Thread(this);
    audioThread.setDaemon(true);
    audioThread.setPriority(Thread.MAX_PRIORITY);
    audioThread.start();
  }

  @Override
  public void stop() {
    server.shutdown();
  }

  @Override
  public void terminate() {
    server.shutdown();
  }

  @Override
  public void run() {
    try {
      server.run();
    } catch (Exception e) {
      throw new IllegalStateException("Audio server failed", e);
    }
  }

}
