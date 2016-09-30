package org.pagrus.sound;

import gnu.trove.list.array.TDoubleArrayList;
import org.pagrus.sound.effects.SoundFileReader;
import org.pagrus.sound.effects.SoundMixer;
import org.pagrus.sound.plumbing.StereoOut;
import org.pagrus.sound.tone.*;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.stream.DoubleStream;

public class SoundProcessor {
  private static final int DEFAULT_BUFFER_SIZE = 512;

  private BiConsumer<double[], Long> sniffer = (d, t) -> {};
  private double[] sniffed;
  private TDoubleArrayList sniffedList;

  Tone bigDistortionWithReverb = new BigDistortionWithReverb();
  Tone cleanDelay = new CleanDelay();
  Tone clean = new CleanRythm();
  Tone cleanFlanger = new CleanFlanger();
  Tone slightDistortionWithDelay = new SlightDistortionWithDelay();
  Tone bigDistortion = new BigDistortion();


  private OverTimeSelector<Tone> toneSelector;
  private SoundMixer track = new SoundMixer(1.0, 1.0,
      SoundFileReader.INSTANCE.readAsArray(SoundProcessor.class.getResource("/final_cut.mp3").getFile()));

  public SoundProcessor() {
    updateBufferSize(DEFAULT_BUFFER_SIZE);

    toneSelector = OverTimeSelector
        .startWith(clean)
        .thenAt(Duration.parse("PT57.5S") , cleanFlanger)
        .thenAt(Duration.parse("PT67.0S") , cleanDelay)
        .thenAt(Duration.parse("PT101.5S"), slightDistortionWithDelay)
        .thenAt(Duration.parse("PT133.6S"), bigDistortion)
        .thenAt(Duration.parse("PT155.9S"), bigDistortionWithReverb)
        .thenAt(Duration.parse("PT190.0S"), clean)
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
    Tone tone = toneSelector.forTime(sampleTime);
    tone
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
