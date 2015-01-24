package org.pagrus.sound.plumbing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import org.pagrus.sound.SoundProcessor;

import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverState;

public class AsioSoundSystem {
  public static final AsioSoundSystem INSTANCE = new AsioSoundSystem();

  private Set<AsioChannel> activeChannels = new HashSet<AsioChannel>();
  private AsioDriver asioDriver;
  private AsioListener listener;
  private SoundProcessor soundProcessor;

  private AsioSoundSystem() {
    init();
  }

  public boolean isRunning() {
    return asioDriver.getCurrentState().equals(AsioDriverState.RUNNING);
  }

  private void init() {
    List<String> driverNameList = AsioDriver.getDriverNames();
    System.out.println(driverNameList);
    asioDriver = AsioDriver.getDriver(driverNameList.get(0));

    int bufferSize = asioDriver.getBufferPreferredSize();
    System.out.println("buffer size: " + bufferSize);
    System.out.println("sample rate: " + asioDriver.getSampleRate());

    soundProcessor = new SoundProcessor(bufferSize);
  }

  public void start() {
    // create a Set of AsioChannels, defining which input and output channels 
    // will be used

    // configure the ASIO driver to use the given channels
    AsioChannel inputChannel = asioDriver.getChannelInput(0);
    AsioChannel rightOutputChannel = asioDriver.getChannelOutput(0);
    AsioChannel leftOutputChannel = asioDriver.getChannelOutput(1);
    
    activeChannels.add(inputChannel);
    activeChannels.add(rightOutputChannel);
    activeChannels.add(leftOutputChannel);


    // add an AsioDriverListener in order to receive callbacks from the driver
    listener = new AsioListener(inputChannel, leftOutputChannel, rightOutputChannel, asioDriver.getBufferPreferredSize(), soundProcessor);

    asioDriver.addAsioDriverListener(listener);

    // create the audio buffers and prepare the driver to run
    asioDriver.createBuffers(activeChannels);

    // start the driver
    asioDriver.start();
  }

  public void stop() {
    asioDriver.returnToState(AsioDriverState.INITIALIZED);
    asioDriver.removeAsioDriverListener(listener);
  }

  public void terminate() {
    if (isRunning()) {
      stop();
    }
    asioDriver.shutdownAndUnloadDriver();
  }

  public void setSampleSniffer(BiConsumer<double[], Long> sniffer) {
    soundProcessor.setSampleSniffer(sniffer);
  }
}
