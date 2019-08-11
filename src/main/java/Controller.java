import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Controller {
    //private String color = "0123456789abcdef";
    private String R = "fffffffffffffffffffffe";
    private String G = "fffffffffffffffffffffe";
    private String B = "fffffffffffffffffffffe";
    public static final int SCENE_SIZE = 800;
    private static final Random r = new Random();
    private Shapes shapes;
    private final Group circles = new Group();
    private float[] fade = {0.9f};
    Thread thread = null;
    Thread music = null;
    @FXML
    private GridPane grid;
    @FXML
    private Pane pane;
    @FXML
    private Button run;
    @FXML
    private TextField tR;
    @FXML
    private TextField tG;
    @FXML
    private TextField tB;
    @FXML
    private Slider slide;
    @FXML
    private TextField textSlide;
    @FXML
    private TextField tSize;
    private double peak;
    private double rms;
    private double X;
    private double Y;

    @FXML
    private void initialize() {
        shapes = new Shapes();
        shapes.initShape();
        shapes.getShapes();
        shapes.initCircles();

        pane.getChildren().add(circles);
        circles.setTranslateX(-50);
        circles.setTranslateY(-50);
        slide.valueProperty().addListener((observable, oldValue, newValue) -> {

            textSlide.setText(Double.toString(newValue.intValue()));


        });
        pane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                X = event.getX();
                Y = event.getY();
                String msg =
                        "(x: " + event.getX() + ", y: " + event.getY() + ") -- " +
                                "(sceneX: " + event.getSceneX() + ", sceneY: " + event.getSceneY() + ") -- " +
                                "(screenX: " + event.getScreenX() + ", screenY: " + event.getScreenY() + ")";
            }
        });
    }

    public void draw() {
        final Timeline animation = new Timeline(
                new KeyFrame(Duration.seconds(.03), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        for (int i = 0; i < circles.getChildren().size(); i++) {
                            double currentRms = rms;
                            double currentPeak = peak;
                            circles.getChildren().get(i).setTranslateY(circles.getChildren().get(i).getTranslateY() + Math.sqrt(currentRms) * 15);
                            circles.getChildren().get(i).setTranslateX(circles.getChildren().get(i).getTranslateX() + Math.random() * 0.2);
                            circles.getChildren().get(i).setOpacity(circles.getChildren().get(i).opacityProperty().doubleValue() + currentPeak * 3 - currentPeak * 3.05); /*circles.getChildren().get(i).opacityProperty().doubleValue() - Math.random() * 0.01 + */
                            if (circles.getChildren().get(i).getScaleX() >= 0) {
                                circles.getChildren().get(i).setScaleX(circles.getChildren().get(i).getScaleX() + currentPeak * 1 - currentPeak * 1.01);
                                circles.getChildren().get(i).setScaleY(circles.getChildren().get(i).getScaleY() + currentPeak * 1 - currentPeak * 1.01);
                            }

                            double width = circles.getChildren().get(i).getBoundsInParent().getMaxX() - circles.getChildren().get(i).getBoundsInParent().getMinX();
                            double height = circles.getChildren().get(i).getBoundsInParent().getMaxY() - circles.getChildren().get(i).getBoundsInParent().getMinY();
                            // TODO: Мыщка
/*                            double temp = -(X - circles.getChildren().get(i).getBoundsInParent().getMinX() - circles.getChildren().get(i).getScene().getX());
                            double tempY = -(Y - circles.getChildren().get(i).getBoundsInParent().getMinY() - circles.getChildren().get(i).getScene().getY());
                            if ((temp <= width && temp >= -width / 4) || (tempY <= height && tempY >= - height/4)) {
//ЛОГИКА
                                if((temp <= width && temp >= -width / 4)) {
                                    circles.getChildren().get(i).setTranslateX(circles.getChildren().get(i).getTranslateX() + 5);
                                }
                                if((tempY <= height && tempY >= - height/4)) {
                                    circles.getChildren().get(i).setTranslateY(circles.getChildren().get(i).getTranslateY() + 5);
                                }
                            }*/
                            /*double temp2 = (X - (circles.getChildren().get(i).getBoundsInParent().getMinX()  + circles.getChildren().get(i).getScene().getX()));
                            if (temp2 <= width/1.5 && temp2 >= -width/3) {
                                circles.getChildren().get(i).setTranslateX(circles.getChildren().get(i).getTranslateX() - 5);
                            }*/
                            circles.getChildren().get(i).setOpacity(circles.getChildren().get(i).opacityProperty().doubleValue() + peak * 0.02);
                        }
                        long tm = (System.nanoTime() / 10000000);
                        if (tm % 4 == 0) {
                            //
                            Shapes.size = (int) (Double.valueOf(tSize.getText()) / 20);
                            if (circles.getChildren().size() < Double.valueOf(tSize.getText())) {
                                shapes.initShape();
                                shapes.getShapes();
                                shapes.initCircles();
//                                System.out.println("Круги созданы");
                                newCircles();
//                                System.out.println(circles.getChildren().size());
                            }
                        }
//                        if (tm % 1 == 0) {
//                        System.out.println(circles.getChildren().size());
                        for (int i = 0; i < circles.getChildren().size(); i++) {
                            if (circles.getChildren().get(i).opacityProperty().doubleValue() < 0.01 || circles.getChildren().get(i).getScaleY() < 0.03 || circles.getChildren().get(i).getTranslateX() > 1080) {
                                circles.getChildren().remove(i);
                            }
//                            System.out.println(circles.getChildren().size());
                        }
//                        }
                    }

                }));

        animation.setCycleCount(animation.INDEFINITE);
        animation.play();

    }

    public void fallDown() {

    }

    public void newCircles() {
        ArrayList<Circle> arrCircles = new ArrayList<>();
        double max = 0;
        double i = 0;
        for (Shape sh : shapes.getShapes()) {
            i += 0.01;
            double[] arr = sh.getCoord();
            Circle circle = new Circle((Math.random() + 1) * 20 * (1 + arr[1]), (1 + Math.random()) * arr[2] * 10, ((1 - peak * 0.5) * 30) * ((Math.random()) + Double.valueOf(textSlide.getText()) / 2));
            if (circle.getRadius() > max) max = circle.getRadius();
//            if(circle.getRadius() != max) {
            DropShadow shadow1 = new DropShadow();
            shadow1.setOffsetX(0.1);
            shadow1.setOffsetY(0.1);
            shadow1.setColor(Color.color(1, 1, 1));
            circle.setEffect(shadow1);
            circle.setOpacity(peak);
//            }
            circle.setFill(Paint.valueOf("#" + R.charAt((int) (Math.random() * R.length())) + R.charAt((int) (Math.random() * R.length())) + G.charAt((int) (Math.random() * G.length())) + G.charAt((int) (Math.random() * G.length())) + B.charAt((int) (Math.random() * B.length())) + B.charAt((int) (Math.random() * B.length()))));
            arrCircles.add(circle);
        }
        circles.getChildren().addAll(arrCircles);
        /*for (int j=0; j<circles.getChildren().size();j++) {
            circles.getChildren().get(j).setTranslateX(-pane.getParent().getScene().getWidth()/3.2);
        }*/
    }

    @FXML
    public void begin(ActionEvent event) {
        R = tR.getText();
        G = tG.getText();
        B = tB.getText();
        if (thread == null) {
            music = new Thread(new Recorder(this));
            thread = new Thread(new Animation(this));
            music.start();
            thread.start();
        }
        shapes.initShape();
        shapes.getShapes();
        shapes.initCircles();
        newCircles();
    }

    public float getFade() {
        return fade[0];
    }

    public void setPeak(double peak) {
        this.peak = peak;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }
}
