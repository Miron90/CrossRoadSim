package federates.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;


public class AWT {

    MyFrame myFrame;
    Map<Integer, Color> trafficColor=new HashMap<>();

    ArrayList<Car> cars = new ArrayList<Car>();

    public AWT(){
        trafficColor.put(1,Color.GREEN);
        trafficColor.put(2,Color.RED);
        trafficColor.put(3,Color.GREEN);
        trafficColor.put(4,Color.RED);
        myFrame = new MyFrame();
    }

    public void changeLight(int id){
        myFrame.changeTrafficLight(id);
    }

    int width=1200;
    int height=1200;
    int roadWidth=150;
    int pivot=10;



    public class MyFrame extends Frame{

        MyCanvas myCanvas = new MyCanvas();
        public MyFrame(){
            Button b = new Button("Click Here");
            b.setBounds(50,100,80,30);
            b.addActionListener(new ActionListener() {
                public void actionPerformed (ActionEvent e) {
                    for(int i=0;i<=trafficColor.size();i++) {
                        changeTrafficLight(i);
                    }
                }
            });
            this.add(b);
            setSize(width,height);
            setResizable(false);
            drawRoads();
            super.paintComponents(this.getGraphics());
            setVisible(true);
        }
        public void drawRoads(){
            this.add(myCanvas);
        }
        public void changeTrafficLight(int id){
            myCanvas.changeTrafficLight(id, this.getGraphics());
        }

    }
    class MyCanvas extends JComponent {

        int paramx1=(width-roadWidth)/2;
        int paramx2=paramx1+roadWidth;

        int paramy1=(height-roadWidth)/2;
        int paramy2=paramy1+roadWidth;


        public void paint(Graphics g)
        {
            super.paint(g);
            g.drawLine(paramx1, 0, paramx1, paramy1);
            g.drawLine(paramx2, 0, paramx2, paramy1);
            g.drawLine(paramx1, paramy2, paramx1, height);
            g.drawLine(paramx2, paramy2, paramx2, height);

            g.drawLine(0, paramy1, paramx1, paramy1);
            g.drawLine(paramx2, paramy1, width, paramy1);
            g.drawLine(0, paramy2, paramx1, paramy2);
            g.drawLine(paramx2, paramy2, width, paramy2);

            g.drawLine(paramx2+pivot, paramy2+3*pivot, paramx2+pivot, paramy2+pivot);
            g.drawLine(paramx2+3*pivot, paramy1-pivot, paramx2+pivot, paramy1-pivot);
            g.drawLine(paramx1-pivot, paramy2+pivot, paramx1-3*pivot, paramy2+pivot);
            g.drawLine(paramx1-pivot, paramy1-pivot, paramx1-pivot, paramy1-3*pivot);

            drawSubLines(g);

            for(int i=0;i<=trafficColor.size();i++) {
                drawTrafficLight(i,g);
            }
            for(int i=0;i<cars.size();i++) {
                drawCar(g, cars.get(i));
            }
        }

        private void drawSubLines(Graphics g) {

            int paramXHalfRoad=paramx1+roadWidth/2;
            int paramYHalfRoad=paramy1+roadWidth/2;

            for(int i=0;i<paramy1-20;i=i+20){
                g.drawLine(paramXHalfRoad, i, paramXHalfRoad, i+10);
            }
            for(int i=paramy2;i<height;i=i+20){
                g.drawLine(paramXHalfRoad, i, paramXHalfRoad, i+10);
            }
            for(int i=0;i<paramx1-20;i=i+20){
                g.drawLine(i, paramYHalfRoad, i+10, paramYHalfRoad);
            }
            for(int i=paramx2;i<width;i=i+20){
                g.drawLine(i, paramYHalfRoad, i+10, paramYHalfRoad);
            }
        }

