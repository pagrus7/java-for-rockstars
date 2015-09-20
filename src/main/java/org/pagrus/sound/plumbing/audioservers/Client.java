package org.pagrus.sound.plumbing.audioservers;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.jaudiolibs.audioservers.AudioClient;
import org.jaudiolibs.audioservers.AudioConfiguration;
import org.pagrus.sound.SoundProcessor;

public class Client implements AudioClient {
  int bufferSize;
  float[] buffer;
  private SoundProcessor soundProcessor;
  long frameCounter;

  @Override
  public void configure(AudioConfiguration config) throws Exception {
    bufferSize = config.getMaxBufferSize();
    buffer = new float[bufferSize];
    soundProcessor.updateBufferSize(bufferSize);
  }

  @Override
  public boolean process(long time, List<FloatBuffer> inputs, List<FloatBuffer> outputs, int nframes) {

    FloatBuffer output1 = outputs.get(0);
    FloatBuffer output2 = outputs.get(1);
    AudioServersStereoOut out = new AudioServersStereoOut(output1, output2);

    FloatBuffer input = inputs.get(0);
    input.get(buffer);


    DoubleStream doubleStream = IntStream.range(0, bufferSize).mapToDouble(i -> buffer[i]);
    soundProcessor.processBuffer(doubleStream, out, TimeUnit.SECONDS.toNanos(1) / AudioServersSoundSystem.SAMPLING_RATE * frameCounter);
    frameCounter += nframes;

    return true;
  }

  @Override
  public void shutdown() {
  }

  public void setSoundProcessor(SoundProcessor soundProcessor) {
    this.soundProcessor = soundProcessor;
  }

}