package org.pagrus.sound;

import gnu.trove.list.array.TDoubleArrayList;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import org.pagrus.sound.effects.SoundFileReader;
import org.pagrus.sound.effects.SoundMixer;
import org.pagrus.sound.plumbing.StereoOut;
import org.pagrus.sound.tone.CleanRythm;
import org.pagrus.sound.tone.DistortedSolo;
import org.pagrus.sound.tone.OverTimeSelector;
import org.pagrus.sound.tone.Tone;

public class SoundProcessor {
  private static final long SNIFFING_INTERVAL = 40_000_000; // 40 ms in nanos

  private double[] sniffedSamples;
  private BiConsumer<double[], Long> sniffer;
  private TDoubleArrayList sniffedSamplesList;
  long lastSniffedTime;

  private OverTimeSelector<Tone> toneSelector;
  private SoundMixer track = new SoundMixer(1.0, 1.0, SoundFileReader.INSTANCE.readAsArray("/my/music/collection/nickelback-rockstar-fragment.mp3"));

  public SoundProcessor(int bufferSize) {
    sniffedSamples = new double[bufferSize];
    sniffedSamplesList = TDoubleArrayList.wrap(sniffedSamples);

    Tone clean = new CleanRythm();
    Tone distorted = new DistortedSolo();
    toneSelector = OverTimeSelector
        .startWith(clean)
        .thenAt(Duration.parse("PT14.1S"), distorted)
        .thenAt(Duration.parse("PT46.7S"), clean)
        .build();
  }

  /**
   * Process a single buffer of sound samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   * @param estimatedSampleTimeNanos offset relative to the very first sample, in nanoseconds
   */
  public void processBuffer(int[] inputSamples, StereoOut out, long sampleTime, long estimatedSampleTimeNanos) {
    sniffedSamplesList.reset();

    IntStream stream = Arrays.stream(inputSamples);

    toneSelector.forTime(estimatedSampleTimeNanos)
    .with(stream.mapToDouble(i -> ((double) i) / Integer.MAX_VALUE))

    .peek(sniffedSamplesList::add)

    .map(track::mix)

    .mapToInt(d -> ((int)(d * Integer.MAX_VALUE)))
    .forEach(out::putInt);

    if (sniffer != null && sampleTime > lastSniffedTime + SNIFFING_INTERVAL) {
      sniffer.accept(sniffedSamples, estimatedSampleTimeNanos);
      lastSniffedTime = sampleTime;
    }
  }

  public void setSampleSniffer(BiConsumer<double[], Long> sniffer) {
    this.sniffer = sniffer;
  }
}
