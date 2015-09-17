package org.pagrus.sound.effects;

import gnu.trove.list.array.TDoubleArrayList;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 * Decodes a 44100 stereo file (mp3) into a double array of mono samples. Each
 * mono sample is average of left and right channels.
 */
public class SoundFileReader {
  public static final SoundFileReader INSTANCE = new SoundFileReader();

  private static final int DESIRED_SAMPLE_RATE = 44100;

  // assume stereo
  private static final int EXPECTED_INPUT_CHANNELS = 2;

  private SoundFileReader() {
  }

  public double[] readAsArray(String filePath) {

    System.out.println("Reading " + filePath);

    try {
      double[] result = readAsMonoSamples(filePath);

      System.out.println("Done");
      return result;
    } catch (IOException | UnsupportedAudioFileException e) {
      throw new IllegalArgumentException("Cannot read file " + filePath, e);
    }
  }

  private double[] readAsMonoSamples(String filePath) throws IOException, UnsupportedAudioFileException {
    File file = new File(filePath);
    double[] result;

    try (AudioInputStream in = AudioSystem.getAudioInputStream(file)) {

      AudioFormat baseFormat = in.getFormat();
      result = new double[estimateSize(file)];

      TDoubleArrayList sampleList = TDoubleArrayList.wrap(result);
      sampleList.resetQuick();

      AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, DESIRED_SAMPLE_RATE, 16, EXPECTED_INPUT_CHANNELS, baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
      AudioInputStream decodedIn = AudioSystem.getAudioInputStream(decodedFormat, in);

      byte[] buffer = new byte[8192];

      int read;
      while ((read = decodedIn.read(buffer)) != 0) {
        for (int offset = 0; offset < read; offset += 4) {
          // again, assume stereo and mix samples into mono
          double sample1 = ((buffer[offset + 0] & 0xFF) | (buffer[offset + 1] << 8)) / 32768.0;
          double sample2 = ((buffer[offset + 2] & 0xFF) | (buffer[offset + 3] << 8)) / 32768.0;
          double mixedSample = (sample1 + sample2) / 2;
          sampleList.add(mixedSample);
        }
      }
    }

    return result;
  }

  private int estimateSize(File file) throws UnsupportedAudioFileException, IOException {
    TAudioFileFormat format = (TAudioFileFormat) AudioSystem.getAudioFileFormat(file);
    long microseconds = (long) format.properties().get("duration");

    int requiredSamples = (int) Math.floor((DESIRED_SAMPLE_RATE * ((double) microseconds / 1000_000)));
    return requiredSamples;
  }
}