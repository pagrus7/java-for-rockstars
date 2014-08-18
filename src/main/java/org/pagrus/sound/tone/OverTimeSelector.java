package org.pagrus.sound.tone;

import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Once configured with objects and periods, this object will provide object
 * relevant for the requested "current" sample time, in nanoseconds. Assumes
 * that the argument to {@link #forTime(long)} does never decrease, i.e. callers
 * are interested in "current" or "next" object, but not "previous".
 */
public class OverTimeSelector<T> {
  private long[] switchPoints;
  private T[] objects;
  private int currentIndex;

  private OverTimeSelector(long[] switchPoints, T[] objects) {
    this.switchPoints = switchPoints;
    this.objects = objects;
  }


  public T forTime(long sampleTime) {
    while (currentIndex < switchPoints.length && switchPoints[currentIndex] < sampleTime) {
      currentIndex++;
    }

    return objects[currentIndex];
  }



  public static <T> Builder<T> startWith(T initial) {
    return new Builder<T>(initial);
  }

  public static class Builder<T> {
    private Map<Long, T> objectMap = new TreeMap<>();
    private T initialTone;

    private Builder(T initialTone) {
      this.initialTone = initialTone;
    }

    public Builder<T> at(Duration duration, T object) {
      objectMap.put(duration.toNanos(), object);
      return this;
    }

    public OverTimeSelector<T> build() {
      long[] switchPoints = new long[objectMap.size()];
      @SuppressWarnings("unchecked")
      T[] objects = (T[]) new Object[objectMap.size() + 1];

      objects[0] = initialTone;

      int i = 0;
      for (Entry<Long, T> entry : objectMap.entrySet()) {
        switchPoints[i] = entry.getKey();
        objects[++i] = entry.getValue();
      }

      return new OverTimeSelector<T>(switchPoints, objects);
    }
  }

}
