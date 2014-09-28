package org.pagrus.sound.plumbing.asio;

import com.synthbot.jasiohost.AsioChannel;

/**
 * Convenience class which buffers samples and then flushes copy into left and
 * right channels.
 */
public class StereoOut {
  private AsioChannel leftChannel;
  private AsioChannel rightChannel;
  private float[] outBuffer;
  private int idx;

  public StereoOut(AsioChannel leftChannel, AsioChannel rightChannel, int bufferSize) {
    this.leftChannel = leftChannel;
    this.rightChannel = rightChannel;
    outBuffer = new float[bufferSize];
  }

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
