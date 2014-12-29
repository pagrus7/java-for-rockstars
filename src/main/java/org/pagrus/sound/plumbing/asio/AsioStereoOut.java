package org.pagrus.sound.plumbing.asio;

import org.pagrus.sound.plumbing.StereoOut;

import com.synthbot.jasiohost.AsioChannel;

/**
 * The implementation buffers the samples, flushes the whole buffer into
 * left/right ASIO channels.
 */
public class AsioStereoOut implements StereoOut {
  private AsioChannel leftChannel;
  private AsioChannel rightChannel;
  private float[] outBuffer;
  private int idx;

  public AsioStereoOut(AsioChannel leftChannel, AsioChannel rightChannel, int bufferSize) {
    this.leftChannel = leftChannel;
    this.rightChannel = rightChannel;
    outBuffer = new float[bufferSize];
  }

  @Override
  public void putSample(double sample) {
    outBuffer[idx++] = (float) sample;
    if (idx == outBuffer.length) {
      flush();
    }
  }

  private void flush() {
    leftChannel.write(outBuffer);
    rightChannel.write(outBuffer);
    idx = 0;
  }

}
