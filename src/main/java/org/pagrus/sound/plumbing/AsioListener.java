package org.pagrus.sound.plumbing;

import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.pagrus.sound.SoundProcessor;

import com.synthbot.jasiohost.AsioChannel;

public class AsioListener extends EmptyAsioDriverListener {
  private static final int SAMPLE_RATE = 44100;

  private AsioChannel inputChannel;
  private AsioChannel leftOutputChannel;
  private AsioChannel rightOutputChannel;

  private int[] inputSamples;
  private SoundProcessor soundProcessor;
  private long bufferCounter;
  private int bufferSize;

  public AsioListener(AsioChannel inputChannel, AsioChannel leftOutputChannel,
      AsioChannel rightOutputChannel, int bufferSize, SoundProcessor soundProcessor) {
    this.inputChannel = inputChannel;
    this.leftOutputChannel = leftOutputChannel;
    this.rightOutputChannel = rightOutputChannel;
    this.bufferSize = bufferSize;
    inputSamples = new int[bufferSize];

    this.soundProcessor = soundProcessor;
  }

  @Override
  public void bufferSwitch(long sampleTime, long samplePosition, Set<AsioChannel> activeChannels) {
    ByteBuffer inputBuffer = inputChannel.getByteBuffer();
    inputBuffer.asIntBuffer().get(inputSamples);

    StereoOut out = new StereoOut(leftOutputChannel.getByteBuffer(), rightOutputChannel.getByteBuffer());

    long estimatedSampleTimeNanos = bufferCounter++ * bufferSize * TimeUnit.SECONDS.toNanos(1) / SAMPLE_RATE;

    soundProcessor.processBuffer(inputSamples, out, sampleTime, estimatedSampleTimeNanos);
  }

  @Override
  public void latenciesChanged(int inputLatency, int outputLatency) {
  }

}
