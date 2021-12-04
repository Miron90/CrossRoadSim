package federates.GUI;

import federates.Car.Car;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AWT {

    MyFrame myFrame;
    Map<Integer, Color> trafficColor=new HashMap<>();

    ArrayList<Car> carsOnRoad1 = new ArrayList<>();
    ArrayList<Car> carsOnRoad2 = new ArrayList<>();
    ArrayList<Car> carsOnRoad3 = new ArrayList<>();
    ArrayList<Car> carsOnRoad4 = new ArrayList<>();

    ArrayList<Car> carsOnEnd = new ArrayList<>();
    ArrayList<Car> carsOnCrossRoad = new ArrayList<>();

    ArrayList<Integer> carsOnRoad = new ArrayList<>();


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

    public void drawCar(ArrayList<Car> cars) {
        for (Car car: cars) {
            if(car.getRoadId()==0 && !carsOnRoad1.contains(car) && !carsOnEnd.contains(car))carsOnRoad1.add(car);
            else if (car.getRoadId()==1&& !carsOnRoad2.contains(car)&& !carsOnEnd.contains(car))carsOnRoad2.add(car);
            else if (car.getRoadId()==2&& !carsOnRoad3.contains(car)&& !carsOnEnd.contains(car))carsOnRoad3.add(car);
            else if (car.getRoadId()==3&& !carsOnRoad4.contains(car)&& !carsOnEnd.contains(car))carsOnRoad4.add(car);

        }
    }

    public void setCarsOnRoad(ArrayList<Integer> carOnRoadList) {
        this.carsOnRoad=carOnRoadList;
    }


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
            for(int i=0;i<carsOnRoad1.size();i++) {
                drawCar(g, carsOnRoad1.get(i),i);
            }
            for(int i=0;i<carsOnRoad2.size();i++) {
                drawCar(g, carsOnRoad2.get(i),i);
            }
            for(int i=0;i<carsOnRoad3.size();i++) {
                drawCar(g, carsOnRoad3.get(i),i);
            }
            for(int i=0;i<carsOnRoad4.size();i++) {
                drawCar(g, carsOnRoad4.get(i),i);
            }
            for(int i=0;i<carsOnEnd.size();i++) {
                drawCar(g, carsOnEnd.get(i),i);
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





        public void drawCar(Graphics g, Car car, int carOfNumber) {
            //car.nowx+=1;
            System.out.println(carsOnCrossRoad.size());
            for (int i=0;i<carsOnCrossRoad.size();i++) {
                System.out.println("car is on "+carsOnCrossRoad.get(i).getRoadId());
                System.out.println("car is going to "+carsOnCrossRoad.get(i).getRoadToGo());
                if((carsOnCrossRoad.get(i).getCurrentx()<450 || carsOnCrossRoad.get(i).getCurrentx()>750) || (carsOnCrossRoad.get(i).getCurrenty()<450 || carsOnCrossRoad.get(i).getCurrenty()>750)){
                    carsOnCrossRoad.remove(carsOnCrossRoad.get(i));
                }
            }
            if(car.isOnTraffic()) {
                for (int i = 0; i < carsOnCrossRoad.size(); i++) {
                    switch (car.getRoadId()) {
                        case 0:
                            if (carsOnCrossRoad.get(i).getRoadId() == 1 || (carsOnCrossRoad.get(i).getRoadId() == 2 && carsOnCrossRoad.get(i).getRoadToGo() == 3)) {
                                driveToRoad1(g, car);
                                return;
                            }
                            break;
                        case 1:
                            if (carsOnCrossRoad.get(i).getRoadId() == 2 || (carsOnCrossRoad.get(i).getRoadId() == 3 && carsOnCrossRoad.get(i).getRoadToGo() == 0)) {
                                driveToRoad2(g, car);
                                return;
                            }
                            break;
                        case 2:
                            if (carsOnCrossRoad.get(i).getRoadId() == 3 || (carsOnCrossRoad.get(i).getRoadId() == 0 && carsOnCrossRoad.get(i).getRoadToGo() == 1)) {
                                driveToRoad3(g, car);
                                return;
                            }
                            break;
                        case 3:
                            if (carsOnCrossRoad.get(i).getRoadId() == 0 || (carsOnCrossRoad.get(i).getRoadId() == 1 && carsOnCrossRoad.get(i).getRoadToGo() == 2)) {
                                driveToRoad4(g, car);
                                return;
                            }
                            break;
                    }
                }
            }
            if(car.isAfterTraffic()){
                switch (car.getRoadId()){
                    case 0:
                            if(car.getRoadToGo()==2){
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+6);
                            } else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()>=535){
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                }else{
                                    driveToRoad1(g, car);
                                    car.setCurrenty(car.getCurrenty()+6);
                                }
                            }else if(car.getRoadToGo()==1){
                                    if (car.getCurrenty() >= 610) {
                                        driveToRoad4(g, car);
                                        car.setCurrentx(car.getCurrentx() + 6);
                                    } else {
                                        driveToRoad1(g, car);
                                        car.setCurrenty(car.getCurrenty() + 6);
                                    }
                            }

                        break;
                    case 1:
                            //TODO poinformuj o tym ze juz jedziesz nie jestes w kolejce
                            if(car.getRoadToGo()==3){
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                            } else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()<=610){
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                }
                            }else if(car.getRoadToGo()==2){
                                if(car.getCurrentx()<=535){
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()+6);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                }
                            }
                        break;
                    case 2:
                        if(car.getRoadToGo()==0){
                            driveToRoad3(g, car);
                            car.setCurrenty(car.getCurrenty()-6);
                        } else if(car.getRoadToGo()==1){
                            if(car.getCurrenty()<=610){
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                            }else{
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                            }
                        }else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()<=535){
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                }
                }
                        break;
                    case 3:
                        if(car.getRoadToGo()==1){
                            driveToRoad4(g, car);
                            car.setCurrentx(car.getCurrentx()+6);
                        } else if(car.getRoadToGo()==2){
                            if(car.getCurrentx()>=535){
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+6);
                            }else{
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                            }
                        }else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()>=610){
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                }
                        }
                        break;
                }
            }
            else if(car.isOnTraffic()){
                switch (car.getRoadId()){
                    case 0:
                        if(trafficColor.get(car.getRoadId()+1)==Color.RED){
                            //TODO poinformuj o tym ze juz jedziesz nie jestes w kolejce
                            if(car.getRoadToGo()==2){
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+6);
                                ChangeState(car);
                                carsOnRoad1.remove(car);
                            } else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()>=535){
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    ChangeState(car);
                                    carsOnRoad1.remove(car);
                                }else{
                                    driveToRoad1(g, car);
                                    car.setCurrenty(car.getCurrenty()+6);
                                    ChangeState(car);
                                    carsOnRoad1.remove(car);
                                }
                            }else if(car.getRoadToGo()==1){
                                if(carsOnRoad3.size()>0&&(carsOnRoad3.get(0).getRoadToGo()==0||carsOnRoad3.get(0).getRoadToGo()==1) &&carsOnRoad3.get(0).getCurrenty()<950){
                                    driveToRoad1(g, car);
                                }else{
                                    if(car.getCurrenty()>=610){
                                        driveToRoad4(g, car);
                                        car.setCurrentx(car.getCurrentx()+6);
                                        ChangeState(car);
                                        carsOnRoad1.remove(car);
                                    }else{
                                        driveToRoad1(g, car);
                                        car.setCurrenty(car.getCurrenty()+6);
                                        ChangeState(car);
                                        carsOnRoad1.remove(car);
                                    }
                                }
                            }
                        }else{
                            driveToRoad1(g, car);
                        }
                        break;
                    case 1:
                        if(trafficColor.get(car.getRoadId()+1)==Color.RED){
                            //TODO poinformuj o tym ze juz jedziesz nie jestes w kolejce
                            if(car.getRoadToGo()==3){
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                                ChangeState(car);
                                carsOnRoad2.remove(car);
                            } else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()<=610){
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    ChangeState(car);
                                    carsOnRoad2.remove(car);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    ChangeState(car);
                                    carsOnRoad2.remove(car);
                                }
                            }else if(car.getRoadToGo()==2){
                                if(carsOnRoad4.size()>0&&(carsOnRoad4.get(0).getRoadToGo()==1||carsOnRoad3.get(0).getRoadToGo()==2) &&carsOnRoad3.get(0).getCurrentx()>350){
                                    driveToRoad2(g, car);
                                }else{
                                    if(car.getCurrentx()<=535){
                                        driveToRoad3(g, car);
                                        car.setCurrenty(car.getCurrenty()+6);
                                        ChangeState(car);
                                        carsOnRoad2.remove(car);
                                    }else{
                                        driveToRoad2(g, car);
                                        car.setCurrentx(car.getCurrentx()-6);
                                        ChangeState(car);
                                        carsOnRoad2.remove(car);
                                    }
                                }
                            }
                        }else{
                            driveToRoad2(g, car);
                        }
                        break;
                    case 2:
                        if(trafficColor.get(car.getRoadId()+1)==Color.RED){
                            //TODO poinformuj o tym ze juz jedziesz nie jestes w kolejce
                            if(car.getRoadToGo()==0){
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                                ChangeState(car);
                                carsOnRoad3.remove(car);
                            } else if(car.getRoadToGo()==1){
                                if(car.getCurrenty()<=610){
                                    driveToRoad4(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    ChangeState(car);
                                    carsOnRoad3.remove(car);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    ChangeState(car);
                                    carsOnRoad3.remove(car);
                                }
                            }else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()<=535){
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    ChangeState(car);
                                    carsOnRoad3.remove(car);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    ChangeState(car);
                                    carsOnRoad3.remove(car);
                                }
                            }
                        }else{
                            driveToRoad3(g, car);
                        }
                        break;
                    case 3:
                        if(trafficColor.get(car.getRoadId()+1)==Color.RED){
                            //TODO poinformuj o tym ze juz jedziesz nie jestes w kolejce
                            if(car.getRoadToGo()==1){
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                                ChangeState(car);
                                carsOnRoad4.remove(car);
                            } else if(car.getRoadToGo()==2){
                                if(car.getCurrentx()>=535){
                                    driveToRoad1(g, car);
                                    car.setCurrenty(car.getCurrenty()+6);
                                    ChangeState(car);
                                    carsOnRoad4.remove(car);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    ChangeState(car);
                                    carsOnRoad4.remove(car);
                                }
                            }else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()>=610){
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    ChangeState(car);
                                    carsOnRoad4.remove(car);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    ChangeState(car);
                                    carsOnRoad4.remove(car);
                                }
                            }
                        }else{
                            driveToRoad4(g, car);
                        }
                        break;
                }
            }else {
                switch (car.getRoadId()){
                    case 0:
                        driveToRoad1(g, car);
                        if(carOfNumber!=0 && carsOnRoad1.get(carOfNumber-1).getCurrenty()>carsOnRoad1.get(carOfNumber).getCurrenty()+50+30){
                            car.setCurrenty(car.getCurrenty()+3);
                        }else if (carOfNumber==0) car.setCurrenty(car.getCurrenty()+3);
                        if(car.getCurrenty()>=car.getTrafficy()){
                            car.setOnTraffic(true);
                        }
                        break;
                    case 1:
                        driveToRoad2(g, car);
                        if(carOfNumber!=0 && carsOnRoad2.get(carOfNumber-1).getCurrentx()<carsOnRoad2.get(carOfNumber).getCurrentx()-50-30){
                            car.setCurrentx(car.getCurrentx()-3);
                        }else if (carOfNumber==0) car.setCurrentx(car.getCurrentx()-3);

                        if(car.getCurrentx()<=car.getTrafficx()){
                            car.setOnTraffic(true);
                        }
                        break;
                    case 2:
                        driveToRoad3(g, car);
                        if(carOfNumber!=0 && carsOnRoad3.get(carOfNumber-1).getCurrenty()<carsOnRoad3.get(carOfNumber).getCurrenty()-30-50){
                            car.setCurrenty(car.getCurrenty()-3);
                        }else if (carOfNumber==0) car.setCurrenty(car.getCurrenty()-3);

                        if(car.getCurrenty()<=car.getTrafficy()){
                            car.setOnTraffic(true);
                        }
                        break;
                    case 3:
                        driveToRoad4(g, car);
                        if(carOfNumber!=0 && carsOnRoad4.get(carOfNumber-1).getCurrentx()>carsOnRoad4.get(carOfNumber).getCurrentx()+50+30){
                            car.setCurrentx(car.getCurrentx()+3);
                        }else if (carOfNumber==0) car.setCurrentx(car.getCurrentx()+3);

                        if(car.getCurrentx()>=car.getTrafficx()){
                            car.setOnTraffic(true);
                        }
                        break;
                }
            }
        }
        public void driveToRoad1(Graphics g, Car car){
            g.setColor(Color.BLACK);
            g.fillOval(car.getCurrentx()-5, car.getCurrenty(),10,10);
            g.fillOval(car.getCurrentx()-5, car.getCurrenty()+40,10,10);
            g.fillOval(car.getCurrentx()+35, car.getCurrenty(),10,10);
            g.fillOval(car.getCurrentx()+35, car.getCurrenty()+40,10,10);
            g.setColor(Color.BLUE);
            g.fillRect(car.getCurrentx(), car.getCurrenty(), 40,50);
            g.setColor(Color.YELLOW);
            g.fillRect(car.getCurrentx()+10, car.getCurrenty()+30, 20,10);
            g.setColor(Color.BLACK);
        }
        public void driveToRoad2(Graphics g, Car car){
            g.setColor(Color.BLACK);
            g.fillOval(car.getCurrentx()+5, car.getCurrenty()-5,10,10);
            g.fillOval(car.getCurrentx()+35, car.getCurrenty()-5,10,10);
            g.fillOval(car.getCurrentx()+5, car.getCurrenty()+35,10,10);
            g.fillOval(car.getCurrentx()+35, car.getCurrenty()+35,10,10);
            g.setColor(Color.BLUE);
            g.fillRect(car.getCurrentx(), car.getCurrenty(), 50,40);
            g.setColor(Color.YELLOW);
            g.fillRect(car.getCurrentx()+10, car.getCurrenty()+10, 10,20);
            g.setColor(Color.BLACK);
        }
        public void driveToRoad3(Graphics g, Car car){
            g.setColor(Color.BLACK);
            g.fillOval(car.getCurrentx()-5, car.getCurrenty()+5,10,10);
            g.fillOval(car.getCurrentx()-5, car.getCurrenty()+40,10,10);
            g.fillOval(car.getCurrentx()+35, car.getCurrenty()+5,10,10);
            g.fillOval(car.getCurrentx()+35, car.getCurrenty()+40,10,10);
            g.setColor(Color.BLUE);
            g.fillRect(car.getCurrentx(), car.getCurrenty(), 40,50);
            g.setColor(Color.YELLOW);
            g.fillRect(car.getCurrentx()+10, car.getCurrenty()+10, 20,10);
            g.setColor(Color.BLACK);
            g.setColor(Color.BLACK);
        }
        public void driveToRoad4(Graphics g, Car car){
            g.setColor(Color.BLACK);
            g.fillOval(car.getCurrentx()+5, car.getCurrenty()-5,10,10);
            g.fillOval(car.getCurrentx()+5, car.getCurrenty()+35,10,10);
            g.fillOval(car.getCurrentx()+40, car.getCurrenty()-5,10,10);
            g.fillOval(car.getCurrentx()+40, car.getCurrenty()+35,10,10);
            g.setColor(Color.BLUE);
            g.fillRect(car.getCurrentx(), car.getCurrenty(), 50,40);
            g.setColor(Color.YELLOW);
            g.fillRect(car.getCurrentx()+30, car.getCurrenty()+10, 10,20);
            g.setColor(Color.BLACK);
        }
        public void ChangeState(Car car){
            car.setOnTraffic(false);
            car.setAfterTraffic(true);
            if(car.getRoadId()==0 && car.getRoadToGo()!=3 && !carsOnCrossRoad.contains(car)) {
                carsOnCrossRoad.add(car);
            }
            if(car.getRoadId()-1!=car.getRoadToGo() && car.getRoadId()!=0 && !carsOnCrossRoad.contains(car)) {
                carsOnCrossRoad.add(car);
            }
            if(!carsOnEnd.contains(car)) {
                carsOnEnd.add(car);
            }
        }
    }
}
