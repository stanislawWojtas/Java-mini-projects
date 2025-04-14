package clock;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.time.LocalTime;

import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_MITER;

public class ClockWithGui extends JPanel{

    LocalTime time = LocalTime.now();
    ClockWithGui(){
        new ClockThread().start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        paintClockFace(g2d);
        paintClockHands(g2d);
    }

    public void paintClockFace(Graphics2D g2d){
        g2d.translate(getWidth()/2,getHeight()/2);
        g2d.setColor(Color.lightGray);
        g2d.drawOval(-140, -140, 280, 280);
        g2d.fillOval(-140, -140, 280, 280);
        g2d.setColor(Color.black);
        g2d.drawOval(-130,-130,260,260);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(-130,-130,260,260);
        g2d.setColor(Color.WHITE);
        for(int i=1;i<13;i++){
            AffineTransform at = new AffineTransform();
            at.rotate(2*Math.PI/12*i);
            Point2D src = new Point2D.Float(0,-120);
            Point2D trg = new Point2D.Float();
            at.transform(src,trg);
            g2d.drawString(Integer.toString(i),(int)trg.getX(),(int)trg.getY());
        }
        //rysowanie linii wskazujących konkretne sekundy/minuty/godziny
        for(int i = 0; i < 60; i++){
            AffineTransform at = new AffineTransform();
            //kąt na tarczy
            double angle = i * 6 * Math.PI/180;
            if(i % 5 != 0){
                int x1 = (int)(Math.cos(angle)*100);
                int y1 = (int)(Math.sin(angle)*100);
                int x2 = (int)(Math.cos(angle)*110);
                int y2 = (int)(Math.sin(angle)*110);

                g2d.setStroke(new BasicStroke(1, CAP_ROUND,JOIN_MITER));
                g2d.setColor(Color.GRAY);
                g2d.drawLine(x1,y1,x2,y2);
                g2d.setColor(Color.WHITE);
            }
            else{
                int x1 = (int)(Math.cos(angle)*90);
                int y1 = (int)(Math.sin(angle)*90);
                int x2 = (int)(Math.cos(angle)*110);
                int y2 = (int)(Math.sin(angle)*110);

                g2d.setStroke(new BasicStroke(3, CAP_ROUND,JOIN_MITER));
                g2d.setColor(Color.lightGray);
                g2d.drawLine(x1,y1,x2,y2);
                g2d.setColor(Color.WHITE);
            }
        }
    }

    public void paintClockHands(Graphics2D g2d){

        //W hours i minutes uwzględniam sekundy i minuty aby strzałki poruszały się płynnie
        //do sekund dodaje nanosekundy dla płynnego przejścia
        double seconds = time.getSecond() + time.getNano() / 1000000000.0;
        double minutes = time.getMinute() + seconds/ 60.0;
        double hours = time.getHour()%12 + minutes / 60.0;

        //wyznaczam kąty dla każdej strzałki
        double hour_angle = (hours/12.0) * 2 * Math.PI;
        double minute_angle = (minutes/60) * 2 * Math.PI;
        double second_angle = (seconds/60) * 2 * Math.PI;

        AffineTransform saveAT = g2d.getTransform();
        //strzałka godzinowa
        g2d.setStroke(new BasicStroke(5, CAP_ROUND,JOIN_MITER));
        g2d.rotate(hour_angle);
        g2d.drawLine(0,0,0,-70);
        g2d.setTransform(saveAT);

        //strzełka minutowa
        g2d.setStroke(new BasicStroke(3, CAP_ROUND,JOIN_MITER));
        g2d.rotate(minute_angle);
        g2d.drawLine(0,0,0,-90);
        g2d.setTransform(saveAT);

        //strzałka sekundowa
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(1, CAP_ROUND,JOIN_MITER));
        g2d.rotate(second_angle);
        g2d.drawLine(0,0,0,-100);
        g2d.setTransform(saveAT);
        g2d.setColor(Color.WHITE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Clock");
        frame.setContentPane(new ClockWithGui());
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    class ClockThread extends Thread{
        @Override
        public void run() {
            while(true){
                time = LocalTime.now();
                try{
                    //sleep taki niski aby strzałka sekundowa płynnie przechodziła
                    sleep(16);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                repaint();
            }
        }
    }
}
