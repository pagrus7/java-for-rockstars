package org.pagrus.sound;

import gnu.trove.list.array.TDoubleArrayList;

import java.util.Arrays;
import java.util.function.Consumer;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.SoundFileReader;
import org.pagrus.sound.effects.SoundMixer;
import org.pagrus.sound.effects.ClippingDistorion;
import org.pagrus.sound.effects.Normalizer;
import org.pagrus.sound.plumbing.StereoOut;

public class SoundProcessor {
  private static final long SNIFFING_INTERVAL = 40_000_000; // 40 ms in nanos

  private double[] sniffedSamples;
  private Consumer<double[]> sniffer;
  private TDoubleArrayList sniffedSamplesList;
  long lastSniffedTime;

  private SoundMixer track = new SoundMixer(1, 0.5, SoundFileReader.INSTANCE.readAsArray("/my/music/collection/smoke-on-the-water-fragment.mp3"));
  private Normalizer preNormalizer = new Normalizer(0.2);
  private ClippingDistorion distortion = new ClippingDistorion(0.05, 0, 2);
  private Amplifier postAmp = new Amplifier(3);

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

    Arrays.stream(inputSamples)
    .mapToDouble(i -> ((double) i / Integer.MAX_VALUE))

    .map(preNormalizer::apply)

    .map(distortion::apply)

    .map(postAmp::apply)

    .peek(sniffedSamplesList::add)

    .map(track::mix)


    .mapToInt(d -> ((int)(d * Integer.MAX_VALUE)))
    .forEach(out::putInt);

    if (sniffer != null && sampleTime > lastSniffedTime + SNIFFING_INTERVAL) {
      sniffer.accept(sniffedSamples);
      lastSniffedTime = sampleTime;
    }
  }

  public void setSampleSniffer(Consumer<double[]> sniffer) {
    this.sniffer = sniffer;
  }
}
