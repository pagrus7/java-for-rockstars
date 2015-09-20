package org.pagrus.sound.ui;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.pagrus.sound.SoundProcessor;
import org.pagrus.sound.plumbing.SoundSystem;
import org.pagrus.sound.tone.OverTimeSelector;

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
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {
  private SoundSystem soundSystem = SoundSystem.get();
  private SoundProcessor soundProcessor;
  private ToggleButton startButton;
  private Canvas canvas;

  private OverTimeSelector<Consumer<Canvas>> styleSelector;

  public MainApp() {
    RegularStyle regularStyle = new RegularStyle();
    MotionBlurStyle motionBlurStyle = new MotionBlurStyle();
    PerspectiveAndBlurStyle perspectiveAndBlurStyle = new PerspectiveAndBlurStyle();

    styleSelector = OverTimeSelector
        .<Consumer<Canvas>> startWith(regularStyle)
        .thenAt(Duration.parse("PT14.3S"), motionBlurStyle)
        .thenAt(Duration.parse("PT27.7S"), perspectiveAndBlurStyle)
        .build();
  }

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

  private void refreshChart(double[] samples, Long timeNanos) {
    styleSelector.forTime(timeNanos).accept(canvas);

    GraphicsContext g = canvas.getGraphicsContext2D();
    double w = canvas.getWidth();
    double h = canvas.getHeight();

    double xStep = w / samples.length;

    g.fillRect(0, 0, w, h);
    g.setLineWidth(xStep - 1);

    double x = 0;
    for (double s : samples) {
      g.strokeLine(x, h / 2, x, h / 2 * (1 + s));
      x += xStep;
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

  private class RenderingSniffer implements BiConsumer<double[], Long> {
    private final long REFRESH_INTERVAL = TimeUnit.MILLISECONDS.toNanos(40);
    private long lastSniffedTime;

    @Override
    public void accept(double[] values, Long timeNanos) {
      long nanoTime = System.nanoTime();
      if (nanoTime > lastSniffedTime + REFRESH_INTERVAL) {
        Platform.runLater(() -> refreshChart(values, timeNanos));
        lastSniffedTime = nanoTime;
      }
    }

  }

}
