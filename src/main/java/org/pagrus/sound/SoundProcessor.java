package org.pagrus.sound;

import java.util.function.Consumer;
import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.plumbing.StereoOut;

import gnu.trove.list.array.TDoubleArrayList;

public class SoundProcessor {
  private static final int DEFAULT_BUFFER_SIZE = 512;

  private Consumer<double[]> sniffer = d -> {};
  private double[] sniffed;
  private TDoubleArrayList sniffedList;

  private Amplifier amp = new Amplifier(2);

  public SoundProcessor() {
    updateBufferSize(DEFAULT_BUFFER_SIZE);
  }

  /**
   * May potentially get invoked by the sound system
   */
  public void updateBufferSize(int bufferSize) {
    sniffed = new double[bufferSize];
    sniffedList = TDoubleArrayList.wrap(sniffed);
  }

  /**
   * Process a stream of input samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   */
  public void processBuffer(DoubleStream input, StereoOut out, long sampleTime) {
    sniffedList.reset();

    // 1. Put samples into the sniffedList, as they "flow" through the stream. They will show up on UI then.
    // 2. How about one-liner for basic clipping distortion? Clip samples greater than X. 
    // UI is helpful for choosing the right X. 

    input
      .map(amp::amplify)
      .forEach(out::putSample);

    sniffer.accept(sniffed);
  }

  public void setSampleSniffer(Consumer<double[]> sniffer) {
    this.sniffer = sniffer;
  }
}
