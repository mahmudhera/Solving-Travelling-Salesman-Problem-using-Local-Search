package hillclimbing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GUI extends JFrame {

    private JFrame jf = this;
    private Grid grid;
    
    private double scaleFactorX;
    private double scaleFactorY;
    
    private Route r;
    private Position[] cityPositions;
    
    public GUI(Route r, Position[] pos) {
        
        this.r = r;
        this.cityPositions = pos;
        
        setTitle("Solution");
        this.setLayout(new BorderLayout());
        
        double maxX, minX, maxY, minY;
        maxX = minX = pos[0].x;
        maxY = minY = pos[0].y;
        
        for(int i = 1; i < pos.length; i++) {
            if(pos[i].x > maxX) maxX = pos[i].x;
            if(pos[i].x < minX) minX = pos[i].x;
            if(pos[i].y > maxY) maxY = pos[i].y;
            if(pos[i].y < minY) minY = pos[i].y;
        }
        
        for(int i = 0; i < pos.length; i++) {
            this.cityPositions[i].x -= minX;
        }
        
        for(int i = 0; i < pos.length; i++) {
            this.cityPositions[i].y -= minY;
        }
        
        this.setSize(700, 700);
        this.scaleFactorX = 500.0/(maxX - minX);
        this.scaleFactorY = 500.0/(maxY - minY);
    
        grid = new Grid(this.r, this.cityPositions, this.scaleFactorX, this.scaleFactorY);
        add(grid);
    
    }

    
    class Grid extends JPanel {

        private Color c = Color.BLACK;
        private JButton jButton1;
        private JTextArea jTextArea;
        
        private double scaleFactorX;
        private double scaleFactorY;
        
        private Route r;
        private Position[] cityPositions;
        
        public Grid(Route r, Position[] cityPositions, double sfx, double sfy) {
            
            this.scaleFactorX = sfx;
            this.scaleFactorY = sfy;
            this.r = r;
            this.cityPositions = cityPositions;
            
            setLayout(new FlowLayout());
            jButton1 = new JButton("Close");
            add(jButton1);
            repaint();
            
            jTextArea = new JTextArea();
            jTextArea.setText("Cost: " + ((int)(this.r.getDistanceValue()*10000))/10000.0 );
            add(jTextArea);
            repaint();
            
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        
        }

        private void jButton1ActionPerformed(ActionEvent evt) {
            jf.dispose();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            for( int i = 0; i < this.cityPositions.length; i++ ) {
                Position p = this.cityPositions[i];
                g.fillOval((int)(p.x * this.scaleFactorX) - 10 + 100, (int)(p.y * this.scaleFactorY) + 100 - 10, 20, 20);
                g.setColor(Color.BLACK);
                g.drawString("" + (i + 1), (int)(p.x * this.scaleFactorX) - 10 + 100, (int)(p.y * this.scaleFactorY) + 100 - 10);
                g.setColor(Color.RED);
            }
            g.setColor(Color.BLUE);
            for(int i = 0; i < this.r.route.length; i++) {
                int city1Index = this.r.route[i] - 1;
                int city2Index = this.r.route[(i + 1)%(this.r.route.length)] - 1;
                int x1 = (int)(this.cityPositions[city1Index].x * this.scaleFactorX);
                int x2 = (int)(this.cityPositions[city2Index].x * this.scaleFactorX);
                int y1 = (int)(this.cityPositions[city1Index].y * this.scaleFactorY);
                int y2 = (int)(this.cityPositions[city2Index].y * this.scaleFactorY);
                g.drawLine(x1 + 100, y1 + 100, x2 + 100, y2 + 100);
            }
            
        }
    }
}