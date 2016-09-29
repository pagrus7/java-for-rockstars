package org.pagrus.sound.ui;

import java.util.function.Consumer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RegularStyle implements Consumer<Canvas>{

  @Override
  public void accept(Canvas canvas) {
    canvas.setEffect(null);
    GraphicsContext g = canvas.getGraphicsContext2D();
    g.setFill(Color.BLACK);
    g.setStroke(Color.WHITE);
  }

}
