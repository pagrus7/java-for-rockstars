package org.pagrus.sound.plumbing;

import java.nio.ByteBuffer;

public class StereoOut {
  private ByteBuffer leftOutBuffer;
  private ByteBuffer rightOutBuffer;

  public StereoOut(ByteBuffer left, ByteBuffer right) {
    this.leftOutBuffer = left;
    this.rightOutBuffer = right;
  }

  public void putInt(int value) {
    leftOutBuffer.putInt(value);
    rightOutBuffer.putInt(value);
  }
}
