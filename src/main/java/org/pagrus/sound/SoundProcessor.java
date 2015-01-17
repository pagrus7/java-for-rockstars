package org.pagrus.sound;

import java.util.Arrays;

import org.pagrus.sound.effects.SoundFileReader;
import org.pagrus.sound.effects.SoundMixer;
import org.pagrus.sound.plumbing.StereoOut;

public class SoundProcessor {
  private SoundMixer track = new SoundMixer(1.0, 0.3, SoundFileReader.INSTANCE.readAsArray("/my/music/collection/layla-unplugged-novoice-fragment.mp3"));

  /**
   * Process a single buffer of sound samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   * @param estimatedSampleTimeNanos offset relative to the very first sample, in nanoseconds
   */
  public void processBuffer(int[] inputSamples, StereoOut out, long sampleTime, long estimatedSampleTimeNanos) {

    Arrays.stream(inputSamples)
    .mapToDouble(i -> ((double) i / Integer.MAX_VALUE))
    .map(i -> i * 2)

    .map(d -> track.mix(d))

    .mapToInt(d -> ((int)(d * Integer.MAX_VALUE)))
    .forEach(i -> out.putInt(i));

  }
}
