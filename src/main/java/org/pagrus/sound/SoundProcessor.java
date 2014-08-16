package org.pagrus.sound;

import java.util.Arrays;

import org.pagrus.sound.plumbing.StereoOut;

public class SoundProcessor {

  /**
   * Process a single buffer of sound samples and write results to<code>out</code>. 
   * @param sampleTime system nano time associated with the samples.
   * @param estimatedSampleTimeNanos offset relative to the very first sample, in nanoseconds
   */
  public void processBuffer(int[] inputSamples, StereoOut out, long sampleTime, long estimatedSampleTimeNanos) {

    // 1. Replace "for" with streams and lambdas. [done]
    // 2. Hey, the sound is quiet. Can you make it twice as loud?
    // 3. How about parallel streams?
    // 4. Translate samples to [-1, 1] - will need for effects

    Arrays.stream(inputSamples)
    .forEach(i -> out.putInt(i));

//    for (int i : inputSamples) {
//      out.putInt(i);
//    }
  }
}
