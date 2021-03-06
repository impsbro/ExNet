package girard.sc.cc.awt;

/* Used to edit the description of a specific CCNetworkAction.

   Author: Dudley Girard
   Started: 7-24-2001
*/

import girard.sc.awt.BorderPanel;
import girard.sc.awt.GridBagPanel;
import girard.sc.cc.obj.CCNetworkAction;
import girard.sc.expt.web.ExptOverlord;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CCEditDescriptionWindow extends Frame implements ActionListener
    {
    ExptOverlord m_EOApp;
    CCFormatNetworkActionWindow m_FNAWApp;
    CCNetworkAction m_activeNetwork;

 /* GUI Variables For User Panel */
    GridBagPanel m_NodeUserPanel;
    TextArea m_desc = new TextArea("",4,25,TextArea.SCROLLBARS_NONE);
    
 /* GUI Variables For Bottom Panel */
    GridBagPanel m_BottomPanel;
    Button m_DescOK, m_DescCancel;

    public CCEditDescriptionWindow(ExptOverlord app1, CCFormatNetworkActionWindow app2, CCNetworkAction app3)
        {
        super();

        m_EOApp = app1; /* Need to make pretty buttons. */
        m_FNAWApp = app2; /* Need so can unset edit mode */
        m_activeNetwork = app3; /* Makes referencing easier */

        initializeLabels();

        setLayout(new BorderLayout());
        setBackground(m_EOApp.getDispBkgColor());
        setTitle(m_EOApp.getLabels().getObjectLabel("ccedw_title"));
        setFont(m_EOApp.getMedWinFont());


// Setup NodeUser Panel area.

        m_NodeUserPanel = new GridBagPanel();

        m_NodeUserPanel.constrain(new Label(m_EOApp.getLabels().getObjectLabel("ccedw_description")),1,1,4,1,GridBagConstraints.CENTER);

        m_desc.setText(m_activeNetwork.getDesc());
        m_NodeUserPanel.constrain(m_desc,1,2,4,4,GridBagConstraints.CENTER); 

// Setup Bottom Panel

        m_BottomPanel = new GridBagPanel();

        m_DescOK = new Button(m_EOApp.getLabels().getObjectLabel("ccedw_ok"));
        m_DescOK.addActionListener(this);
        m_BottomPanel.constrain(m_DescOK,1,1,2,1,GridBagConstraints.CENTER);

        m_DescCancel = new Button(m_EOApp.getLabels().getObjectLabel("ccedw_cancel"));
        m_DescCancel.addActionListener(this);
        m_BottomPanel.constrain(m_DescCancel,3,1,2,1,GridBagConstraints.CENTER);

        add("Center",new BorderPanel(m_NodeUserPanel,BorderPanel.FRAME));
        add("South",new BorderPanel(m_BottomPanel,BorderPanel.FRAME));

        pack();
        show();
        }

    public void actionPerformed (ActionEvent e)
        {
        Button theSource = (Button)e.getSource();
       
        if (theSource == m_DescOK)
            {
            // Handle Save
            m_activeNetwork.setDesc(m_desc.getText());
            m_FNAWApp.setEditMode(false);
            removeLabels();
            dispose();
            }
        if (theSource == m_DescCancel)
            {
            m_FNAWApp.setEditMode(false);
            removeLabels();
            dispose();
            }
        }

    public void initializeLabels()
        {
        m_EOApp.initializeLabels("girard/sc/cc/awt/ccedw.txt");
        }  

    public void removeLabels()
        {
        m_EOApp.removeLabels("girard/sc/cc/awt/ccedw.txt");
        }
    }
