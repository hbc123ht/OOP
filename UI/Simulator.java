package UI;

import edu.uci.ics.jung.visualization.VisualizationImageServer; 

import org.apache.commons.collections15.Transformer;
import java.awt.*;
import java.awt.image.*;
import java.nio.file.StandardCopyOption;
import java.awt.BorderLayout;
import java.awt.geom.Point2D;
import java.util.concurrent.TimeUnit;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;
import javax.swing.BorderFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.*; 

import edu.uci.ics.jung.graph.*;
import Panels.*;

public class Simulator extends JFrame {

    public int Simulate(int n, LinkedList<Integer>[] Edge){ 
  
        JPanel Model = new JPanel();
            
        // create Image panel

        ImagePanel ImageLabel = new ImagePanel();
        ImageLabel.init(n, Edge);
        // ImagePanel.setPreferredSize(new Dimension(630, 630));
        VisualizationViewer<Integer, String> vv = ImageLabel.vv;
        // add Image to Model
        
        // add model to frame
        JFrame frame = new JFrame();

        // create zoom Button
        JPanel Zoom= new JPanel();
        JButton Zoomin = new JButton(" + ");
        double scale = 1.0;
        Zoomin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImageLabel.zoomin();
                Model.invalidate();
                Model.repaint();
            }
        });

        JButton Zoomout = new JButton(" - ");
        Zoomout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImageLabel.zoomout();
                Model.invalidate();
                Model.repaint();
            }
        });
        Zoom.add(Zoomin);
        Zoom.add(Zoomout);
        //create trace list

        JTextArea traces = new JTextArea();
        JScrollPane traces_with_scroll = new JScrollPane(traces);

        traces_with_scroll.setPreferredSize(new Dimension(100, 500));
        // create Choice of next vertex

        Choice choice=new Choice();  
        choice.setBounds(100,100, 75,75);

        choice.add("1");

       
        // Go: go to the chosen vertex

        JButton Go = new JButton("Go");
        Go.addActionListener(new ActionListener() {
            int nxt = 0, cur = 1;
            String path = "1";
            int [][] passed = new int[500][500];
            public void actionPerformed(ActionEvent e) {
                //get the chosen one
                this.nxt = Integer.parseInt(choice.getItem(choice.getSelectedIndex()));
                // mark the path passed
                passed[this.cur][this.nxt] = 1;
                if (this.cur != this.nxt) traces.append(this.cur + " ---> " + this.nxt + '\n');
                this.cur = this.nxt;
                // repaint the image
                ImageLabel.repaint(this.cur, passed);
                // repaint image
                frame.invalidate();
                frame.repaint();
                // update the choices for next step
                choice.removeAll();
                Integer [] adj_array = new Integer [Edge[this.cur].size()];
                for (int i = 0;i < Edge[this.cur].size(); i++){
                    choice.add(Edge[this.cur].get(i).toString());
                }
            }  
        });

        JPanel function = new JPanel();
        function.setPreferredSize(new Dimension(200, 480));
        function.setLayout(null);

        // Go step by step Panel
        JPanel step = new JPanel();
        TitledBorder border_step = BorderFactory.createTitledBorder("Go step by step");
        step.setBorder(border_step);
        step.setBounds(0, 10, 200, 70);
        choice.setBounds(10, 10, 70, 50);
        Go.setBounds(100, 19, 50, 30);
        step.add(choice);
        step.add(Go);

        // auto find path Panel
        JPanel find_path = new JPanel();
        TitledBorder border_find_path = BorderFactory.createTitledBorder("Run all");
        find_path.setBorder(border_find_path);
        find_path.setBounds(0, 90, 200, 130);

            // begin: choose vertex from begining to destination
        JTextField begin_text = new JTextField(6);
        JTextField end_text = new JTextField(6);
 
        find_path.add(begin_text);
        find_path.add(end_text);
        
        // run button
        JButton run = new JButton("Run");

        // Create Thread to perform BFS to find Path
        class Run implements Runnable{  
            private int exit = 1;
            
            public void start_process(){
                if (exit == 0) return;
                Thread t = new Thread(this);
                exit = 0;
                t.start();
                
            }
            public void stop_process(){
                exit = 1;
            }
            public void clear_traces(){
                traces.setText("");
            }
            public static void wait(int ms)
            {
                try
                {
                    Thread.sleep(ms);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
            }
            public void update(int u, int v){
                traces.append(u + " ---> " + v + '\n');
                // traces_with_scroll.paintImmediately(traces_with_scroll.getBounds());
                traces.setCaretPosition(traces.getDocument().getLength());
                traces_with_scroll.repaint();
                traces_with_scroll.revalidate();
            }
            public void run(){  
                int begin = Integer.parseInt(begin_text.getText());
                int des = Integer.parseInt(end_text.getText());
                //empty traces
                clear_traces();
                // BFS
                int[] p = new int[10000];
                int left = 1;
                int right = 0;
                Queue<Integer> q = new LinkedList<>();
                q.add(begin);
                while(q.size() > 0){
                    int u = q.remove();
                    for (int i = 0;i < Edge[u].size(); i++){
                        if (exit == 1) return;
                        int v = Edge[u].get(i);
                        if (p[v] == 1) continue;
                        wait(100);
                        p[v] = 1;
                        q.add(v);
                        update(u, v);
                        if (v == des){
                            exit = 1;
                            return;
                        }
                    }
                }
                exit = 1;
            }  
        }  


        Run tmp = new Run();
        class MyActionListener implements ActionListener {
            
            public void actionPerformed(ActionEvent e) {
                tmp.start_process();
            }
        }
        run.addActionListener(new MyActionListener());

        //stop button
        JButton stop = new JButton("Stop");
        stop.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                tmp.stop_process();
            } 
          } );
          //clear traces button
        JButton clear_traces = new JButton("Clear log");
        clear_traces.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                tmp.clear_traces();
            } 
        });
        //create save button
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent evt) {
        			try {
        				ImageLabel.save();
        			}
        			catch(Exception e) {
        				
        			}
        		}
        });
        
        //create save panel
        JPanel save_image = new JPanel();
        TitledBorder border_save_image = BorderFactory.createTitledBorder("Save image");
        save_image.setBorder(border_save_image);
        save_image.setBounds(0, 240, 200, 100);
        
        save.setBounds(100, 100, 50, 50);
        
        //add save button to save panel
        save_image.add(save);

        // add run and stop button to run panel
        find_path.add(run);
        find_path.add(stop);
        find_path.add(clear_traces);
        // add to function
        function.add(step);
        function.add(find_path);
        function.add(save_image);
        
        GraphZoomScrollPane ImagePanel = new GraphZoomScrollPane(ImageLabel.vv);
        frame.add(ImagePanel, BorderLayout.CENTER);
        // frame.add(Zoom,BorderLayout.SOUTH);
        frame.add(traces_with_scroll, BorderLayout.WEST);
        frame.add(function,BorderLayout.EAST);

        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Go.doClick();
        return 5;
    } 

}