package Panels;

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

  


public class ImagePanel extends JPanel
{
    private BufferedImage image;
    double scale;
    public ImagePanel(){
        scale = 1.0;
        setBackground(Color.white);
    }
    public void init(VisualizationImageServer<Integer, String> vis,  VisualizationViewer<Integer, String> vv3){
    
        this.image = (BufferedImage) vis.getImage(
            new Point2D.Double(vv3.getGraphLayout().getSize().getWidth(),
            vv3.getGraphLayout().getSize().getHeight()),
            new Dimension(vv3.getGraphLayout().getSize()));
       
    }
    
  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, null); // see javadoc for more info on the parameters            
    }
  
    /**
     * For the scroll pane.
     */
    public Dimension getPreferredSize()
    {
        int w = (int)(scale * image.getWidth());
        int h = (int)(scale * image.getHeight());
        System.out.print(w);
        return new Dimension(w, h);
    }
  
    public void setScale(double s)
    {
        scale = s;
        revalidate();      // update the scroll pane
        repaint();
    }
}
  
public class ImageZoom
{
    ImagePanel imagePanel;
  
    public void init(ImagePanel ip)
    {
        imagePanel = ip;
    }
  
    public JPanel getUIPanel()
    {
        SpinnerNumberModel model = new SpinnerNumberModel(1.0, 0.1, 5, .1);
        final JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(45, spinner.getPreferredSize().height));
        spinner.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                float scale = ((Double)spinner.getValue()).floatValue();
                imagePanel.setScale(scale);
            }
        });
        JPanel panel = new JPanel();
        panel.add(new JLabel("scale"));
        panel.add(spinner);
        return panel;
    }
}