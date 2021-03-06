package girard.sc.qa.awt;

import girard.sc.awt.ErrorDialog;
import girard.sc.awt.GridBagPanel;
import girard.sc.expt.awt.ClientWindow;
import girard.sc.expt.io.ExptMessageListener;
import girard.sc.expt.io.msg.ExptErrorMsg;
import girard.sc.expt.io.msg.ExptMessage;
import girard.sc.expt.obj.ClientExptInfo;
import girard.sc.expt.web.ExptOverlord;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;

/**
 * This window is used to display a wait message for a subject.
 * <p>
 * <br>Started: 08-26-2002
 * <br>Moved: 10-14-2002
 * <p>
 * @author Dudley Girard
 */

public class QAWaitWindow extends ClientWindow
    {

    public QAWaitWindow(ExptOverlord app1, ClientExptInfo app2, ExptMessageListener app3)
        {
        super(app1,app2,app3);
 
        initializeLabels();

        // m_SML.addActionListener(this);

        getContentPane().setLayout(new BorderLayout());
        setTitle(m_EOApp.getLabels().getObjectLabel("qaww_title"));
        getContentPane().setFont(m_EOApp.getMedWinFont());
        getContentPane().setBackground(m_EOApp.getWinBkgColor());

        Panel centerPanel = new Panel(new GridLayout(1,1));
        centerPanel.add(new Label(m_EOApp.getLabels().getObjectLabel("qaww_pwfotf")));

    // Start Setup for North, South, East, and West Panels.
        GridBagPanel northPanel = new GridBagPanel();
        northPanel.constrain(new Label(" "),1,1,1,1);

        GridBagPanel southPanel = new GridBagPanel();
        southPanel.constrain(new Label(" "),1,1,1,1);

        GridBagPanel eastPanel = new GridBagPanel();
        eastPanel.constrain(new Label(" "),1,1,1,1);

        GridBagPanel westPanel = new GridBagPanel();
        westPanel.constrain(new Label(" "),1,1,1,1);
   // End Setup for  North, South, East, and West Panels.

        getContentPane().add("Center",centerPanel);
        getContentPane().add("North",northPanel);
        getContentPane().add("South",southPanel);
        getContentPane().add("East",eastPanel);
        getContentPane().add("West",westPanel);
        pack();
        show();
        }

    public void actionPerformed(ActionEvent e) 
        {
        if (e.getSource() instanceof ExptMessage)
            {
            synchronized(m_SML)
                {
                ExptMessage em = (ExptMessage)e.getSource();

                if (em instanceof ExptErrorMsg)
                    {
                    String str = (String)em.getArgs()[0];
                    new ErrorDialog(str);
                    }
                else
                    {
                    em.getClientResponse(this);
                    }
                }
            }
        }

    public void cleanUpWindow()
        {
        removeLabels();
        }

    public void initializeLabels()
        {
        m_EOApp.initializeLabels("girard/sc/qa/awt/qaww.txt");
        }  

    public void removeLabels()
        {
        m_EOApp.removeLabels("girard/sc/qa/awt/qaww.txt");
        }
    }
