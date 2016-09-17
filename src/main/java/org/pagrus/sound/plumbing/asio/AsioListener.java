package org.pagrus.sound.plumbing.asio;

import java.util.Set;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.pagrus.sound.SoundProcessor;
import org.pagrus.sound.plumbing.StereoOut;

import com.synthbot.jasiohost.AsioChannel;
import rx.Observable;

public class AsioListener extends EmptyAsioDriverListener {
  private AsioChannel inputChannel;
  private StereoOut stereoOut;

  private float[] inputSamples;
  private SoundProcessor soundProcessor;
  private int bufferSize;

  public AsioListener(AsioChannel inputChannel, AsioChannel leftOutputChannel,
      AsioChannel rightOutputChannel, int bufferSize, SoundProcessor soundProcessor) {
    this.inputChannel = inputChannel;
    this.stereoOut = new AsioStereoOut(leftOutputChannel, rightOutputChannel,  bufferSize);
    this.bufferSize = bufferSize;
    inputSamples = new float[bufferSize];

    this.soundProcessor = soundProcessor;
  }

  @Override
  public void bufferSwitch(long sampleTime, long samplePosition, Set<AsioChannel> activeChannels) {
    inputChannel.read(inputSamples);
    Double[] inputAsDouble = new Double[inputSamples.length];
    for (int i = 0; i < inputSamples.length; i++) {
      inputAsDouble[i] = new Double(inputSamples[i]);
    }

    soundProcessor.processBuffer(Observable.from(inputAsDouble), stereoOut, sampleTime);
  }

  @Override
  public void latenciesChanged(int inputLatency, int outputLatency) {
  }

}
