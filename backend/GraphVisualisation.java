package backend;

import java.awt.RenderingHints;
import  javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.lang.Math;
import java.awt.Dimension;
import java.util.*;
import java.lang.*;
import node.MyNode;
import node.MyLink;
import Graph.Tree;
import UI.*;
import java.io.File;
import javax.swing.JFrame;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.Container;
import java.awt.*;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.VisualizationImageServer; 
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import java.awt.Font;
import java.io.IOException;
import java.io.FileNotFoundException;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class GraphVisualisation {
	 

    int n;
    LinkedList<Integer> [] Edge = new LinkedList[1000];
    Map<Pair, Integer> map= new HashMap<Pair, Integer>();

    int cur = 1, nxt = 0;
    public GraphVisualisation(){
        for (int j=0; j<=500; j++)
            this.Edge[j]=new LinkedList<Integer>();
    }


    public void WritePNG(VisualizationImageServer<Integer, String> vis, VisualizationViewer<Integer, String> vv3){  
        BufferedImage image = (BufferedImage) vis.getImage(
            new Point2D.Double(vv3.getGraphLayout().getSize().getWidth(),
            vv3.getGraphLayout().getSize().getHeight()),
            new Dimension(vv3.getGraphLayout().getSize()));

        // Write image to a png file
        File outputfile = new File("./png/graph.png");

        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            // Exception handling
        }
    }

    public void ReadFile(){

        try {
			File myObj = new File("./Test.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] edge = data.split("\\s");
				int edge_start = Integer.parseInt(edge[0]);
                this.n = Math.max(n, edge_start);
				for(int i = 1; i < edge.length; i++) {
					int edge_end = Integer.parseInt(edge[i]);
                    this.Edge[edge_start].add(edge_end);
                    this.n = Math.max(this.n, edge_end);
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error!");
			e.printStackTrace();
		}
    }    

    public static void main(String[] args){
        
        GraphVisualisation GA = new GraphVisualisation();
        GA.ReadFile();
		Tree T = new Tree(GA.n);
		
        // Add edges
        T.addAllEdge(GA.Edge);
        //source and destination points
        int s = 1, d = GA.n;
        // T.printAllPaths(s, d);
        
        Simulator UI = new Simulator();
    
        UI.Simulate(GA.n, GA.Edge);
        // GA.VisualizeGraph(GA.n, GA.Edge, new int[GA.n + 1][GA.n + 1]);
            
    }
}
