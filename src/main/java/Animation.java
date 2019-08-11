import javafx.application.Platform;

public class Animation implements Runnable {
    Controller ctrl;

    Animation(Controller ctrl)
    {
        this.ctrl = ctrl;
    }
    @Override
    public void run() {
//        while (true) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ctrl.draw();
                    try {
//                        System.out.print("af");
//                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
//        }
    }
}
