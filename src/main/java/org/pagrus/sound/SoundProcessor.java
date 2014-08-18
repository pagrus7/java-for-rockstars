package org.pagrus.sound;

import gnu.trove.list.array.TDoubleArrayList;

import java.util.Arrays;
import java.util.function.Consumer;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.plumbing.StereoOut;

public class SoundProcessor {
  private static final long SNIFFING_INTERVAL = 40_000_000; // 40 ms in nanos

  private double[] sniffedSamples;
  private Consumer<double[]> sniffer;
  private TDoubleArrayList sniffedSamplesList;
  long lastSniffedTime;

  private Amplifier amp = new Amplifier(2);

  public SoundProcessor(int bufferSize) {
    sniffedSamples = new double[bufferSize];
    sniffedSamplesList = TDoubleArrayList.wrap(sniffedSamples);
  }

  /**
   * Process a single buffer of sound samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   * @param estimatedSampleTimeNanos offset relative to the very first sample, in nanoseconds
   */
  public void processBuffer(int[] inputSamples, StereoOut out, long sampleTime, long estimatedSampleTimeNanos) {
    sniffedSamplesList.reset();

    // 1. Put samples into the sniffedSamplesList, as they "flow" through the stream. They will show up on UI then.
    // 2. How about one-liner for basic clipping distortion? Clip samples greater than X. 
    // UI is helpful to choose the right X. 

    Arrays.stream(inputSamples)
    .mapToDouble(i -> ((double) i / Integer.MAX_VALUE))

    .map(amp::apply)

    .mapToInt(d -> ((int)(d* Integer.MAX_VALUE)))
    .forEach(i -> out.putInt(i));

    if (sniffer != null && sampleTime > lastSniffedTime + SNIFFING_INTERVAL) {
      sniffer.accept(sniffedSamples);
      lastSniffedTime = sampleTime;
    }
  }

  public void setSampleSniffer(Consumer<double[]> sniffer) {
    this.sniffer = sniffer;
  }
}
