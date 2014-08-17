package org.pagrus.sound;

import java.util.Arrays;

import org.pagrus.sound.plumbing.StereoOut;
import org.pagrus.sound.effects.Delay;

public class SoundProcessor {
  private Delay delay = new Delay(200, 1, 0.5); 

  /**
   * Process a single buffer of sound samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   * @param estimatedSampleTimeNanos offset relative to the very first sample, in nanoseconds
   */
  public void processBuffer(int[] inputSamples, StereoOut out, long sampleTime, long estimatedSampleTimeNanos) {

    // TODO #1 - complete the Delay implementation

    Arrays.stream(inputSamples)
    .mapToDouble(i -> ((double) i / Integer.MAX_VALUE))
    .map(i -> i * 2)

    // TODO #2 - apply delay here

    .mapToInt(d -> ((int)(d * Integer.MAX_VALUE)))
    .forEach(i -> out.putInt(i));

  }
}
