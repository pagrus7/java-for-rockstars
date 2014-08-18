package org.pagrus.sound.effects;

import gnu.trove.list.array.TDoubleArrayList;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

public class SoundFileMixer {
  private static final int DESIRED_SAMPLE_RATE = 44100;

  // assume stereo
  private static int EXPECTED_INPUT_CHANNELS = 2;

  private TDoubleArrayList sampleList;
  private int index;

  public SoundFileMixer(String filePath) {
    try {
      readSamples(filePath);
    } catch (IOException | UnsupportedAudioFileException e) {
      throw new IllegalStateException(e);
    }
  }

  public double mix(double input) {
    double currentSample = sampleList.getQuick(index++ % sampleList.size());
    return input + currentSample;
  }

  private void readSamples(String filePath) throws IOException, UnsupportedAudioFileException {
    System.out.println("Reading " + filePath);
    File file = new File(filePath);

    int count = 0;
    try (AudioInputStream in = AudioSystem.getAudioInputStream(file)) {

      AudioFormat baseFormat = in.getFormat();
      sampleList = new TDoubleArrayList(estimateSize(file));

      AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, DESIRED_SAMPLE_RATE, 16,
          EXPECTED_INPUT_CHANNELS, baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
      AudioInputStream decodedIn = AudioSystem.getAudioInputStream(decodedFormat, in);

      byte[] buffer = new byte[8192];

      int read;
      while ((read = decodedIn.read(buffer)) != 0) {
        for (int offset = 0; offset < read; offset += 4) {
          // again, assume stereo and mix samples
          double sample1 = ((buffer[offset + 0] & 0xFF) | (buffer[offset + 1] << 8)) / 32768.0;
          double sample2 = ((buffer[offset + 2] & 0xFF) | (buffer[offset + 3] << 8)) / 32768.0;
          double mixedSample = (sample1 + sample2) / 2;
          count++;
          try {
            sampleList.add(mixedSample);
          } catch (RuntimeException rex) {
            System.out.println("errored out at " + count);
            throw rex;
          }
        }
      }
    }

    System.out.println("Done");
  }

  private int estimateSize(File file) throws UnsupportedAudioFileException, IOException {
    TAudioFileFormat format = (TAudioFileFormat) AudioSystem.getAudioFileFormat(file);
    long microseconds = (long) format.properties().get("duration");

    int requiredSamples = (int) Math.floor((DESIRED_SAMPLE_RATE * ((double) microseconds / 1000_000)));
    System.out.println(requiredSamples);
    return requiredSamples;
  }
}
