package org.pagrus.sound;

import java.util.function.Consumer;
import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.Flanger;
import org.pagrus.sound.effects.SoundFileReader;
import org.pagrus.sound.effects.SoundMixer;
import org.pagrus.sound.plumbing.StereoOut;

public class SoundProcessor {
  private static final int DEFAULT_BUFFER_SIZE = 512;
  private Consumer<double[]> sniffer = d -> {};

  private Amplifier amp = new Amplifier(2);
  private Flanger flanger = new Flanger(4, 1, 2, 1, 0.5);
  private SoundMixer track = new SoundMixer(1, 1,
      SoundFileReader.INSTANCE.readAsArray(System.getenv("HOME") + "/personal/music/collection/come-as-you-are-E-fragment.mp3"));


  public SoundProcessor() {
    updateBufferSize(DEFAULT_BUFFER_SIZE);
  }

  /**
   * May potentially get invoked by the sound system
   */
  public void updateBufferSize(int bufferSize) {
  }

  /**
   * Process a stream of input samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   */
  public void processBuffer(DoubleStream input, StereoOut out, long sampleTime) {
    input
      .map(amp::amplify)
      .map(flanger::apply)
      .map(track::mix)
      .forEach(out::putSample);
    }

  public void setSampleSniffer(Consumer<double[]> sniffer) {
    this.sniffer = sniffer;
  }
}
