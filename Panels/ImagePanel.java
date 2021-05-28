package Panels;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import edu.uci.ics.jung.visualization.*;
 
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.control.*;
import java.awt. event.MouseEvent;

public class ImagePanel extends JLabel{

    VisualizationImageServer<Integer, String> vi;
    Graph<Integer, String> graph;
    public VisualizationViewer<Integer, String> vv;

    private double scale = 1.0;
    private BufferedImage image;
    private int w = 10, h = 10;

    public void init(int n, LinkedList<Integer>[] Edge){
        this.graph = new DelegateForest<>();
        // add vertex and add edge
        for(int i = 1;i <= n; i++){
            this.graph.addVertex(i);
        }
        
        for(int i = 1; i <= n; i++) {
            for (int j = 0;j < Edge[i].size(); j++){
                int v = Edge[i].get(j);
                this.graph.addEdge("RAD" + Integer.toString(i) + Integer.toString(v), i, v);
            }
        }

        // Initiate layout
        Layout<Integer, String> layout = new FRLayout<>((Forest<Integer, String>) this.graph);
        this.vv = new VisualizationViewer<>(layout);
        
        // coloring the vertex
        Transformer<Integer, String> transformer = new Transformer<Integer, String>() {
    
            @Override
            public String transform(Integer arg0) {
            return arg0.toString();
            }
    
        };

        this.vv.getRenderContext().setVertexLabelTransformer(transformer);
        PluggableGraphMouse inter = new PluggableGraphMouse();
        inter.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON3_MASK));
        inter.add(new PickingGraphMousePlugin());
        // inter.add(new ViewScalingGraphMousePlugin());
        inter.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));
        this.vv.setGraphMouse(inter);
        
        // final DefaultModalGraphMouse<String, Number> graphMouse3 = new DefaultModalGraphMouse<>();
        // this.vv.setGraphMouse(graphMouse3);
        this.vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);

        int w = (int) this.vv.getGraphLayout().getSize().getWidth() + 30;
        int h = (int)this.vv.getGraphLayout().getSize().getHeight() + 30;

        this.vi = new VisualizationImageServer<Integer, String>(this.vv.getGraphLayout(),new Dimension(w, h));

        this.vi.setBackground(Color.WHITE);
        this.vi.getRenderContext().setVertexLabelTransformer(transformer);

        // create image
        this.image = (BufferedImage) this.vi.getImage(
                new Point2D.Double(w , h),
                new Dimension(w, h));
                
        setIcon(new ImageIcon(this.image));
        this.w = image.getWidth();
        this.h = image.getHeight();
        System.out.print(this.w);
    }
    public void zoomin(){
        this.h = (int) (this.h * 1.1);
        this.w = (int) (this.w * 1.1);
        setIcon(new ImageIcon(this.image.getScaledInstance(this.h, this.w , Image.SCALE_SMOOTH)));
    }
    public void zoomout(){
        this.h = (int) (this.h * 0.9);
        this.w = (int) (this.w * 0.9);
        setIcon(new ImageIcon(this.image.getScaledInstance(this.h ,this.w, Image.SCALE_SMOOTH)));
    }
    public void repaint(int cur, int [][] passed){
        // update vis
        Transformer<String, Paint> colorTransformer = new Transformer<String, Paint>(){
            @Override
            public Paint transform(String arg0)
            {
                final int s = graph.getSource(arg0);
                final int d = graph.getDest(arg0);  
                 if (passed[s][d] != 1) return Color.BLACK;
                else return Color.GREEN;
            }
        };
        Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
            public Paint transform(Integer i) {
                if (i == cur) {
                    return Color.YELLOW; // Example of color from java.awt.Color
                }
                else {
                    return Color.RED;
                }
            }
        };
        this.vv.getRenderContext().setArrowFillPaintTransformer(colorTransformer);
        this.vv.getRenderContext().setArrowDrawPaintTransformer(colorTransformer);
        this.vv.getRenderContext().setEdgeDrawPaintTransformer(colorTransformer);
        this.vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        this.image = (BufferedImage) vi.getImage(
                new Point2D.Double(630, 630),
                new Dimension(630, 630));
        setIcon(new ImageIcon(this.image.getScaledInstance(this.h ,this.w, Image.SCALE_SMOOTH)));
        
    }
    
    //save image function
    public void save() throws IOException {
    	File outputfile = new File("image.jpg");
    	ImageIO.write(this.image, "jpg", outputfile);
    }
}  
