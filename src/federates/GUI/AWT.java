package federates.GUI;

import federates.Car.Car;
import federates.SpecialCar.SpecialCar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AWT {

    GUIFederate femab;

    MyFrame myFrame;
    Map<Integer, Color> trafficColor=new HashMap<>();

    ArrayList<Car> carsOnRoad1 = new ArrayList<>();
    ArrayList<Car> carsOnRoad2 = new ArrayList<>();
    ArrayList<Car> carsOnRoad3 = new ArrayList<>();
    ArrayList<Car> carsOnRoad4 = new ArrayList<>();

    ArrayList<Car> carsOnEnd = new ArrayList<>();
    ArrayList<Car> carsOnCrossRoad = new ArrayList<>();



    public AWT(GUIFederate femab){
        trafficColor.put(1,Color.GREEN);
        trafficColor.put(2,Color.RED);
        trafficColor.put(3,Color.GREEN);
        trafficColor.put(4,Color.RED);
        this.femab=femab;
        myFrame = new MyFrame();
    }

    public void changeLight(int id){
        myFrame.changeTrafficLight(id);
    }

    int width=1200;
    int height=1200;
    int roadWidth=150;
    int pivot=10;

    public void drawCar() {
        for (Car car: femab.carList) {
            if(!car.isAfterCrossRoad()) {
                if (car.getRoadId() == 0 && !carsOnRoad1.contains(car)  && !car.isAfterTraffic())
                    carsOnRoad1.add(car);
                else if (car.getRoadId() == 1 && !carsOnRoad2.contains(car) && !car.isAfterTraffic())
                    carsOnRoad2.add(car);
                else if (car.getRoadId() == 2 && !carsOnRoad3.contains(car) && !car.isAfterTraffic())
                    carsOnRoad3.add(car);
                else if (car.getRoadId() == 3 && !carsOnRoad4.contains(car) && !car.isAfterTraffic())
                    carsOnRoad4.add(car);
            }
        }
    }




    public class MyFrame extends Frame{

        MyCanvas myCanvas = new MyCanvas();
        public MyFrame(){
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
            for(int i=0;i<femab.specialCarList.size();i++) {
                drawSpecialCar(g, femab.specialCarList.get(i),i);
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
                drawCarToEnd(g, carsOnEnd.get(i),i);
            }
            for(int i=0;i<carsOnCrossRoad.size();i++) {
                drawCar(g, carsOnCrossRoad.get(i),i);
            }

        }

        private void drawSpecialCar(Graphics g, SpecialCar specialCar, int i) {

            if(specialCar.getCurrentx()<-400||specialCar.getCurrentx()>1600||specialCar.getCurrenty()<-400||specialCar.getCurrenty()>1600){
                femab.specialCarList.remove(specialCar);
                return;
            }

                    switch (specialCar.getRoadId()){
                        case 0:
                            if(specialCar.getRoadToGoId()==2){
                                driveSpecialToRoad1(g, specialCar);
                                specialCar.setCurrenty(specialCar.getCurrenty()+10);
                            } else if(specialCar.getRoadToGoId()==3){
                                if(specialCar.getCurrenty()>=580){
                                    driveSpecialToRoad2(g, specialCar);
                                    specialCar.setCurrentx(specialCar.getCurrentx()-10);
                                }else{
                                    driveSpecialToRoad1(g, specialCar);
                                    specialCar.setCurrenty(specialCar.getCurrenty()+10);
                                }
                            }else if(specialCar.getRoadToGoId()==1){
                                if (specialCar.getCurrenty() >= 585) {
                                    driveSpecialToRoad4(g, specialCar);
                                    specialCar.setCurrentx(specialCar.getCurrentx() + 10);
                                } else {
                                    driveSpecialToRoad1(g, specialCar);
                                    specialCar.setCurrenty(specialCar.getCurrenty() + 10);
                                }
                            }

                            break;
                        case 1:
                            if(specialCar.getRoadToGoId()==3){
                                driveSpecialToRoad2(g, specialCar);
                                specialCar.setCurrentx(specialCar.getCurrentx()-10);
                            } else if(specialCar.getRoadToGoId()==0){
                                if(specialCar.getCurrentx()<=585){
                                    driveSpecialToRoad3(g, specialCar);
                                    specialCar.setCurrenty(specialCar.getCurrenty()-10);
                                }else{
                                    driveSpecialToRoad2(g, specialCar);
                                    specialCar.setCurrentx(specialCar.getCurrentx()-10);
                                }
                            }else if(specialCar.getRoadToGoId()==2){
                                if(specialCar.getCurrentx()<=580){
                                    driveSpecialToRoad1(g, specialCar);
                                    specialCar.setCurrenty(specialCar.getCurrenty()+10);
                                }else{
                                    driveSpecialToRoad2(g, specialCar);
                                    specialCar.setCurrentx(specialCar.getCurrentx()-10);
                                }
                            }
                            break;
                        case 2:
                            if(specialCar.getRoadToGoId()==0){
                                driveSpecialToRoad3(g, specialCar);
                                specialCar.setCurrenty(specialCar.getCurrenty()-10);
                            } else if(specialCar.getRoadToGoId()==1){
                                if(specialCar.getCurrenty()<=585){
                                    driveSpecialToRoad4(g, specialCar);
                                    specialCar.setCurrentx(specialCar.getCurrentx()+10);
                                }else{
                                    driveSpecialToRoad3(g, specialCar);
                                    specialCar.setCurrenty(specialCar.getCurrenty()-10);
                                }
                            }else if(specialCar.getRoadToGoId()==3){
                                if(specialCar.getCurrenty()<=580){
                                    driveSpecialToRoad2(g, specialCar);
                                    specialCar.setCurrentx(specialCar.getCurrentx()-10);
                                }else{
                                    driveSpecialToRoad3(g, specialCar);
                                    specialCar.setCurrenty(specialCar.getCurrenty()-10);
                                }
                            }
                            break;
                        case 3:
                            if(specialCar.getRoadToGoId()==1){
                                driveSpecialToRoad4(g, specialCar);
                                specialCar.setCurrentx(specialCar.getCurrentx()+10);
                            } else if(specialCar.getRoadToGoId()==2){
                                if(specialCar.getCurrentx()>=580){
                                    driveSpecialToRoad1(g, specialCar);
                                    specialCar.setCurrenty(specialCar.getCurrenty()+10);
                                }else{
                                    driveSpecialToRoad4(g, specialCar);
                                    specialCar.setCurrentx(specialCar.getCurrentx()+10);
                                }
                            }else if(specialCar.getRoadToGoId()==0){
                                if(specialCar.getCurrentx()>=585){
                                    driveSpecialToRoad3(g, specialCar);
                                    specialCar.setCurrenty(specialCar.getCurrenty()-10);
                                }else{
                                    driveSpecialToRoad4(g, specialCar);
                                    specialCar.setCurrentx(specialCar.getCurrentx()+10);
                                }
                            }
                            break;
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


        public void drawCarToEnd(Graphics g, Car car, int carOfNumber) {


            for (int i=0; i<femab.specialCarList.size();i++){
                if(car.getCarMoved()<8&&((car.getCurrentx()<600+75&&car.getCurrentx()>600-75) && (car.getCurrenty()<600+75&&car.getCurrenty()>600-75))&&((car.getCurrentx()<femab.specialCarList.get(i).getCurrentx()+150&&car.getCurrentx()>femab.specialCarList.get(i).getCurrentx()-150) && (car.getCurrenty()<femab.specialCarList.get(i).getCurrenty()+150&&car.getCurrenty()>femab.specialCarList.get(i).getCurrenty()-150))) {
                    if(car.getCenterx()>525&&car.getCenterx()<600&&car.getCentery()<675&&car.getCentery()>600){//lewwy dolny
                        switch (car.getDriveTo()){
                            case 0:
                                driveToRoad1(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, true,car);
                                break;
                            case 1:
                                driveToRoad2(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, true,car);
                                break;
                            case 2:
                                driveToRoad3(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, true,car);
                                break;
                            case 3:
                                driveToRoad4(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, true,car);
                                break;
                        }
                    }else if(car.getCenterx()>525&&car.getCenterx()<600&&car.getCentery()<600&&car.getCentery()>525){//lewy gorny
                        switch (car.getDriveTo()){
                            case 0:
                                driveToRoad1(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, false,car);
                                break;
                            case 1:
                                driveToRoad2(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, false,car);
                                break;
                            case 2:
                                driveToRoad3(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, false,car);
                                break;
                            case 3:
                                driveToRoad4(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, false,car);
                                break;
                        }
                    }else if(car.getCenterx()>600&&car.getCenterx()<675&&car.getCentery()<600&&car.getCentery()>525){//prawy grony
                        switch (car.getDriveTo()){
                            case 0:
                                driveToRoad1(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, false,car);
                                break;
                            case 1:
                                driveToRoad2(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, false,car);
                                break;
                            case 2:
                                driveToRoad3(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, false,car);
                                break;
                            case 3:
                                driveToRoad4(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, false,car);
                                break;
                        }
                    }else if(car.getCenterx()>600&&car.getCenterx()<675&&car.getCentery()<675&&car.getCentery()>600){//prawy dolny
                        switch (car.getDriveTo()){
                            case 0:
                                driveToRoad1(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, true,car);
                                break;
                            case 1:
                                driveToRoad2(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, true,car);
                                break;
                            case 2:
                                driveToRoad3(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, true,car);
                                break;
                            case 3:
                                driveToRoad4(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, true,car);
                                break;
                        }
                    }
                    car.addCarMoved();
                } else if(car.getCarMoved()<5&&((car.getCurrentx()<femab.specialCarList.get(i).getCurrentx()+150&&car.getCurrentx()>femab.specialCarList.get(i).getCurrentx()-150) && (car.getCurrenty()<femab.specialCarList.get(i).getCurrenty()+150&&car.getCurrenty()>femab.specialCarList.get(i).getCurrenty()-150))){

                    switch (car.getRoadId()){
                        case 0:
                            if(car.getRoadToGo()==2){
                                driveToRoad1(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                                addCor(6,0, false, false,car);
                                car.setDriveTo(0);
                            } else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()>=535){
                                    driveToRoad2(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    addCor(0,6, false, false,car);
                                    car.setDriveTo(1);
                                }else{
                                    driveToRoad1(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    addCor(6,0, false, false,car);
                                    car.setDriveTo(0);
                                }
                            }else if(car.getRoadToGo()==1){
                                if (car.getCurrenty() >= 610) {
                                    driveToRoad4(g, car);
                                    car.setCurrenty(car.getCurrenty() + 6);
                                    addCor(0,6, false, true,car);
                                    car.setDriveTo(3);
                                } else {
                                    driveToRoad1(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    addCor(6,0, false, false,car);
                                    car.setDriveTo(0);
                                }
                            }

                            break;
                        case 1:
                            if(car.getRoadToGo()==3){
                                driveToRoad2(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                                addCor(0,6, false, false,car);
                                car.setDriveTo(1);
                            } else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()<=610){
                                    driveToRoad3(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    addCor(6,0, true, false,car);
                                    car.setDriveTo(2);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    addCor(0,6, false, false,car);
                                    car.setDriveTo(1);
                                }
                            }else if(car.getRoadToGo()==2){
                                if(car.getCurrentx()<=535){
                                    driveToRoad1(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    addCor(6,0, false, false,car);
                                    car.setDriveTo(0);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    addCor(0,6, false, false,car);
                                    car.setDriveTo(1);
                                }
                            }
                            break;
                        case 2:
                            if(car.getRoadToGo()==0){
                                driveToRoad3(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                                addCor(6,0, true, false,car);
                                car.setDriveTo(2);
                            } else if(car.getRoadToGo()==1){
                                if(car.getCurrenty()<=610){
                                    driveToRoad4(g, car);
                                    car.setCurrenty(car.getCurrenty() + 6);
                                    addCor(0,6, false, true,car);
                                    car.setDriveTo(3);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    addCor(6,0, true, false,car);
                                    car.setDriveTo(2);
                                }
                            }else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()<=535){
                                    driveToRoad2(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    addCor(0,6, false, false,car);
                                    car.setDriveTo(1);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    addCor(6,0, true, false,car);
                                    car.setDriveTo(2);
                                }
                            }
                            break;
                        case 3:
                            if(car.getRoadToGo()==1){
                                driveToRoad4(g, car);
                                car.setCurrenty(car.getCurrenty() + 6);
                                addCor(0,6, false, true,car);
                                car.setDriveTo(3);
                            } else if(car.getRoadToGo()==2){
                                if(car.getCurrentx()>=535){
                                    driveToRoad1(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    addCor(6,0, false, false,car);
                                    car.setDriveTo(0);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrenty(car.getCurrenty() + 6);
                                    addCor(0,6, false, true,car);
                                    car.setDriveTo(3);
                                }
                            }else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()>=610){
                                    driveToRoad3(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    addCor(6,0, true, true,car);
                                    car.setDriveTo(2);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrenty(car.getCurrenty() + 6);
                                    addCor(0,6, false, true,car);
                                    car.setDriveTo(3);
                                }
                            }
                            break;
                    }
                        car.addCarMoved();
                    return;
                }else if (((car.getCurrentx()<femab.specialCarList.get(i).getCurrentx()+150&&car.getCurrentx()>femab.specialCarList.get(i).getCurrentx()-150) && (car.getCurrenty()<femab.specialCarList.get(i).getCurrenty()+150&&car.getCurrenty()>femab.specialCarList.get(i).getCurrenty()-150))){
                    switch (car.getDriveTo()){
                        case 0:
                            driveToRoad1(g,car);
                            break;
                        case 1:
                            driveToRoad2(g,car);
                            break;
                        case 2:
                            driveToRoad3(g,car);
                            break;
                        case 3:
                            driveToRoad4(g,car);
                            break;
                    }
                    return;
                }else if(car.getCarMoved()>0){
                    switch (car.getDriveTo()){
                        case 0:
                            driveToRoad1(g,car);
                            if(car.getAddedsignx()[car.getCarMoved()-1]){
                                car.setCurrentx(car.getCurrentx()-car.getAddedx()[car.getCarMoved()-1]);

                            }else{
                                car.setCurrentx(car.getCurrentx()+car.getAddedx()[car.getCarMoved()-1]);
                            }
                            if(car.getAddedsigny()[car.getCarMoved()-1]){
                                car.setCurrenty(car.getCurrenty()-car.getAddedy()[car.getCarMoved()-1]);
                            }else{
                                car.setCurrenty(car.getCurrenty()+car.getAddedy()[car.getCarMoved()-1]);
                            }
                            addCor(0,0,false,false,car);
                            break;
                        case 1:
                            driveToRoad2(g,car);
                            if(car.getAddedsignx()[car.getCarMoved()-1]){
                                car.setCurrentx(car.getCurrentx()-car.getAddedx()[car.getCarMoved()-1]);

                            }else{
                                car.setCurrentx(car.getCurrentx()+car.getAddedx()[car.getCarMoved()-1]);
                            }
                            if(car.getAddedsigny()[car.getCarMoved()-1]){
                                car.setCurrenty(car.getCurrenty()-car.getAddedy()[car.getCarMoved()-1]);
                            }else{
                                car.setCurrenty(car.getCurrenty()+car.getAddedy()[car.getCarMoved()-1]);
                            }
                            addCor(0,0,false,false,car);
                            break;
                        case 2:
                            driveToRoad3(g,car);
                            if(car.getAddedsignx()[car.getCarMoved()-1]){
                                car.setCurrentx(car.getCurrentx()-car.getAddedx()[car.getCarMoved()-1]);

                            }else{
                                car.setCurrentx(car.getCurrentx()+car.getAddedx()[car.getCarMoved()-1]);
                            }
                            if(car.getAddedsigny()[car.getCarMoved()-1]){
                                car.setCurrenty(car.getCurrenty()-car.getAddedy()[car.getCarMoved()-1]);
                            }else{
                                car.setCurrenty(car.getCurrenty()+car.getAddedy()[car.getCarMoved()-1]);
                            }
                            addCor(0,0,false,false,car);
                            break;
                        case 3:
                            driveToRoad4(g,car);
                            if(car.getAddedsignx()[car.getCarMoved()-1]){
                                car.setCurrentx(car.getCurrentx()-car.getAddedx()[car.getCarMoved()-1]);

                            }else{
                                car.setCurrentx(car.getCurrentx()+car.getAddedx()[car.getCarMoved()-1]);
                            }
                            if(car.getAddedsigny()[car.getCarMoved()-1]){
                                car.setCurrenty(car.getCurrenty()-car.getAddedy()[car.getCarMoved()-1]);
                            }else{
                                car.setCurrenty(car.getCurrenty()+car.getAddedy()[car.getCarMoved()-1]);
                            }
                            addCor(0,0,false,false,car);
                            break;
                    }
                    car.subCarMoved();
                    return;
                }
            }
            //car.nowx+=1;
            for (int i=0;i<carsOnCrossRoad.size();i++) {
                if((carsOnCrossRoad.get(i).getCurrentx()<450 || carsOnCrossRoad.get(i).getCurrentx()>750) || (carsOnCrossRoad.get(i).getCurrenty()<450 || carsOnCrossRoad.get(i).getCurrenty()>750)){
                    carsOnEnd.add(carsOnCrossRoad.get(i));
                    carsOnCrossRoad.remove(carsOnCrossRoad.get(i));
                }
            }
            for (int i=0;i<carsOnEnd.size();i++) {
                if((carsOnEnd.get(i).getCurrentx()<-60 || carsOnEnd.get(i).getCurrentx()>1260) || (carsOnEnd.get(i).getCurrenty()<-60 || carsOnEnd.get(i).getCurrenty()>1260)){
                    femab.carList.get(carsOnEnd.get(i).getCarId()).setAfterCrossRoad(true);
                    carsOnEnd.remove(carsOnEnd.get(i));
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
                            car.setDriveTo(0);
                        } else if(car.getRoadToGo()==3){
                            if(car.getCurrenty()>=535){
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                                car.setDriveTo(1);
                            }else{
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+6);
                                car.setDriveTo(0);
                            }
                        }else if(car.getRoadToGo()==1){
                            if (car.getCurrenty() >= 610) {
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx() + 6);
                                car.setDriveTo(3);
                            } else {
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty() + 6);
                                car.setDriveTo(0);
                            }
                        }

                        break;
                    case 1:
                        if(car.getRoadToGo()==3){
                            driveToRoad2(g, car);
                            car.setCurrentx(car.getCurrentx()-6);
                            car.setDriveTo(1);
                        } else if(car.getRoadToGo()==0){
                            if(car.getCurrentx()<=610){
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                                car.setDriveTo(2);
                            }else{
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                                car.setDriveTo(1);
                            }
                        }else if(car.getRoadToGo()==2){
                            if(car.getCurrentx()<=535){
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+6);
                                car.setDriveTo(0);
                            }else{
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                                car.setDriveTo(1);
                            }
                        }
                        break;
                    case 2:
                        if(car.getRoadToGo()==0){
                            driveToRoad3(g, car);
                            car.setCurrenty(car.getCurrenty()-6);
                            car.setDriveTo(2);
                        } else if(car.getRoadToGo()==1){
                            if(car.getCurrenty()<=610){
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                                car.setDriveTo(3);
                            }else{
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                                car.setDriveTo(2);
                            }
                        }else if(car.getRoadToGo()==3){
                            if(car.getCurrenty()<=535){
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                                car.setDriveTo(1);
                            }else{
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                                car.setDriveTo(2);
                            }
                        }
                        break;
                    case 3:
                        if(car.getRoadToGo()==1){
                            driveToRoad4(g, car);
                            car.setCurrentx(car.getCurrentx()+6);
                            car.setDriveTo(3);
                        } else if(car.getRoadToGo()==2){
                            if(car.getCurrentx()>=535){
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+6);
                                car.setDriveTo(0);
                            }else{
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                                car.setDriveTo(3);
                            }
                        }else if(car.getRoadToGo()==0){
                            if(car.getCurrentx()>=610){
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                                car.setDriveTo(2);
                            }else{
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                                car.setDriveTo(3);
                            }
                        }
                        break;
                }
            }

                switch (car.getRoadId()){
                    case 0:
                        if(car.getRoadToGo()==2){
                            driveToRoad1(g, car);
                            car.setCurrenty(car.getCurrenty()+3);
                            car.setDriveTo(0);
                        } else if(car.getRoadToGo()==3){
                            if(car.getCurrenty()>=535){
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-3);
                                car.setDriveTo(1);
                            }else{
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+3);
                                car.setDriveTo(0);
                            }
                        }else if(car.getRoadToGo()==1){
                            if (car.getCurrenty() >= 610) {
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx() + 3);
                                car.setDriveTo(3);
                            } else {
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty() + 3);
                                car.setDriveTo(0);
                            }
                        }

                        break;
                    case 1:
                        if(car.getRoadToGo()==3){
                            driveToRoad2(g, car);
                            car.setCurrentx(car.getCurrentx()-3);
                            car.setDriveTo(1);
                        } else if(car.getRoadToGo()==0){
                            if(car.getCurrentx()<=610){
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-3);
                                car.setDriveTo(2);
                            }else{
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-3);
                                car.setDriveTo(1);
                            }
                        }else if(car.getRoadToGo()==2){
                            if(car.getCurrentx()<=535){
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+3);
                                car.setDriveTo(0);
                            }else{
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-3);
                                car.setDriveTo(1);
                            }
                        }
                        break;
                    case 2:
                        if(car.getRoadToGo()==0){
                            driveToRoad3(g, car);
                            car.setCurrenty(car.getCurrenty()-3);
                            car.setDriveTo(2);
                        } else if(car.getRoadToGo()==1){
                            if(car.getCurrenty()<=610){
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+3);
                                car.setDriveTo(3);
                            }else{
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-3);
                                car.setDriveTo(2);
                            }
                        }else if(car.getRoadToGo()==3){
                            if(car.getCurrenty()<=535){
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-3);
                                car.setDriveTo(1);
                            }else{
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-3);
                                car.setDriveTo(2);
                            }
                        }
                        break;
                    case 3:
                        if(car.getRoadToGo()==1){
                            driveToRoad4(g, car);
                            car.setCurrentx(car.getCurrentx()+3);
                            car.setDriveTo(3);
                        } else if(car.getRoadToGo()==2){
                            if(car.getCurrentx()>=535){
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+3);
                                car.setDriveTo(0);
                            }else{
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+3);
                                car.setDriveTo(3);
                            }
                        }else if(car.getRoadToGo()==0){
                            if(car.getCurrentx()>=610){
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-3);
                                car.setDriveTo(2);
                            }else{
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+3);
                                car.setDriveTo(3);
                            }
                        }
                        break;
                }
        }


        public void drawCar(Graphics g, Car car, int carOfNumber) {

            for (int i=0; i<femab.specialCarList.size();i++){
                if(car.getCarMoved()<8&&((car.getCurrentx()<600+75&&car.getCurrentx()>600-75) && (car.getCurrenty()<600+75&&car.getCurrenty()>600-75))&&((car.getCurrentx()<femab.specialCarList.get(i).getCurrentx()+150&&car.getCurrentx()>femab.specialCarList.get(i).getCurrentx()-150) && (car.getCurrenty()<femab.specialCarList.get(i).getCurrenty()+150&&car.getCurrenty()>femab.specialCarList.get(i).getCurrenty()-150))) {
                    if(car.getCenterx()>525&&car.getCenterx()<600&&car.getCentery()<675&&car.getCentery()>600){//lewwy dolny
                        switch (car.getDriveTo()){
                            case 0:
                                driveToRoad1(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, true,car);
                                break;
                            case 1:
                                driveToRoad2(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, true,car);
                                break;
                            case 2:
                                driveToRoad3(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, true,car);
                                break;
                            case 3:
                                driveToRoad4(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, true,car);
                                break;
                        }
                    }else if(car.getCenterx()>525&&car.getCenterx()<600&&car.getCentery()<600&&car.getCentery()>525){//lewy gorny
                        switch (car.getDriveTo()){
                            case 0:
                                driveToRoad1(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, false,car);
                                break;
                            case 1:
                                driveToRoad2(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, false,car);
                                break;
                            case 2:
                                driveToRoad3(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, false,car);
                                break;
                            case 3:
                                driveToRoad4(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()-8);
                                addCor(8,8, false, false,car);
                                break;
                        }
                    }else if(car.getCenterx()>600&&car.getCenterx()<675&&car.getCentery()<600&&car.getCentery()>525){//prawy grony
                        switch (car.getDriveTo()){
                            case 0:
                                driveToRoad1(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, false,car);
                                break;
                            case 1:
                                driveToRoad2(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, false,car);
                                break;
                            case 2:
                                driveToRoad3(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, false,car);
                                break;
                            case 3:
                                driveToRoad4(g,car);
                                car.setCurrenty(car.getCurrenty()-8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, false,car);
                                break;
                        }
                    }else if(car.getCenterx()>600&&car.getCenterx()<675&&car.getCentery()<675&&car.getCentery()>600){//prawy dolny
                        switch (car.getDriveTo()){
                            case 0:
                                driveToRoad1(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, true,car);
                                break;
                            case 1:
                                driveToRoad2(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, true,car);
                                break;
                            case 2:
                                driveToRoad3(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, true,car);
                                break;
                            case 3:
                                driveToRoad4(g,car);
                                car.setCurrenty(car.getCurrenty()+8);
                                car.setCurrentx(car.getCurrentx()+8);
                                addCor(8,8, true, true,car);
                                break;
                        }
                    }
                    car.addCarMoved();
                } else if(car.getCarMoved()<5&&((car.getCurrentx()<femab.specialCarList.get(i).getCurrentx()+150&&car.getCurrentx()>femab.specialCarList.get(i).getCurrentx()-150) && (car.getCurrenty()<femab.specialCarList.get(i).getCurrenty()+150&&car.getCurrenty()>femab.specialCarList.get(i).getCurrenty()-150))){
                    switch (car.getRoadId()){
                        case 0:
                            if(car.getRoadToGo()==2){
                                driveToRoad1(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                                addCor(6,0, false, false,car);
                                car.setDriveTo(0);
                            } else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()>=535){
                                    driveToRoad2(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    addCor(0,6, false, false,car);
                                    car.setDriveTo(1);
                                }else{
                                    driveToRoad1(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    addCor(6,0, false, false,car);
                                    car.setDriveTo(0);
                                }
                            }else if(car.getRoadToGo()==1){
                                if (car.getCurrenty() >= 610) {
                                    driveToRoad4(g, car);
                                    car.setCurrenty(car.getCurrenty() + 6);
                                    addCor(0,6, false, true,car);
                                    car.setDriveTo(3);
                                } else {
                                    driveToRoad1(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    addCor(6,0, false, false,car);
                                    car.setDriveTo(0);
                                }
                            }

                            break;
                        case 1:
                            if(car.getRoadToGo()==3){
                                driveToRoad2(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                                addCor(0,6, false, false,car);
                                car.setDriveTo(1);
                            } else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()<=610){
                                    driveToRoad3(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    addCor(6,0, true, false,car);
                                    car.setDriveTo(2);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    addCor(0,6, false, false,car);
                                    car.setDriveTo(1);
                                }
                            }else if(car.getRoadToGo()==2){
                                if(car.getCurrentx()<=535){
                                    driveToRoad1(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    addCor(6,0, false, false,car);
                                    car.setDriveTo(0);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    addCor(0,6, false, false,car);
                                    car.setDriveTo(1);
                                }
                            }
                            break;
                        case 2:
                            if(car.getRoadToGo()==0){
                                driveToRoad3(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                                addCor(6,0, true, false,car);
                                car.setDriveTo(2);
                            } else if(car.getRoadToGo()==1){
                                if(car.getCurrenty()<=610){
                                    driveToRoad4(g, car);
                                    car.setCurrenty(car.getCurrenty() + 6);
                                    addCor(0,6, false, true,car);
                                    car.setDriveTo(3);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    addCor(6,0, true, false,car);
                                    car.setDriveTo(2);
                                }
                            }else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()<=535){
                                    driveToRoad2(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    addCor(0,6, false, false,car);
                                    car.setDriveTo(1);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    addCor(6,0, true, false,car);
                                    car.setDriveTo(2);
                                }
                            }
                            break;
                        case 3:
                            if(car.getRoadToGo()==1){
                                driveToRoad4(g, car);
                                car.setCurrenty(car.getCurrenty() + 6);
                                addCor(0,6, false, true,car);
                                car.setDriveTo(3);
                            } else if(car.getRoadToGo()==2){
                                if(car.getCurrentx()>=535){
                                    driveToRoad1(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    addCor(6,0, false, false,car);
                                    car.setDriveTo(0);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrenty(car.getCurrenty() + 6);
                                    addCor(0,6, false, true,car);
                                    car.setDriveTo(3);
                                }
                            }else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()>=610){
                                    driveToRoad3(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    addCor(6,0, true, true,car);
                                    car.setDriveTo(2);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrenty(car.getCurrenty() + 6);
                                    addCor(0,6, false, true,car);
                                    car.setDriveTo(3);
                                }
                            }
                            break;
                    }
                    car.addCarMoved();
                    return;
                }else if (((car.getCurrentx()<femab.specialCarList.get(i).getCurrentx()+150&&car.getCurrentx()>femab.specialCarList.get(i).getCurrentx()-150) && (car.getCurrenty()<femab.specialCarList.get(i).getCurrenty()+150&&car.getCurrenty()>femab.specialCarList.get(i).getCurrenty()-150))){
                    switch (car.getDriveTo()){
                        case 0:
                            driveToRoad1(g,car);
                            break;
                        case 1:
                            driveToRoad2(g,car);
                            break;
                        case 2:
                            driveToRoad3(g,car);
                            break;
                        case 3:
                            driveToRoad4(g,car);
                            break;
                    }
                    return;
                }else if(car.getCarMoved()>0){
                    switch (car.getDriveTo()){
                        case 0:
                            driveToRoad1(g,car);
                            if(car.getAddedsignx()[car.getCarMoved()-1]){
                                car.setCurrentx(car.getCurrentx()-car.getAddedx()[car.getCarMoved()-1]);

                            }else{
                                car.setCurrentx(car.getCurrentx()+car.getAddedx()[car.getCarMoved()-1]);
                            }
                            if(car.getAddedsigny()[car.getCarMoved()-1]){
                                car.setCurrenty(car.getCurrenty()-car.getAddedy()[car.getCarMoved()-1]);
                            }else{
                                car.setCurrenty(car.getCurrenty()+car.getAddedy()[car.getCarMoved()-1]);
                            }
                            addCor(0,0,false,false,car);
                            break;
                        case 1:
                            driveToRoad2(g,car);
                            if(car.getAddedsignx()[car.getCarMoved()-1]){
                                car.setCurrentx(car.getCurrentx()-car.getAddedx()[car.getCarMoved()-1]);

                            }else{
                                car.setCurrentx(car.getCurrentx()+car.getAddedx()[car.getCarMoved()-1]);
                            }
                            if(car.getAddedsigny()[car.getCarMoved()-1]){
                                car.setCurrenty(car.getCurrenty()-car.getAddedy()[car.getCarMoved()-1]);
                            }else{
                                car.setCurrenty(car.getCurrenty()+car.getAddedy()[car.getCarMoved()-1]);
                            }
                            addCor(0,0,false,false,car);
                            break;
                        case 2:
                            driveToRoad3(g,car);
                            if(car.getAddedsignx()[car.getCarMoved()-1]){
                                car.setCurrentx(car.getCurrentx()-car.getAddedx()[car.getCarMoved()-1]);

                            }else{
                                car.setCurrentx(car.getCurrentx()+car.getAddedx()[car.getCarMoved()-1]);
                            }
                            if(car.getAddedsigny()[car.getCarMoved()-1]){
                                car.setCurrenty(car.getCurrenty()-car.getAddedy()[car.getCarMoved()-1]);
                            }else{
                                car.setCurrenty(car.getCurrenty()+car.getAddedy()[car.getCarMoved()-1]);
                            }
                            addCor(0,0,false,false,car);
                            break;
                        case 3:
                            driveToRoad4(g,car);
                            if(car.getAddedsignx()[car.getCarMoved()-1]){
                                car.setCurrentx(car.getCurrentx()-car.getAddedx()[car.getCarMoved()-1]);

                            }else{
                                car.setCurrentx(car.getCurrentx()+car.getAddedx()[car.getCarMoved()-1]);
                            }
                            if(car.getAddedsigny()[car.getCarMoved()-1]){
                                car.setCurrenty(car.getCurrenty()-car.getAddedy()[car.getCarMoved()-1]);
                            }else{
                                car.setCurrenty(car.getCurrenty()+car.getAddedy()[car.getCarMoved()-1]);
                            }
                            addCor(0,0,false,false,car);
                            break;
                    }
                    car.subCarMoved();
                    return;
                }
            }
            //car.nowx+=1;
            for (int i=0;i<carsOnCrossRoad.size();i++) {
                if((carsOnCrossRoad.get(i).getCurrentx()<450 || carsOnCrossRoad.get(i).getCurrentx()>750) || (carsOnCrossRoad.get(i).getCurrenty()<450 || carsOnCrossRoad.get(i).getCurrenty()>750)){
                    carsOnEnd.add(carsOnCrossRoad.get(i));
                    carsOnCrossRoad.remove(carsOnCrossRoad.get(i));
                }
            }
            for (int i=0;i<carsOnEnd.size();i++) {
                if((carsOnEnd.get(i).getCurrentx()<-60 || carsOnEnd.get(i).getCurrentx()>1260) || (carsOnEnd.get(i).getCurrenty()<-60 || carsOnEnd.get(i).getCurrenty()>1260)){
                    femab.carList.get(carsOnEnd.get(i).getCarId()).setAfterCrossRoad(true);
                    carsOnEnd.remove(carsOnEnd.get(i));
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
                                car.setDriveTo(0);
                            } else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()>=535){
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    car.setDriveTo(1);
                                }else{
                                    driveToRoad1(g, car);
                                    car.setCurrenty(car.getCurrenty()+6);
                                    car.setDriveTo(0);
                                }
                            }else if(car.getRoadToGo()==1){
                                    if (car.getCurrenty() >= 610) {
                                        driveToRoad4(g, car);
                                        car.setCurrentx(car.getCurrentx() + 6);
                                        car.setDriveTo(3);
                                    } else {
                                        driveToRoad1(g, car);
                                        car.setCurrenty(car.getCurrenty() + 6);
                                        car.setDriveTo(0);
                                    }
                            }

                        break;
                    case 1:
                            if(car.getRoadToGo()==3){
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                                car.setDriveTo(1);
                            } else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()<=610){
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    car.setDriveTo(2);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    car.setDriveTo(1);
                                }
                            }else if(car.getRoadToGo()==2){
                                if(car.getCurrentx()<=535){
                                    driveToRoad1(g, car);
                                    car.setCurrenty(car.getCurrenty()+6);
                                    car.setDriveTo(0);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    car.setDriveTo(1);
                                }
                            }
                        break;
                    case 2:
                        if(car.getRoadToGo()==0){
                            driveToRoad3(g, car);
                            car.setCurrenty(car.getCurrenty()-6);
                            car.setDriveTo(2);
                        } else if(car.getRoadToGo()==1){
                            if(car.getCurrenty()<=610){
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                                car.setDriveTo(3);
                            }else{
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                                car.setDriveTo(2);
                            }
                        }else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()<=535){
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    car.setDriveTo(1);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    car.setDriveTo(2);
                                }
                }
                        break;
                    case 3:
                        if(car.getRoadToGo()==1){
                            driveToRoad4(g, car);
                            car.setCurrentx(car.getCurrentx()+6);
                            car.setDriveTo(3);
                        } else if(car.getRoadToGo()==2){
                            if(car.getCurrentx()>=535){
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+6);
                                car.setDriveTo(0);
                            }else{
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                                car.setDriveTo(3);
                            }
                        }else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()>=610){
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    car.setDriveTo(2);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    car.setDriveTo(3);
                                }
                        }
                        break;
                }
            }
            else if(car.isOnTraffic()){
                switch (car.getRoadId()){
                    case 0:
                        if(trafficColor.get(car.getRoadId()+1)==Color.RED){
                            if(car.getRoadToGo()==2){
                                driveToRoad1(g, car);
                                car.setCurrenty(car.getCurrenty()+6);
                                car.setDriveTo(0);
                                ChangeState(car);
                                carsOnRoad1.remove(car);
                            } else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()>=535){
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    car.setDriveTo(1);
                                    ChangeState(car);
                                    carsOnRoad1.remove(car);
                                }else{
                                    driveToRoad1(g, car);
                                    car.setCurrenty(car.getCurrenty()+6);
                                    car.setDriveTo(0);
                                    ChangeState(car);
                                    carsOnRoad1.remove(car);
                                }
                            }else if(car.getRoadToGo()==1){
                                    if(car.getCurrenty()>=610){
                                        driveToRoad4(g, car);
                                        car.setCurrentx(car.getCurrentx()+6);
                                        car.setDriveTo(3);
                                        ChangeState(car);
                                        carsOnRoad1.remove(car);
                                    }else{
                                        driveToRoad1(g, car);
                                        car.setCurrenty(car.getCurrenty()+6);
                                        car.setDriveTo(0);
                                        ChangeState(car);
                                        carsOnRoad1.remove(car);
                                    }
                            }
                        }else{
                            driveToRoad1(g, car);
                        }
                        break;
                    case 1:
                        if(trafficColor.get(car.getRoadId()+1)==Color.RED){
                            if(car.getRoadToGo()==3){
                                driveToRoad2(g, car);
                                car.setCurrentx(car.getCurrentx()-6);
                                car.setDriveTo(1);
                                ChangeState(car);
                                carsOnRoad2.remove(car);
                            } else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()<=610){
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    car.setDriveTo(2);
                                    ChangeState(car);
                                    carsOnRoad2.remove(car);
                                }else{
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    car.setDriveTo(1);
                                    ChangeState(car);
                                    carsOnRoad2.remove(car);
                                }
                            }else if(car.getRoadToGo()==2){
                                    if(car.getCurrentx()<=535){
                                        driveToRoad3(g, car);
                                        car.setCurrenty(car.getCurrenty()+6);
                                        car.setDriveTo(2);
                                        ChangeState(car);
                                        carsOnRoad2.remove(car);
                                    }else{
                                        driveToRoad2(g, car);
                                        car.setCurrentx(car.getCurrentx()-6);
                                        car.setDriveTo(1);
                                        ChangeState(car);
                                        carsOnRoad2.remove(car);
                                    }
                            }
                        }else{
                            driveToRoad2(g, car);
                        }
                        break;
                    case 2:
                        if(trafficColor.get(car.getRoadId()+1)==Color.RED){
                            if(car.getRoadToGo()==0){
                                driveToRoad3(g, car);
                                car.setCurrenty(car.getCurrenty()-6);
                                car.setDriveTo(2);
                                ChangeState(car);
                                carsOnRoad3.remove(car);
                            } else if(car.getRoadToGo()==1){
                                if(car.getCurrenty()<=610){
                                    driveToRoad4(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    car.setDriveTo(3);
                                    ChangeState(car);
                                    carsOnRoad3.remove(car);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    car.setDriveTo(2);
                                    ChangeState(car);
                                    carsOnRoad3.remove(car);
                                }
                            }else if(car.getRoadToGo()==3){
                                if(car.getCurrenty()<=535){
                                    driveToRoad2(g, car);
                                    car.setCurrentx(car.getCurrentx()-6);
                                    car.setDriveTo(1);
                                    ChangeState(car);
                                    carsOnRoad3.remove(car);
                                }else{
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    car.setDriveTo(2);
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
                            if(car.getRoadToGo()==1){
                                driveToRoad4(g, car);
                                car.setCurrentx(car.getCurrentx()+6);
                                car.setDriveTo(3);
                                ChangeState(car);
                                carsOnRoad4.remove(car);

                            } else if(car.getRoadToGo()==2){
                                if(car.getCurrentx()>=535){
                                    driveToRoad1(g, car);
                                    car.setCurrenty(car.getCurrenty()+6);
                                    car.setDriveTo(0);
                                    ChangeState(car);
                                    carsOnRoad4.remove(car);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    car.setDriveTo(3);
                                    ChangeState(car);
                                    carsOnRoad4.remove(car);
                                }
                            }else if(car.getRoadToGo()==0){
                                if(car.getCurrentx()>=610){
                                    driveToRoad3(g, car);
                                    car.setCurrenty(car.getCurrenty()-6);
                                    car.setDriveTo(2);
                                    ChangeState(car);
                                    carsOnRoad4.remove(car);
                                }else{
                                    driveToRoad4(g, car);
                                    car.setCurrentx(car.getCurrentx()+6);
                                    car.setDriveTo(3);
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
                            car.setDriveTo(0);
                            femab.carList.get(carsOnRoad1.get(carOfNumber).getCarId()).setInQueue(false);
                            femab.carList.get(carsOnRoad1.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }else if (carOfNumber==0) {
                            car.setCurrenty(car.getCurrenty() + 3);
                        }else if (carsOnRoad1.get(carOfNumber-1).isInQueue()){
                            femab.carList.get(carsOnRoad1.get(carOfNumber).getCarId()).setInQueue(true);
                            femab.carList.get(carsOnRoad1.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }
                        if(car.getCurrenty()>=car.getTrafficy()){
                            car.setOnTraffic(true);
                            femab.carList.get(car.getCarId()).setOnTraffic(true);
                            femab.carList.get(carsOnRoad1.get(carOfNumber).getCarId()).setInQueue(true);
                            femab.carList.get(carsOnRoad1.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }
                        break;
                    case 1:
                        driveToRoad2(g, car);
                        if(carOfNumber!=0 && carsOnRoad2.get(carOfNumber-1).getCurrentx()<carsOnRoad2.get(carOfNumber).getCurrentx()-50-30){
                            car.setCurrentx(car.getCurrentx()-3);
                            car.setDriveTo(1);
                            femab.carList.get(carsOnRoad2.get(carOfNumber).getCarId()).setInQueue(false);
                            femab.carList.get(carsOnRoad2.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }else if (carOfNumber==0) car.setCurrentx(car.getCurrentx()-3);
                        else if (carsOnRoad2.get(carOfNumber-1).isInQueue()){
                            femab.carList.get(carsOnRoad2.get(carOfNumber).getCarId()).setInQueue(true);
                            femab.carList.get(carsOnRoad2.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }
                        if(car.getCurrentx()<=car.getTrafficx()){
                            car.setOnTraffic(true);
                            femab.carList.get(car.getCarId()).setOnTraffic(true);
                            femab.carList.get(carsOnRoad2.get(carOfNumber).getCarId()).setInQueue(true);
                            femab.carList.get(carsOnRoad2.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }
                        break;
                    case 2:
                        driveToRoad3(g, car);
                        if(carOfNumber!=0 && carsOnRoad3.get(carOfNumber-1).getCurrenty()<carsOnRoad3.get(carOfNumber).getCurrenty()-30-50){
                            car.setCurrenty(car.getCurrenty()-3);
                            car.setDriveTo(2);
                            femab.carList.get(carsOnRoad3.get(carOfNumber).getCarId()).setInQueue(false);
                            femab.carList.get(carsOnRoad3.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }else if (carOfNumber==0) car.setCurrenty(car.getCurrenty()-3);
                        else if (carsOnRoad3.get(carOfNumber-1).isInQueue()){
                            femab.carList.get(carsOnRoad3.get(carOfNumber).getCarId()).setInQueue(true);
                            femab.carList.get(carsOnRoad3.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }
                        if(car.getCurrenty()<=car.getTrafficy()){
                            car.setOnTraffic(true);
                            femab.carList.get(car.getCarId()).setOnTraffic(true);
                            femab.carList.get(carsOnRoad3.get(carOfNumber).getCarId()).setInQueue(true);
                            femab.carList.get(carsOnRoad3.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }
                        break;
                    case 3:
                        driveToRoad4(g, car);
                        if(carOfNumber!=0 && carsOnRoad4.get(carOfNumber-1).getCurrentx()>carsOnRoad4.get(carOfNumber).getCurrentx()+50+30){
                            car.setCurrentx(car.getCurrentx()+3);
                            car.setDriveTo(3);
                            femab.carList.get(carsOnRoad4.get(carOfNumber).getCarId()).setInQueue(false);
                            femab.carList.get(carsOnRoad4.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }else if (carOfNumber==0) car.setCurrentx(car.getCurrentx()+3);
                        else if (carsOnRoad4.get(carOfNumber-1).isInQueue()){
                            femab.carList.get(carsOnRoad4.get(carOfNumber).getCarId()).setInQueue(true);
                            femab.carList.get(carsOnRoad4.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                        }
                        if(car.getCurrentx()>=car.getTrafficx()){
                            car.setOnTraffic(true);
                            femab.carList.get(carsOnRoad4.get(carOfNumber).getCarId()).setInQueue(true);
                            femab.carList.get(carsOnRoad4.get(carOfNumber).getCarId()).setWaitTime(femab.getTime());
                            femab.carList.get(car.getCarId()).setOnTraffic(true);
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
            car.setInQueue(false);
            car.setWaitTime(femab.getTime());
            femab.carList.get(car.getCarId()).setInQueue(false);
            femab.carList.get(car.getCarId()).setOnTraffic(false);
            femab.carList.get(car.getCarId()).setWaitTime(femab.getTime());
            car.setAfterTraffic(true);
            femab.carList.get(car.getCarId()).setAfterTraffic(true);
            if(!carsOnCrossRoad.contains(car)) {
                carsOnCrossRoad.add(car);
            }
        }
        public void driveSpecialToRoad1(Graphics g, SpecialCar car){
            g.setColor(Color.BLACK);
            g.fillOval(car.getCurrentx()-3, car.getCurrenty(),6,6);
            g.fillOval(car.getCurrentx()-3, car.getCurrenty()+34,6,6);
            g.fillOval(car.getCurrentx()+27, car.getCurrenty(),6,6);
            g.fillOval(car.getCurrentx()+27, car.getCurrenty()+34,6,6);
            g.setColor(Color.RED);
            g.fillRect(car.getCurrentx(), car.getCurrenty(), 30,40);
            g.setColor(Color.YELLOW);
            g.fillRect(car.getCurrentx()+7, car.getCurrenty()+30, 15,7);
            g.setColor(Color.BLUE);
            if(car.isSpin()) {
                g.setColor(Color.BLUE);
                g.fillRect(car.getCurrentx() + 13, car.getCurrenty() + 18, 3, 6);
                car.setSpin(false);
            }else{
                g.setColor(Color.YELLOW);
                g.fillRect(car.getCurrentx() + 14, car.getCurrenty() + 17, 6, 3);
                car.setSpin(true);
            }
            g.setColor(Color.BLACK);
        }
        public void driveSpecialToRoad2(Graphics g, SpecialCar car){
            g.setColor(Color.BLACK);
            g.fillOval(car.getCurrentx()+3, car.getCurrenty()-3,6,6);
            g.fillOval(car.getCurrentx()+34, car.getCurrenty()-3,6,6);
            g.fillOval(car.getCurrentx()+3, car.getCurrenty()+27,6,6);
            g.fillOval(car.getCurrentx()+34, car.getCurrenty()+27,6,6);
            g.setColor(Color.RED);
            g.fillRect(car.getCurrentx(), car.getCurrenty(), 40,30);
            g.setColor(Color.YELLOW);
            g.fillRect(car.getCurrentx()+3, car.getCurrenty()+7, 7,15);
            if(car.isSpin()) {
                g.setColor(Color.BLUE);
                g.fillRect(car.getCurrentx() + 18, car.getCurrenty() + 13, 3, 6);
                car.setSpin(false);
            }else{
                g.setColor(Color.YELLOW);
                g.fillRect(car.getCurrentx() + 17, car.getCurrenty() + 14, 6, 3);
                car.setSpin(true);
            }
            g.setColor(Color.BLACK);
        }
        public void driveSpecialToRoad3(Graphics g, SpecialCar car){
            g.setColor(Color.BLACK);
            g.fillOval(car.getCurrentx()-3, car.getCurrenty()+3,6,6);
            g.fillOval(car.getCurrentx()-3, car.getCurrenty()+34,6,6);
            g.fillOval(car.getCurrentx()+27, car.getCurrenty()+3,6,6);
            g.fillOval(car.getCurrentx()+27, car.getCurrenty()+34,6,6);
            g.setColor(Color.RED);
            g.fillRect(car.getCurrentx(), car.getCurrenty(), 30,40);
            g.setColor(Color.YELLOW);
            g.fillRect(car.getCurrentx()+7, car.getCurrenty()+3, 15,7);
            if(car.isSpin()) {
                g.setColor(Color.BLUE);
                g.fillRect(car.getCurrentx() + 13, car.getCurrenty() + 18, 3, 6);
                car.setSpin(false);
            }else{
                g.setColor(Color.YELLOW);
                g.fillRect(car.getCurrentx() + 14, car.getCurrenty() + 17, 6, 3);
                car.setSpin(true);
            }
            g.setColor(Color.BLACK);
        }
        public void driveSpecialToRoad4(Graphics g, SpecialCar car){
            g.setColor(Color.BLACK);
            g.fillOval(car.getCurrentx()+3, car.getCurrenty()-3,6,6);
            g.fillOval(car.getCurrentx()+3, car.getCurrenty()+27,6,6);
            g.fillOval(car.getCurrentx()+34, car.getCurrenty()-3,6,6);
            g.fillOval(car.getCurrentx()+34, car.getCurrenty()+27,6,6);
            g.setColor(Color.RED);
            g.fillRect(car.getCurrentx(), car.getCurrenty(), 40,30);
            g.setColor(Color.YELLOW);
            g.fillRect(car.getCurrentx()+30, car.getCurrenty()+7, 7,15);
            if(car.isSpin()) {
                g.setColor(Color.BLUE);
                g.fillRect(car.getCurrentx() + 18, car.getCurrenty() + 13, 3, 6);
                car.setSpin(false);
            }else{
                g.setColor(Color.YELLOW);
                g.fillRect(car.getCurrentx() + 17, car.getCurrenty() + 13, 6, 3);
                car.setSpin(true);
            }
            g.setColor(Color.BLACK);
        }
        public void addCor(int x, int y,boolean xsign, boolean ysign, Car car){
            car.addAddedx(x,car.getCarMoved());
            car.addAddedy(y,car.getCarMoved());
            car.setAddedsignx(car.getCarMoved(),xsign);
            car.setAddedsigny(car.getCarMoved(),ysign);
        }
    }
}