        public void drawTrafficLight(int id, Graphics g) {
            if(trafficColor.get(id)==Color.RED){
                switch (id){
                    case 1:
                        drawTraffic(g, Color.GREEN, paramx1-pivot-7, paramx1-pivot-7, paramy1+14-pivot-4, paramy1-pivot-4);
                        break;
                    case 2:
                        drawTraffic(g, Color.GREEN, paramx2+pivot-14-8, paramx2+pivot-8, paramy1-pivot-7, paramy1-pivot-7);
                        break;
                    case 3:
                        drawTraffic(g, Color.GREEN, paramx2+pivot-7, paramx2+pivot-7, paramy2-pivot-14+9, paramy2-pivot+9);
                        break;
                    case 4:
                        drawTraffic(g, Color.GREEN, paramx1-pivot+14-4, paramx1-pivot-4, paramy2+pivot-7, paramy2+pivot-7);
                        break;
                }
            }else{
                switch (id){
                    case 1:
                        drawTraffic(g, Color.RED, paramx1-pivot-7, paramx1-pivot-7, paramy1-pivot-4, paramy1+14-pivot-4);
                        break;
                    case 2:
                        drawTraffic(g, Color.RED, paramx2+pivot-8, paramx2+pivot-14-8, paramy1-pivot-7, paramy1-pivot-7);
                        break;
                    case 3:
                        drawTraffic(g, Color.RED, paramx2+pivot-7, paramx2+pivot-7, paramy2-pivot+9, paramy2-pivot-14+9);
                        break;
                    case 4:
                        drawTraffic(g, Color.RED, paramx1-pivot-4, paramx1-pivot+14-4, paramy2+pivot-7, paramy2+pivot-7);
                        break;
                }
            }

        }

        public void changeTrafficLight(int id, Graphics g) {
            if(trafficColor.get(id)==Color.RED){
                switch (id){
                    case 1:
                        drawTraffic(g, Color.GREEN, paramx1-pivot+1, paramx1-pivot+1, paramy1+14+pivot+7, paramy1+pivot+7);
                        trafficColor.put(id,Color.GREEN);
                        break;
                    case 2:
                        drawTraffic(g, Color.GREEN, paramx2+pivot-14, paramx2+pivot, paramy1+pivot+4, paramy1+pivot+4);
                        trafficColor.put(id,Color.GREEN);
                        break;
                    case 3:
                        drawTraffic(g, Color.GREEN, paramx2+pivot+1, paramx2+pivot+1, paramy2+3*pivot-14, paramy2+3*pivot);
                        trafficColor.put(id,Color.GREEN);
                        break;
                    case 4:
                        drawTraffic(g, Color.GREEN, paramx1-pivot+14+4, paramx1-pivot+4, paramy2+3*pivot+4, paramy2+3*pivot+4);
                        trafficColor.put(id,Color.GREEN);
                        break;
                }
            }else{
                switch (id){
                    case 1:
                        drawTraffic(g, Color.RED, paramx1-pivot+1, paramx1-pivot+1, paramy1+pivot+7, paramy1+14+pivot+7);
                        trafficColor.put(id,Color.RED);
                        break;
                    case 2:
                        drawTraffic(g, Color.RED, paramx2+pivot, paramx2+pivot-14, paramy1+pivot+4, paramy1+pivot+4);
                        trafficColor.put(id,Color.RED);
                        break;
                    case 3:
                        drawTraffic(g, Color.RED, paramx2+pivot+1, paramx2+pivot+1, paramy2+3*pivot, paramy2+3*pivot-14);
                        trafficColor.put(id,Color.RED);
                        break;
                    case 4:
                        drawTraffic(g, Color.RED, paramx1-pivot+4, paramx1-pivot+14+4, paramy2+3*pivot+4, paramy2+3*pivot+4);
                        trafficColor.put(id,Color.RED);
                        break;
                }
            }

        }

        public void drawTraffic(Graphics g, Color color, int xpivot1, int xpivot2, int ypivot1, int ypivot2){
            g.setColor(Color.BLACK);
            g.fillRect(xpivot1, ypivot1, 14,14);
            g.setColor(color);
            g.fillRect(xpivot2, ypivot2, 14,14);
            g.setColor(Color.BLACK);
        }


        public void drawsmt(Graphics g){
            g.setColor(Color.BLACK);
            g.fillRect(50, 50, 14,14);
            g.fillRect(67, 67, 14,14);
            g.fillRect(678, 68, 14,14);
            g.fillRect(678, 678, 14,14);
        }

        public void drawCar(Graphics g, Car car) {

            g.setColor(Color.BLACK);
            g.fillOval(car.nowx-5, car.nowy,10,10);
            g.fillOval(car.nowx-5, car.nowy+40,10,10);
            g.fillOval(car.nowx+35, car.nowy,10,10);
            g.fillOval(car.nowx+35, car.nowy+40,10,10);
            g.setColor(Color.BLUE);
            g.fillRect(car.nowx, car.nowy, 40,50);
            //car.nowx+=1;
            car.nowy+=3;
        }
    }
}
class Car{
    int Fromx,Fromy;
    int Tox,Toy;
    int nowx,nowy;

    public Car(int x, int y, int tox, int toy){
        this.Fromy=y;
        this.Fromx=x;
        this.nowy=y;
        this.nowx=x;
        this.Tox=tox;
        this.Toy=toy;
    }
}

