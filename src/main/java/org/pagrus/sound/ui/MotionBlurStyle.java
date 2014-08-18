package org.pagrus.sound.ui;

import java.util.function.Consumer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.Color;

public class MotionBlurStyle implements Consumer<Canvas>{
  private MotionBlur motionBlur = new MotionBlur(30, 20);

  @Override
  public void accept(Canvas canvas) {
    GraphicsContext g = canvas.getGraphicsContext2D();

    g.setFill(Color.BLACK);
    g.setStroke(Color.RED);
    canvas.setEffect(motionBlur);
  }

}
