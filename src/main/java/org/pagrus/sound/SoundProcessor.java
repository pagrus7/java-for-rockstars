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
  Tone bigDistortionWithReverb = new BigDistortionWithReverb();
  Tone slightDistortion = new SlightDistortion();

  Tone clean = new CleanRythm();
  Tone cleanFlanger = new CleanFlanger();
  Tone delayWithLittleDistortion = new SlightDistortionWithDelay();

  Tone bigDistortion = new BigDistortion();


  private OverTimeSelector<Tone> toneSelector;
  private SoundMixer track = new SoundMixer(1.0, 1.0,
      SoundFileReader.INSTANCE.readAsArray(SoundProcessor.class.getResource("/backing-lw-lg-cs-sec-1.mp3").getFile()));

  public SoundProcessor() {
    updateBufferSize(DEFAULT_BUFFER_SIZE);

    toneSelector = OverTimeSelector
        .startWith(clean)
        .thenAt(Duration.parse("PT25.1S"), cleanFlanger)
        .thenAt(Duration.parse("PT36.2S"), slightDistortion)
        .thenAt(Duration.parse("PT69.2S"), delayWithLittleDistortion)
            // todo delay + distortion
        .thenAt(Duration.parse("PT98.5S"), slightDistortion)
        .thenAt(Duration.parse("PT133.7S"), bigDistortion)
        .thenAt(Duration.parse("PT193.1S"), bigDistortionWithReverb)
        .thenAt(Duration.parse("PT217S"), slightDistortion)
        .thenAt(Duration.parse("PT274.5S"), delayWithLittleDistortion)
        .build();

    toneSelector.print();
  }

  /**
   * May potentially get invoked by the sound system
   */
  public void updateBufferSize(int bufferSize) {
    sniffed = new double[bufferSize];
    sniffedList = TDoubleArrayList.wrap(sniffed);
  }

  int tmp = 0;
  /**
   * Process a stream of input samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   */
  public void processBuffer(DoubleStream input, StereoOut out, long sampleTime) {
    sniffedList.reset();
    Tone tone = toneSelector.forTime(sampleTime);
    if(tmp++ % 10 == 0) {
      System.out.println(sampleTime + " -> " + tone);
    }
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
