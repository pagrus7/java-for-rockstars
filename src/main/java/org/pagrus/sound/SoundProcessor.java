package org.pagrus.sound;

import java.util.function.Consumer;
import java.util.stream.DoubleStream;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.ClippingDistorion;
import org.pagrus.sound.effects.Normalizer;
import org.pagrus.sound.effects.SoundFileReader;
import org.pagrus.sound.effects.SoundMixer;
import org.pagrus.sound.plumbing.StereoOut;

import gnu.trove.list.array.TDoubleArrayList;

public class SoundProcessor {
  private static final int DEFAULT_BUFFER_SIZE = 512;

  private Consumer<double[]> sniffer = d -> {};
  private double[] sniffed;
  private TDoubleArrayList sniffedList;

  private SoundMixer track = new SoundMixer(1, 0.5,
      SoundFileReader.INSTANCE.readAsArray(System.getenv("HOME") + "/personal/music/collection/smoke-on-the-water-fragment.mp3"));
  private Normalizer preNormalizer = new Normalizer(0.2);
  private ClippingDistorion distortion = new ClippingDistorion(0.05, 0, 2);
  private Amplifier postAmp = new Amplifier(2);

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

    input
      .map(preNormalizer::apply)
      .map(distortion::apply)
      .map(postAmp::amplify)

      .peek(sniffedList::add)

      .map(track::mix)

      .forEach(out::putSample);

    sniffer.accept(sniffed);
  }

  public void setSampleSniffer(Consumer<double[]> sniffer) {
    this.sniffer = sniffer;
  }
}
