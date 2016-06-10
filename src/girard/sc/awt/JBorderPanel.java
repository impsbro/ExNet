package girard.sc.awt;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 *  JBorderPanel
 * <p>
 * <br> A class that draws a simple border around a given panel
 * <p>
 * <br> Started: 1998
 * <br> Modified: 1999
 * <p>
 *
 * @author Clif Presser and Dudley Girard
 * @version SC AWT Toolkit
 * @since JDK1.1 
 */

public class JBorderPanel extends JPanel
    {
/**
 * The Panel around which the border is drawn.
 */
    JPanel m_center;

/**
 * The style in which to draw the border.
 */
    int bp_style;

    public final static int FRAME = 1;
    public final static int BUTTON = 2;
    public final static int ETCHED = 3;
    public final static int FRAME2 = 4;
    public final static int BUTTON2 = 5;
    public final static int ETCHED2 = 6;
        
/**
 * Constructor. Builds a new JBorderPanel param center Panel 
 * that will have a border placed around it.
 *
 * @param center The Panel around which the border will be built.
 */
    public JBorderPanel(JPanel center)
        {
        m_center = center;
        bp_style = FRAME;
                
        setLayout(new BorderLayout(0,0));
                
        add("Center", m_center);
        add("North", new JBorder(JBorder.NORTH,bp_style));
        add("South", new JBorder(JBorder.SOUTH,bp_style));
        add("East", new JBorder(JBorder.EAST,bp_style));
        add("West", new JBorder(JBorder.WEST,bp_style));
        }

/**
 * Constructor. Builds a new JBorderPanel param center Panel 
 * that will have a border placed around it.
 *
 * @param center The Panel around which the border will be built.
 * @param style What look to give the border.
 */
    public JBorderPanel(JPanel center, int style)
        {
        m_center = center;
        bp_style = style;
                
        setLayout(new BorderLayout(0,0));
                
        add("Center", m_center);
        add("North", new JBorder(JBorder.NORTH,bp_style));
        add("South", new JBorder(JBorder.SOUTH,bp_style));
        add("East", new JBorder(JBorder.EAST,bp_style));
        add("West", new JBorder(JBorder.WEST,bp_style));
        }

/**
 * Get the current Panel that is within these borders. 
 * 
 * @return Returns the stored Panel
 */
    public JPanel getPanel()
        {
        return m_center;
        }

/**
 * Sets a new Panel to have this border param p new Panel to 
 * replace the old one. 
 *
 * @param p The new Panel to set the m_center Panel to.
 * @return Returns the old Panel
 */
    public JPanel setPanel(JPanel p)
        {
        JPanel temp = m_center;
        m_center = p;
        add("Center", m_center);
        return temp;
        }

/**
 * Static Main method for testing. Param args Array of strings 
 * passed in as arguments to the application.
 *
 * @param args Not used at this time.
 */
    public static void main(String args[])
        {
        Frame f = new Frame("Test");
        f.setSize(100,100);

        JPanel p = new JPanel();
        JBorderPanel bp = new JBorderPanel(p);

        f.add(bp);
        f.show();
        }
    }

/**
 * A Protected class to draw the actual borders. Changes in Java to v1.1
 * caused the borders to draw incorrectly. The paint method has therefore
 * been removed.
 *
 * @author Clif Presser
 */
