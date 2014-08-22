package org.pagrus.sound;

import java.util.Arrays;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.Flanger;
import org.pagrus.sound.effects.SoundFileReader;
import org.pagrus.sound.effects.SoundMixer;
import org.pagrus.sound.plumbing.StereoOut;

public class SoundProcessor {
  private Amplifier amp = new Amplifier(2);
  private Flanger flanger = new Flanger(4, 1, 2, 1, 0.5);
  private SoundMixer track = new SoundMixer(1, 1, SoundFileReader.INSTANCE.readAsArray("/my/music/collection/come-as-you-are-E-fragment.mp3"));

  /**
   * Process a single buffer of sound samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   * @param estimatedSampleTimeNanos offset relative to the very first sample, in nanoseconds
   */
  public void processBuffer(int[] inputSamples, StereoOut out, long sampleTime, long estimatedSampleTimeNanos) {

    Arrays.stream(inputSamples)
    .mapToDouble(i -> ((double) i / Integer.MAX_VALUE))

    .map(amp::apply)
    .map(flanger::apply)
    .map(track::mix)

    .mapToInt(d -> ((int)(d * Integer.MAX_VALUE)))
    .forEach(out::putInt);

  }
}
