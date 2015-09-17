package org.pagrus.sound;

import java.util.function.Consumer;
import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.Delay;
import org.pagrus.sound.plumbing.StereoOut;

public class SoundProcessor {
  private static final int DEFAULT_BUFFER_SIZE = 512;
  private Consumer<double[]> sniffer = d -> {};

  private Delay delay = new Delay(200, 1, 0.5); ;

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
      .map(d -> d * 2)
      // TODO: apply delay
      .forEach(d -> out.putSample(d));
    }

  public void setSampleSniffer(Consumer<double[]> sniffer) {
    this.sniffer = sniffer;
  }
}
