package girard.sc.cc.awt;

/* Used to display the offer data from a CCNetworkAction to an experimenter.

   Author: Dudley Girard
   Started: 9-13-2001
*/

import girard.sc.cc.obj.CCNetworkAction;
import girard.sc.cc.obj.CCOffersOutputObject;
import girard.sc.expt.obj.BaseDataInfo;
import girard.sc.expt.obj.DataOutputObject;
import girard.sc.expt.web.ExptOverlord;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class CCOfferDataWindow extends Frame implements ActionListener
    {
    CCNetworkActionDataDisplay m_DDApp;
    CCNetworkAction m_NApp;
    ExptOverlord m_EOApp;
    BaseDataInfo m_bdi;
    
    MenuBar m_menuBar;
    Menu m_fileMenu, m_helpMenu;

    public CCOfferDataWindow(CCNetworkAction app1, ExptOverlord app2, CCNetworkActionDataDisplay app3, BaseDataInfo bdi)
        {
        super();
        m_NApp = app1;
        m_EOApp =app2;
        m_DDApp = app3;
        m_bdi = bdi;

        initializeLabels();

        setLayout(new GridLayout(1,1));
        setTitle(m_EOApp.getLabels().getObjectLabel("ccodw_title"));
        setFont(m_EOApp.getMedWinFont());
        setBackground(m_EOApp.getWinBkgColor());

     // Setup Menu options
        MenuItem tmpItem;

        m_menuBar = new MenuBar();

        m_menuBar.setFont(m_EOApp.getSmWinFont());

    // File Menu
        m_fileMenu = new Menu(m_EOApp.getLabels().getObjectLabel("ccodw_file"));

        tmpItem = new MenuItem(m_EOApp.getLabels().getObjectLabel("ccodw_exit"));
        tmpItem.addActionListener(this);
        m_fileMenu.add(tmpItem);

        m_menuBar.add(m_fileMenu);

    // Help Menu
        m_helpMenu = new Menu(m_EOApp.getLabels().getObjectLabel("ccodw_help"));

        tmpItem = new MenuItem(m_EOApp.getLabels().getObjectLabel("ccodw_help"));
        tmpItem.addActionListener(this);
        m_helpMenu.add(tmpItem);

        m_menuBar.add(m_helpMenu);

        setMenuBar(m_menuBar);
     // End setup for Menu options

        Panel MainPanel = new Panel(new GridLayout(1,1));

        TextArea tmpText = new TextArea(20,80);
        tmpText.setEditable(false);

        Hashtable data = m_bdi.getActionData();
        Hashtable nodeData = (Hashtable)data.get("Node Data");
        Hashtable offerData = (Hashtable)nodeData.get("CCNodeResource");
        Vector offers = (Vector)offerData.get("Data");
        
        StringBuffer str = new StringBuffer("");
        str.append(getHeadings()+"\n");

        Enumeration enm = offers.elements();
        while (enm.hasMoreElements())
            {
            DataOutputObject tmp1 = (DataOutputObject)enm.nextElement();
            if (tmp1 instanceof CCOffersOutputObject)
                {
                CCOffersOutputObject tmp2 = (CCOffersOutputObject)tmp1;
                str.append(buildOfferEntry(tmp2)+"\n");
                }
            }

        tmpText.setText(str.toString());

        MainPanel.add(tmpText);

        add(MainPanel);
        pack();
        show();
        }

    public void actionPerformed(ActionEvent e)
        {
        if (e.getSource() instanceof MenuItem)
            {
            MenuItem theSource = (MenuItem)e.getSource();

       // File Menu
            if (theSource.getLabel().equals(m_EOApp.getLabels().getObjectLabel("ccodw_exit")))
                {
                m_DDApp.setEditMode(false);
                this.dispose();
                return;
                }
        // Help Menu
            if (theSource.getLabel().equals(m_EOApp.getLabels().getObjectLabel("ccodw_help")))
                {
                m_EOApp.helpWindow("ehlp_ccodw");
                return;
                }
            }
        }

    public String buildColumnEntry(String str, int width)
        {
        int x, k, m;
        StringBuffer entry = new StringBuffer("");

        if (width > str.length())
            {
            entry.append(str);
            m = width - str.length();
            for (k=0;k<m;k++)
                {
                entry.append(" ");    
                }
            }
        else
            entry.append(str.substring(0,width));

        return entry.toString();
        }
    public String buildOfferEntry(CCOffersOutputObject obj)
        {
        StringBuffer entry = new StringBuffer("");
        int[] columnWidths = {10, 10, 6, 5, 5, 10, 10, 25, 7, 8};
        String[] entries = new String[columnWidths.length];

        entries[0] = new String(""+obj.getExptID());
        entries[1] = new String(""+obj.getActionIndex());
        entries[2] = new String(""+obj.getRound());
        entries[3] = new String(""+obj.getFromNode());
        entries[4] = new String(""+obj.getToNode());
        entries[5] = new String(""+(int)obj.getFromAmt());
        entries[6] = new String(""+(int)obj.getToAmt());
        entries[7] = obj.getOfferType();
        entries[8] = new String(""+obj.getTickTime());
        entries[9] = new String(""+obj.getRealTime());

        for (int x=0;x<columnWidths.length;x++)
            {
            entry.append(buildColumnEntry(entries[x],columnWidths[x]));
            }

        return entry.toString();
        }

    public void dispose()
        {
        removeLabels();
        super.dispose();
        }

    public String getHeadings()
        {
        int[] columnWidths = {10, 10, 6, 5, 5, 10, 10, 25, 7, 8};
        String[] headings = {"ID", "Act_Index", "Round", "From", "To", "From_Amt", "To_Amt", "Type", "Tick", "Seconds"};
        StringBuffer heading = new StringBuffer("");

        for (int x=0;x<columnWidths.length;x++)
            {
            heading.append(buildColumnEntry(headings[x],columnWidths[x]));
            }

        return heading.toString();
        }

    public void initializeLabels()
        {
        m_EOApp.initializeLabels("girard/sc/cc/awt/ccodw.txt");
        }

    public void removeLabels()
        {
        m_EOApp.removeLabels("girard/sc/cc/awt/ccodw.txt");
        }
    }
