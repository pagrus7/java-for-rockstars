package org.pagrus.sound.ui;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.pagrus.sound.SoundProcessor;
import org.pagrus.sound.plumbing.SoundSystem;

public class MainApp extends Application {
  private static final float[] GRID_LINES = new float[] {0, 0.1f, .2f, .3f,.4f, .5f, .6f, .7f, .8f, .9f};
  private SoundSystem soundSystem = SoundSystem.get();
  private SoundProcessor soundProcessor;
  private ToggleButton startButton;
  private Canvas canvas;

  @Override
  public void start(Stage stage) throws Exception {
    stage.setFullScreen(true);
    stage.setTitle("Oscilloscope");
    StackPane root = new StackPane();
    stage.setScene(new Scene(root));

    startButton = new ToggleButton("Start");
    startButton.setPrefWidth(300);
    startButton.setOnAction(e -> startButtonClicked());

    soundProcessor = new SoundProcessor();
    soundProcessor.setSampleSniffer(new RenderingSniffer());

    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    canvas = new Canvas(screenBounds.getWidth(), screenBounds.getHeight() * .95);

    BorderPane borderPane = new BorderPane(canvas);
    borderPane.setBottom(startButton);
    BorderPane.setAlignment(startButton, Pos.CENTER);

    root.getChildren().add(borderPane);

    stage.show();
  }

  private void refreshChart(double[] samples) {
    GraphicsContext g = canvas.getGraphicsContext2D();
    double w = canvas.getWidth();
    double h = canvas.getHeight();

    double xStep = w / samples.length;

    g.setFill(Color.WHITE);
    g.fillRect(0, 0, w, h);

    g.setStroke(Color.GREEN);
    g.setLineWidth(5);

    double x = 0;
    for (double s : samples) {
      g.strokeLine(x, h / 2, x, h / 2 * (1 + s));
      x += xStep;
    }

    drawHorizontalGrid(g, w, h);
  }

  private void drawHorizontalGrid(GraphicsContext g, double w, double h) {
    g.setStroke(Color.GRAY);
    g.setLineWidth(2);

    for (float f : GRID_LINES) {
      double dy = f * h/2;
      g.strokeLine(0, h/2 + dy, w, h/2 + dy);
      g.strokeText(String.valueOf(f), 10, h/2 + dy - 5);
      g.strokeLine(0, h/2 - dy, w, h/2 - dy);
      g.strokeText(String.valueOf(f), 10, h/2 - dy - 5);
    }
  }

  private void startButtonClicked() {
    if (startButton.isSelected()) {
      soundSystem.start(soundProcessor);
    } else {
      soundSystem.stop();
    }
  }

  @Override
  public void stop() throws Exception {
    soundSystem.terminate();
  }

  private class RenderingSniffer implements Consumer<double[]> {
    private final long REFRESH_INTERVAL = TimeUnit.MILLISECONDS.toNanos(40);
    private long lastSniffedTime;

    @Override
    public void accept(double[] values) {
      long nanoTime = System.nanoTime();
      if (nanoTime > lastSniffedTime + REFRESH_INTERVAL) {
        Platform.runLater(() -> refreshChart(values));
        lastSniffedTime = nanoTime;
      }
    }

  }

}
