package org.pagrus.sound;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.SoundFileReader;
import org.pagrus.sound.effects.SoundMixer;
import org.pagrus.sound.plumbing.StereoOut;
import org.pagrus.sound.tone.*;

import gnu.trove.list.array.TDoubleArrayList;

public class SoundProcessor {
  private static final int DEFAULT_BUFFER_SIZE = 512;

  private BiConsumer<double[], Long> sniffer = (d, t) -> {};
  private double[] sniffed;
  private TDoubleArrayList sniffedList;

  private OverTimeSelector<Tone> toneSelector;
  private SoundMixer track = new SoundMixer(2.0, 1.0,
      SoundFileReader.INSTANCE.readAsArray("/Java4Rock/repo/java-for-rockstars/src/main/resources/backing-lw-lg-cs-sec-1.mp3"));

  public SoundProcessor() {
    updateBufferSize(DEFAULT_BUFFER_SIZE);

    Tone clean = new CleanRythm();
    Tone cleanFlanger = new CleanFlanger();
    Tone delayWithLittleDistortion = new SlightDistortionWithDelay();
    Tone slightDistortion = new SlightDistortion();
    Tone bigDistortion = new BigDistortion();
    Tone bigDistortionWithReverb = new BigDistortionWithReverb();
    toneSelector = OverTimeSelector
        .startWith(clean)
        .thenAt(Duration.parse("PT25.1S"), cleanFlanger)
        .thenAt(Duration.parse("PT69.2S"), delayWithLittleDistortion)
        .thenAt(Duration.parse("PT98.5S"), slightDistortion)
        .thenAt(Duration.parse("PT133.7S"), bigDistortion)
        .thenAt(Duration.parse("PT193.1S"), bigDistortionWithReverb)
        .thenAt(Duration.parse("PT217S"), slightDistortion)
        .thenAt(Duration.parse("PT274.5S"), delayWithLittleDistortion)
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
