package org.pagrus.sound.plumbing.asio;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.pagrus.sound.SoundProcessor;
import org.pagrus.sound.plumbing.SoundSystem;

import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverState;

public class AsioSoundSystem implements SoundSystem {
  public static final SoundSystem INSTANCE = new AsioSoundSystem();

  private Set<AsioChannel> activeChannels = new HashSet<AsioChannel>();
  private AsioDriver asioDriver;
  private AsioListener listener;
  private SoundProcessor soundProcessor;

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
    AsioChannel inputChannel = asioDriver.getChannelInput(0);
    AsioChannel rightOutputChannel = asioDriver.getChannelOutput(0);
    AsioChannel leftOutputChannel = asioDriver.getChannelOutput(1);

    activeChannels.add(inputChannel);
    activeChannels.add(rightOutputChannel);
    activeChannels.add(leftOutputChannel);

    int bufferSize = asioDriver.getBufferPreferredSize();
    System.out.println("buffer size: " + bufferSize);
    System.out.println("sample rate: " + asioDriver.getSampleRate());

    // add an AsioDriverListener in order to receive callbacks from the driver
    soundProcessor = new SoundProcessor(bufferSize);
    listener = new AsioListener(inputChannel, leftOutputChannel, rightOutputChannel, bufferSize, soundProcessor);
    asioDriver.addAsioDriverListener(listener);
  }

  @Override
  public void start() {
    // create the audio buffers and prepare the driver to run
    asioDriver.createBuffers(activeChannels);

    // start the driver
    asioDriver.start();
  }

  @Override
  public void stop() {
    asioDriver.returnToState(AsioDriverState.INITIALIZED);
  }

  @Override
  public void terminate() {
    if (isRunning()) {
      asioDriver.shutdownAndUnloadDriver();
    }
  }

  @Override
  public void setSampleSniffer(Consumer<double[]> sniffer) {
    soundProcessor.setSampleSniffer(sniffer);
  }
}
