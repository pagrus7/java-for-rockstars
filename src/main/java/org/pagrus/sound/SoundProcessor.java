package org.pagrus.sound;

import java.util.Arrays;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.Delay;
import org.pagrus.sound.plumbing.StereoOut;

public class SoundProcessor {
  private Delay delay = new Delay(200, 1, 0.5);
  private Amplifier amp = new Amplifier(2);

  /**
   * Process a single buffer of sound samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   * @param estimatedSampleTimeNanos offset relative to the very first sample, in nanoseconds
   */
  public void processBuffer(int[] inputSamples, StereoOut out, long sampleTime, long estimatedSampleTimeNanos) {

    Arrays.stream(inputSamples)
    .mapToDouble(i -> ((double) i / Integer.MAX_VALUE))

    .map(amp::apply)
    .map(delay::apply)

    .mapToInt(d -> ((int)(d* Integer.MAX_VALUE)))
    .forEach(i -> out.putInt(i));

  }
}
