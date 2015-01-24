package org.pagrus.sound.ui;

import java.util.function.Consumer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.paint.Color;

public class PerspectiveAndBlurStyle implements Consumer<Canvas>{
  private PerspectiveTransform perspective;
  private GaussianBlur blurWithPerspective;
  int counter;

  public PerspectiveAndBlurStyle() {
    perspective = new PerspectiveTransform();

    blurWithPerspective = new GaussianBlur();
    blurWithPerspective.setInput(perspective);

  }

  @Override
  public void accept(Canvas canvas) {
    perspective.setUrx(canvas.getWidth());
    perspective.setLrx(canvas.getWidth());

    perspective.setUry(canvas.getHeight() * (0.1 + 0.3 * Math.sin(((float) counter) / 50)));
    perspective.setLry(canvas.getHeight() * (0.9 + 0.3 * Math.sin(((float) counter) / 60)));
    
    perspective.setUly(canvas.getHeight() * (0.1 + 0.3 * Math.sin(((float) counter) / 70)));
    perspective.setLly(canvas.getHeight() * (0.9 + 0.3 * Math.sin(((float) counter) / 80)));
    blurWithPerspective.setRadius(15 + 10 * Math.sin(((float) counter++) / 90));
    canvas.setEffect(blurWithPerspective);


    GraphicsContext g = canvas.getGraphicsContext2D();
    g.setFill(Color.BLACK);
    g.setStroke(Color.RED);
  }

}
