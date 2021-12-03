package federates.GUI;

import java.util.Timer;
import java.util.TimerTask;

public class MAIN {
    static AWT awt = new AWT();
    public static void main( String[] args ) throws InterruptedException {


//        TimerTask task = new TimerTask() {
//            public void run() {
//                for(int i=0;i<=awt.trafficColor.size();i++) {
//                    awt.changeLight(i);
//                }
//                awt.myFrame.myCanvas.repaint();
//            }
//        };

        int y = 50;
        int x = 610;
        awt.cars.add(new Car(x,y, 1000,610));
        while(true){
            for(int i=0;i<=awt.trafficColor.size();i++) {
                //awt.changeLight(i);
            }
            awt.myFrame.myCanvas.repaint(605,0,50,1200);
            Thread.sleep(5);
            //awt.myFrame.myCanvas.drawCar(awt.myFrame.getGraphics(),x,y);
        }

//        for(int i=0;i<=awt.trafficColor.size();i++) {
//            awt.changeLight(i);
//        }
//        awt.myFrame.myCanvas.repaint();
//        Thread.sleep(500);
//        for(int i=0;i<=awt.trafficColor.size();i++) {
//            awt.changeLight(i);
//        }
//        awt.myFrame.myCanvas.repaint();
//        Thread.sleep(500);
//        for(int i=0;i<=awt.trafficColor.size();i++) {
//            awt.changeLight(i);
//        }
//        awt.myFrame.myCanvas.repaint();
//        Thread.sleep(500);
    }
}
