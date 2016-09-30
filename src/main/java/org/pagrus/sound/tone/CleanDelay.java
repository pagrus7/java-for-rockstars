package org.pagrus.sound.tone;

import org.pagrus.sound.effects.Amplifier;
import org.pagrus.sound.effects.Delay;

import java.util.stream.DoubleStream;

public class CleanDelay implements Tone {

    Amplifier preamp = new Amplifier(7);
    Delay delay = new Delay(30, 1d, 0.5);

    @Override
    public DoubleStream with(DoubleStream input) {
        return input.map(preamp::amplify)
                .map(delay::apply);
    }
}