class JBorder extends Canvas
    {
  
/**
 * A JBorder with orientation JBorder,  NORTH runs along the top edge of the JBorderPanel.
 *
 */
    protected static final int NORTH = 1;
  
/**
 * A JBorder with orientation JBorder,  SOUTH runs along the bottom edge of the
 * JBorderPanel.
 *
 */
    protected static final int SOUTH = 2;
 
/**
 * A JBorder with orientation JBorder,  EAST runs along the right edge of the
 * JBorderPanel.
 *
 */
    protected static final int EAST = 3;
  
/**
 * A JBorder with orientation JBorder,  WEST runs along the left edge of the
 * JBorderPanel.
 *
 */
    protected static final int WEST = 4;

/**
 * Tells whether this JBorder is for the NORTH, SOUTH, EAST, or WEST.
 *
 */        
    int m_orientation;

/**
 * Tells what style to draw this JBorder in.
 *
 */
    int bp_style;

/**
 * One of the three colors used in drawing the border, color value can change depending
 * on the selected style.
 *
 */  
    Color m_c1 = Color.darkGray;
/**
 * One of the three colors used in drawing the border, color value can change depending
 * on the selected style.
 *
 */  
    Color m_c2 = Color.gray;
/**
 * One of the three colors used in drawing the border, color value can change depending
 * on the selected style.
 *
 */  
    Color m_c3 = Color.lightGray;

/**
 * Creates a new single border.  
 *
 * @param orientation Tells which direction this JBorder faces.
 * @param style Tells what style to draw the JBorder in.
 */
    protected JBorder (int orientation, int style)
        {
        m_orientation = orientation;
        bp_style = style;
        setBackground(Color.gray);

        if ((style == 4) || (style == 5) || (style == 6))
            {
            m_c1 = Color.gray;
            m_c2 = Color.lightGray;
            m_c3 = Color.white;
            }
        }
        
  
/**
 * Usually called by the layout to determine the preferred size of a component.
 *
 * @return Dimension indicating the preferred size of the JBorder.
 */
    public Dimension getPreferredSize()
        {
        if ((bp_style == JBorderPanel.FRAME) || (bp_style == JBorderPanel.FRAME2))
            {
            if (m_orientation == EAST || m_orientation == WEST)
                return new Dimension(5, 10);
            else
                return new Dimension(10, 5);
            }
        if ((bp_style == JBorderPanel.BUTTON) || (bp_style == JBorderPanel.BUTTON2))
            {
            if (m_orientation == EAST || m_orientation == WEST)
                return new Dimension(3, 10);
            else
                return new Dimension(10, 3);
            }
        if ((bp_style == JBorderPanel.ETCHED) || (bp_style == JBorderPanel.ETCHED2))
            {
            if (m_orientation == EAST || m_orientation == WEST)
                return new Dimension(1, 10);
            else
                return new Dimension(10, 1);
            }
        if (m_orientation == EAST || m_orientation == WEST)
            return new Dimension(5, 10);
        else
            return new Dimension(10, 5);
        }
  
/**
 * Where the JBorder is acutally drawn.  Calls one of three functions based on the style.
 *
 * @param g The Graphics object for the paint function attached to this JBorder.
 */
    public void paint(Graphics g)
        {   
        if ((bp_style == JBorderPanel.FRAME) || (bp_style == JBorderPanel.FRAME2))
            paintA(g);
        else if ((bp_style == JBorderPanel.BUTTON) || (bp_style == JBorderPanel.BUTTON2))
            paintB(g);
        else if ((bp_style == JBorderPanel.ETCHED) || (bp_style == JBorderPanel.ETCHED2))
            paintC(g);
        }

/**
 * Used to draw borders for the FRAME or FRAME2 style.
 *
 * @param g The Graphics object for the paint function attached to this JBorder.
 */
    private void paintA(Graphics g)
        {
        Dimension d = getSize();
      
        if (m_orientation == NORTH)
            {
            g.setColor(m_c1);
            g.fillRect(0,0,d.width-1,d.height);
                        
            g.setColor(m_c2);
            g.fillRect(1,1,d.width-3,d.height-1);
                        
            g.setColor(m_c3);
            g.fillRect(3,3,d.width-7,d.height-3);

            }
        else if (m_orientation == SOUTH)
            {
            g.setColor(m_c1);
            g.fillRect(0,0,d.width-1, d.height);
                        
            g.setColor(m_c2);
            g.fillRect(1,0,d.width-3, d.height-1);
                        
            g.setColor(m_c3);
            g.fillRect(3,0,d.width-7, d.height-3);
            }
        else if (m_orientation == EAST)
            {
            g.setColor(m_c3);
            g.drawLine(0,0,0,d.height);
            g.setColor(m_c2);
            g.drawLine(1,0,1,d.height);
            g.drawLine(2,0,2,d.height);
            g.setColor(m_c1);
            g.drawLine(3,0,3,d.height);
            }
        else if (m_orientation == WEST)
            {
            g.setColor(m_c1);
            g.drawLine(0,0,0,d.height);
            g.setColor(m_c2);
            g.drawLine(1,0,1,d.height);
            g.drawLine(2,0,2,d.height);
            g.setColor(m_c3);
            g.drawLine(3,0,3,d.height);
            }
        }

/**
 * Used to draw borders for the BUTTON or BUTTON2 style.
 *
 * @param g The Graphics object for the paint function attached to this JBorder.
 */
    public void paintB(Graphics g)
        {
	  Dimension d = getSize();
		
        if (m_orientation == NORTH)
		{
            g.setColor(m_c2);
            int x[] = new int[4];
            int y[] = new int[4];

            x[0] = 0;
            x[1] = 0;
            x[2] = d.width-d.height;
            x[3] = d.width;
            y[0] = 0;
            y[1] = d.height;
            y[2] = d.height;
            y[3] = 0;

            g.fillPolygon(x,y,4);

            x = new int[3];
            y = new int[3];

                  
            x[0] = d.width-d.height;
            x[1] = d.width;
            x[2] = d.width;
            y[0] = d.height;
            y[1] = d.height;
            y[2] = 0;
            g.setColor(m_c1);
            g.fillPolygon(x,y,3);

            g.setColor(m_c2);
            g.drawLine(0, 0, 0, d.height);
            g.drawLine(0, 0, d.width, 0);
		}

	  else if (m_orientation == SOUTH)
	      {
            g.setColor(m_c1);
            int x[] = new int[4];
            int y[] = new int[4];

            x[0] = 0;
            x[1] = d.width;
            x[2] = d.width;
            x[3] = d.height;
            y[0] = 0;
            y[1] = 0;
            y[2] = d.height;
            y[3] = d.height;

            g.fillPolygon(x,y,4);

            x = new int[3];
            y = new int[3];

                  
            x[0] = 0;
            x[1] = d.height;
            x[2] = 0;
            y[0] = 0;
            y[1] = 0;
            y[2] = d.height;
            g.setColor(m_c2);
            g.fillPolygon(x,y,3);

            g.setColor(m_c2);
            g.drawLine(0, 0, 0, d.height);
            g.drawLine(d.height+1, 0, d.width - d.height, 0);
		}

	  else if (m_orientation == WEST)
		{
            g.setColor(m_c2);
            int x[] = new int[4];
            int y[] = new int[4];

            x[0] = 0;
            x[1] = 0;
            x[2] = d.width;
            x[3] = d.width;
            y[0] = 0;
            y[1] = d.height;
            y[2] = d.height;
            y[3] = 0;

            g.fillPolygon(x,y,4);

            g.setColor(m_c2);
            g.drawLine(0, 0, 0, d.height);
		}

	  else if (m_orientation == EAST)
		{
            g.setColor(m_c1);
            int x[] = new int[4];
            int y[] = new int[4];

            x[0] = 0;
            x[1] = d.width;
            x[2] = d.width;
            x[3] = 0;
            y[0] = 0;
            y[1] = 0;
            y[2] = d.height;
            y[3] = d.height;

            g.fillPolygon(x,y,4);

            g.setColor(m_c2);
            g.drawLine(0, 0, 0, d.height);
		}
	  }

/**
 * Used to draw borders for the ETCHED or ETCHED2 style.
 *
 * @param g The Graphics object for the paint function attached to this JBorder.
 */
    private void paintC(Graphics g)
        {
        Dimension d = getSize();
      
        if (m_orientation == NORTH)
            {
            g.setColor(m_c3);
            g.drawLine(0,0,d.width-1,0); 
            }
        else if (m_orientation == SOUTH)
            {
            g.setColor(m_c1);
            g.drawLine(0,0,d.width-1, 0);
            }
        else if (m_orientation == EAST)
            {
            g.setColor(m_c1);  // Used to be white
            g.drawLine(0,0,0,d.height);
            }
        else if (m_orientation == WEST)
            {
            g.setColor(m_c3);
            g.drawLine(0,0,0,d.height);
            }
        }
    } 
