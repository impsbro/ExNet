package girard.sc.cc.awt;

import girard.sc.cc.obj.CCEdge;
import girard.sc.cc.obj.CCNetwork;
import girard.sc.exnet.obj.NetworkBuilder;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;

/**
 * This is the mini version of the main display area for the experimenter
 * screen in a CCNetworkAction.
 * <p>
 * <br> Started: 7-24-2001
 * <p>
 * @author: Dudley Girard
 */

public class CCExperimenterMiniDisplayCanvas extends Canvas implements MouseListener,MouseMotionListener
    {
    Point m_topLeft = new Point(0,0);
    Point m_bottomRight = new Point(NetworkBuilder.WIDTH,NetworkBuilder.HEIGHT);
    double[] m_level = { 0.6, 0.7, 0.8, 0.9, 1 };
    CCNetwork m_network;
    CCExperimenterDisplayCanvas m_area;
    boolean m_mousePressed = false;

    public CCExperimenterMiniDisplayCanvas(CCNetwork n,CCExperimenterDisplayCanvas edc)
        {
        m_network = n;
        m_area = edc;
        addMouseListener(this);
        addMouseMotionListener(this);
        }
    public CCExperimenterMiniDisplayCanvas(CCNetwork n, CCExperimenterDisplayCanvas edc, Point tl, Point br)
        {
        m_network = n;
        m_topLeft = tl;
        m_bottomRight = br;
        m_area = edc;
        addMouseListener(this);
        addMouseMotionListener(this);
        }

    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) 
        {
        m_mousePressed = false;
        }
    public void mouseReleased(MouseEvent e) 
        {
        m_mousePressed = false;
        }
    public void mouseDragged(MouseEvent e) 
        {
        int width = m_bottomRight.x - m_topLeft.x;
        int height = m_bottomRight.y - m_topLeft.y;
        Dimension dim = this.getSize();

        double xAdj = NetworkBuilder.WIDTH/(1.0*dim.width);
        double yAdj = NetworkBuilder.HEIGHT/(1.0*dim.height);

        int xPos = (int)(e.getX()*xAdj);
        int yPos = (int)(e.getY()*yAdj);

        if (xPos > NetworkBuilder.WIDTH - width/2)
            xPos = NetworkBuilder.WIDTH - width/2;
        if (xPos < width/2)
            xPos = width/2;

        if (yPos > NetworkBuilder.HEIGHT - height/2)
            yPos = NetworkBuilder.HEIGHT - height/2;
        if (yPos < height/2)
            yPos = height/2;

        m_bottomRight.x = xPos + width/2;
        m_bottomRight.y = yPos + height/2;

        m_topLeft.x = m_bottomRight.x - width;
        m_topLeft.y = m_bottomRight.y - height;

        m_area.changeViewingArea(m_topLeft,m_bottomRight);
        repaint();
        }

    public void mousePressed(MouseEvent e) 
        {
        m_mousePressed = true;

    // Get the true width and height of the viewing area.
        int width = m_bottomRight.x - m_topLeft.x;
        int height = m_bottomRight.y - m_topLeft.y;

   // Get the size of the display area for the mini canvas.
        Dimension dim = this.getSize();

   // Compute the scaling rate for the mini canvas to the true viewing area.
        double xAdj = NetworkBuilder.WIDTH/(1.0*dim.width);
        double yAdj = NetworkBuilder.HEIGHT/(1.0*dim.height);

   // Adjust the location of the mouse click based on the scaling rate.
        int xPos = (int)(e.getX()*xAdj);
        int yPos = (int)(e.getY()*yAdj);

   // If the side edges of the viewing area are off the screen move the center point.
        if (xPos > NetworkBuilder.WIDTH - width/2)
            xPos = NetworkBuilder.WIDTH - width/2;
        if (xPos < width/2)
            xPos = width/2;

   // If the top-bottom edges of the viewing area are off the screen move the center point.
        if (yPos > NetworkBuilder.HEIGHT - height/2)
            yPos = NetworkBuilder.HEIGHT - height/2;
        if (yPos < height/2)
            yPos = height/2;

        m_bottomRight.x = xPos + width/2;
        m_bottomRight.y = yPos + height/2;

        m_topLeft.x = m_bottomRight.x - width;
        m_topLeft.y = m_bottomRight.y - height;

        m_area.changeViewingArea(m_topLeft,m_bottomRight);
        repaint();
        }

    public void paint(Graphics g)
        {
        Dimension dim = this.getSize();

        Image img = this.createImage(dim.width,dim.height);
        Graphics g2 = img.getGraphics();

        double xAdj = dim.width/(1.0*NetworkBuilder.WIDTH);
        double yAdj = dim.height/(1.0*NetworkBuilder.HEIGHT);

        g2.setColor(Color.black);
        Enumeration enm = m_network.getEdgeList().elements();
        while (enm.hasMoreElements())
            {
            CCEdge cce = (CCEdge)enm.nextElement();

            Point newLoc1 = new Point();
            newLoc1.x = (int)(xAdj*cce.getN1Anchor().x);
            newLoc1.y = (int)(yAdj*cce.getN1Anchor().y);

            Point newLoc2 = new Point();
            newLoc2.x = (int)(xAdj*cce.getN2Anchor().x);
            newLoc2.y = (int)(yAdj*cce.getN2Anchor().y);

            g2.drawLine(newLoc1.x,newLoc1.y,newLoc2.x,newLoc2.y);
            }
 
        g2.setColor(Color.red);
        Point newLoc1 = new Point();
        newLoc1.x = (int)(xAdj*m_topLeft.x);
        newLoc1.y = (int)(yAdj*m_topLeft.y);

        Dimension dimBox = new Dimension();
        dimBox.width = (int)(xAdj*m_bottomRight.x) - newLoc1.x;
        dimBox.height = (int)(yAdj*m_bottomRight.y) - newLoc1.y;

        if (newLoc1.x+dimBox.width >= dim.width)
            {
            dimBox.width = dim.width - newLoc1.x - 1;
            }
        if (newLoc1.y+dimBox.height >= dim.height)
            {
            dimBox.height = dim.height - newLoc1.y - 1;
            }

        g2.drawRect(newLoc1.x,newLoc1.y,dimBox.width,dimBox.height);

        if (img != null)
            {
            g.drawImage(img,0,0,this);
            }
        }

    public void update(Graphics g)
        {
        paint(g);
        }

    public void zoomAdjust(int zoomLevel)
        {
        int width = (int)(NetworkBuilder.WIDTH*m_level[zoomLevel]);
        int height = (int)(NetworkBuilder.HEIGHT*m_level[zoomLevel]);

        int midX = m_topLeft.x + ((m_bottomRight.x - m_topLeft.x)/2);
        int midY = m_topLeft.y + ((m_bottomRight.y - m_topLeft.y)/2);

        if (midX + width/2 > NetworkBuilder.WIDTH)
            midX = NetworkBuilder.WIDTH - width/2;
        if (midX - width/2 < 0)
            midX = width/2;
        
        if (midY + height/2 > NetworkBuilder.HEIGHT)
            midY = NetworkBuilder.HEIGHT - height/2;
        if (midY - height/2 < 0)
            midY = height/2;

        m_topLeft.x = midX - width/2;
        m_topLeft.y = midX - height/2;

        m_bottomRight.x = m_topLeft.x + width;
        m_bottomRight.y = m_topLeft.y + height;

        m_area.changeViewingArea(m_topLeft,m_bottomRight);
        repaint();
        }
    }
