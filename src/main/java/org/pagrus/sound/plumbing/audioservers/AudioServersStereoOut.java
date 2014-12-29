package org.pagrus.sound.plumbing.audioservers;

import java.nio.FloatBuffer;

import org.pagrus.sound.plumbing.StereoOut;

public class AudioServersStereoOut implements StereoOut {
  private FloatBuffer leftOut;
  private FloatBuffer rightOut;

  public AudioServersStereoOut(FloatBuffer leftOut, FloatBuffer rightOut) {
    this.leftOut = leftOut;
    this.rightOut = rightOut;
  }

  @Override
  public void putSample(double sample) {
    float floatSample = (float) sample;
    leftOut.put(floatSample);
    rightOut.put(floatSample);
  }

}
