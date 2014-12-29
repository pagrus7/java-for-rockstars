package org.pagrus.sound.plumbing.asio;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pagrus.sound.SoundProcessor;
import org.pagrus.sound.plumbing.SoundSystem;

import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverState;

public class AsioSoundSystem implements SoundSystem {
  public static final SoundSystem INSTANCE = new AsioSoundSystem();

  private Set<AsioChannel> activeChannels = new HashSet<AsioChannel>();
  private AsioChannel inputChannel;
  private AsioChannel rightOutputChannel;
  private AsioChannel leftOutputChannel;
  private AsioDriver asioDriver;
  private AsioListener listener;
  private int bufferPreferredSize;



  private AsioSoundSystem() {
    init();
  }

  boolean isRunning() {
    return asioDriver.getCurrentState().equals(AsioDriverState.RUNNING);
  }

  private void init() {

    List<String> driverNameList = AsioDriver.getDriverNames();
    System.out.println(driverNameList);
    asioDriver = AsioDriver.getDriver(driverNameList.get(0));

    // create a Set of AsioChannels, defining which input and output channels
    // will be used

    // configure the ASIO driver to use the given channels
    inputChannel = asioDriver.getChannelInput(0);
    rightOutputChannel = asioDriver.getChannelOutput(0);
    leftOutputChannel = asioDriver.getChannelOutput(1);

    activeChannels.add(inputChannel);
    activeChannels.add(rightOutputChannel);
    activeChannels.add(leftOutputChannel);

    bufferPreferredSize = asioDriver.getBufferPreferredSize();
    System.out.println("buffer size: " + bufferPreferredSize);
    System.out.println("sample rate: " + asioDriver.getSampleRate());

  }

  @Override
  public void start(SoundProcessor soundProcessor) {
    soundProcessor.updateBufferSize(bufferPreferredSize);

    // add an AsioDriverListener in order to receive callbacks from the driver
    listener = new AsioListener(inputChannel, leftOutputChannel, rightOutputChannel, bufferPreferredSize, soundProcessor);
    asioDriver.addAsioDriverListener(listener);

    // create the audio buffers and prepare the driver to run
    asioDriver.createBuffers(activeChannels);

    // start the driver
    asioDriver.start();
  }

  @Override
  public void stop() {
    asioDriver.returnToState(AsioDriverState.INITIALIZED);
    asioDriver.removeAsioDriverListener(listener);
  }

  @Override
  public void terminate() {
    if (isRunning()) {
      asioDriver.shutdownAndUnloadDriver();
    }
  }

}
