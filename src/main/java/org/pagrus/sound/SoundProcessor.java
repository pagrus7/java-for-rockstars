package org.pagrus.sound;

import gnu.trove.list.array.TDoubleArrayList;

import java.util.function.Consumer;
import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.ClippingDistorion;
import org.pagrus.sound.effects.Normalizer;
import org.pagrus.sound.plumbing.StereoOut;

public class SoundProcessor {
  private static final long SNIFFING_INTERVAL = 40_000_000; // 40 ms in nanos
  private static final int DEFAULT_BUFFER_SIZE = 512;

  private double[] sniffedSamples;
  private Consumer<double[]> sniffer = d -> {};
  private TDoubleArrayList sniffedSamplesList;
  long lastSniffedTime;

  private Normalizer preNormalizer = new Normalizer(0.2);
  private ClippingDistorion distortion = new ClippingDistorion(0.05, 0.25, 2);
  private Amplifier postAmp = new Amplifier(3);

  public SoundProcessor() {
    updateBufferSize(DEFAULT_BUFFER_SIZE);
  }

  /**
   * May potentially get invoked by the sound system
   */
  public void updateBufferSize(int bufferSize) {
    sniffedSamples = new double[bufferSize];
    sniffedSamplesList = TDoubleArrayList.wrap(sniffedSamples);
  }

  /**
   * Process a single buffer of sound samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   */
  public void processBuffer(DoubleStream inputSamples, StereoOut out, long sampleTime) {
    sniffedSamplesList.reset();

    inputSamples

    .map(preNormalizer::apply)

    .map(distortion::apply)

    .map(postAmp::apply)

    .peek(sniffedSamplesList::add)

    .forEach(out :: putSample);

    if (sampleTime > lastSniffedTime + SNIFFING_INTERVAL) {
      sniffer.accept(sniffedSamples);
      lastSniffedTime = sampleTime;
    }
  }

  public void setSampleSniffer(Consumer<double[]> sniffer) {
    this.sniffer = sniffer;
  }
}
