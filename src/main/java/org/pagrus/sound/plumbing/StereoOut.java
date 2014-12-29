package org.pagrus.sound.plumbing;

/**
 * Convenience abstraction which can write sample copy into both left and right
 * channel.
 */
public interface StereoOut {

  public void putSample(double sample);

}