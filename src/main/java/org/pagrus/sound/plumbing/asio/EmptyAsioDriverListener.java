package org.pagrus.sound.plumbing.asio;

import java.util.Set;

import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriverListener;

public class EmptyAsioDriverListener implements AsioDriverListener {

  @Override
  public void sampleRateDidChange(double sampleRate) {
  }

  @Override
  public void resetRequest() {
  }

  @Override
  public void resyncRequest() {
  }

  @Override
  public void bufferSizeChanged(int bufferSize) {
  }

  @Override
  public void latenciesChanged(int inputLatency, int outputLatency) {
  }

  @Override
  public void bufferSwitch(long sampleTime, long samplePosition, Set<AsioChannel> activeChannels) {
  }

}
