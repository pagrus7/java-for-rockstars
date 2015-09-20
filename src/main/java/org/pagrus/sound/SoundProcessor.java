package org.pagrus.sound;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.SoundFileReader;
import org.pagrus.sound.effects.SoundMixer;
import org.pagrus.sound.plumbing.StereoOut;
import org.pagrus.sound.tone.CleanRythm;
import org.pagrus.sound.tone.DistortedSolo;
import org.pagrus.sound.tone.OverTimeSelector;
import org.pagrus.sound.tone.Tone;

import gnu.trove.list.array.TDoubleArrayList;

public class SoundProcessor {
  private static final int DEFAULT_BUFFER_SIZE = 512;

  private BiConsumer<double[], Long> sniffer = (d, t) -> {};
  private double[] sniffed;
  private TDoubleArrayList sniffedList;

  private OverTimeSelector<Tone> toneSelector;
  private SoundMixer track = new SoundMixer(1.0, 1.0, 
      SoundFileReader.INSTANCE.readAsArray(System.getenv("HOME") + "/personal/music/collection/nickelback-rockstar-fragment.mp3"));

  public SoundProcessor() {
    updateBufferSize(DEFAULT_BUFFER_SIZE);

    Tone clean = new CleanRythm();
    Tone distorted = new DistortedSolo();
    toneSelector = OverTimeSelector
        .startWith(clean)
        .thenAt(Duration.parse("PT14.1S"), distorted)
        .thenAt(Duration.parse("PT46.7S"), clean)
        .build();
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
    toneSelector.forTime(sampleTime)
      .with(input)
      .peek(sniffedList::add)
      .map(track::mix)

      .forEach(out::putSample);

    sniffer.accept(sniffed, sampleTime);
  }

  public void setSampleSniffer(BiConsumer<double[], Long> sniffer) {
    this.sniffer = sniffer;
  }
}
