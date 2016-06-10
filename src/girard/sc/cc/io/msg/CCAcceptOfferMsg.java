package girard.sc.cc.io.msg;

/* If a subject accepts another subject's offer then this message is sent.

   Author: Dudley Girard
   Started: 6-27-2001
   Modified: 7-25-2001
*/

import girard.sc.awt.ErrorDialog;
import girard.sc.cc.awt.CCClientDisplayArrow;
import girard.sc.cc.awt.CCNetworkActionClientWindow;
import girard.sc.cc.awt.CCNetworkActionExperimenterWindow;
import girard.sc.cc.awt.CCNetworkActionObserverWindow;
import girard.sc.cc.obj.CCEdge;
import girard.sc.cc.obj.CCEdgeDisplay;
import girard.sc.cc.obj.CCExchange;
import girard.sc.cc.obj.CCNetwork;
import girard.sc.cc.obj.CCNode;
import girard.sc.cc.obj.CCNodeResource;
import girard.sc.cc.obj.CCOffersOutputObject;
import girard.sc.expt.awt.ClientWindow;
import girard.sc.expt.awt.ExperimenterWindow;
import girard.sc.expt.awt.ObserverWindow;
import girard.sc.expt.io.ExptServerConnection;
import girard.sc.expt.io.msg.ExptErrorMsg;
import girard.sc.expt.io.msg.ExptMessage;
import girard.sc.expt.io.obj.ExptComptroller;

import java.util.Enumeration;
import java.util.Vector;

public class CCAcceptOfferMsg extends ExptMessage 
    { 
    public CCAcceptOfferMsg (Object args[])
        {
        super(args);
        }

    public void getClientResponse(ClientWindow cw)
        {
        if (!(cw instanceof CCNetworkActionClientWindow))
            {
            new ErrorDialog("Wrong Client Window. - CCAcceptOfferMsg");
            return;
            }

        CCNetworkActionClientWindow nacw = (CCNetworkActionClientWindow)cw;
        CCNetwork ccn = (CCNetwork)nacw.getExpApp().getActiveAction();

        int from = ((Integer)this.getArgs()[0]).intValue();
        int to = ((Integer)this.getArgs()[1]).intValue();
        int fromKeep = ((Integer)this.getArgs()[2]).intValue();
        int toKeep = ((Integer)this.getArgs()[3]).intValue();

        Boolean rr = (Boolean)ccn.getExtraData("RoundRunning");

        if ((!nacw.getExpApp().getExptRunning()) || (!rr.booleanValue()) || (nacw.getExpApp().getExptStopping()))
            return;

        CCNode fromNode = (CCNode)ccn.getNode(from);
        CCNode toNode = (CCNode)ccn.getNode(to);

        CCNodeResource fromCcnr = (CCNodeResource)fromNode.getExptData("CCNodeResource");
        CCNodeResource toCcnr = (CCNodeResource)toNode.getExptData("CCNodeResource");

        int tt = nacw.getNetwork().getPeriod().getTime() - nacw.getNetwork().getPeriod().getCurrentTime();

        if (toCcnr.getOffer(from) == null)
            {
            CCExchange fromOffer = new CCExchange(tt,-1,fromKeep,from,toKeep,to);
            fromOffer.setExchangeState(CCExchange.GREEN);
            toCcnr.addOffer(fromOffer);
            }
        else
            {
            CCExchange fromOffer = toCcnr.getOffer(from);
            fromOffer.getNode1().setResource(fromKeep);
            fromOffer.getNode2().setResource(toKeep);
            fromOffer.setExchangeState(CCExchange.GREEN);
            fromOffer.setTTime(tt);
            }
        fromCcnr.getOffer(to).setExchangeState(CCExchange.YELLOW);

        Enumeration enm = ccn.getEdgeList().elements();
        while(enm.hasMoreElements())
            {
            CCEdge edge = (CCEdge)enm.nextElement();
            CCEdgeDisplay cced = (CCEdgeDisplay)edge.getExptData("CCEdgeDisplay");

            if ((from == edge.getNode1()) && (to == edge.getNode2()))
                {
                cced.setN1Display(0,""+fromKeep);
                cced.setN1Display(1,""+toKeep);

                cced.setN1Display(2,"DOT");
                cced.setExchangeState1(CCEdgeDisplay.GREEN);
                cced.setExchangeState2(CCEdgeDisplay.YELLOW);
                break;
                }
            if ((from == edge.getNode2()) && (to == edge.getNode1()))
                {
                cced.setN2Display(0,""+fromKeep);
                cced.setN2Display(1,""+toKeep);

                cced.setN2Display(2,"DOT");
                cced.setExchangeState2(CCEdgeDisplay.GREEN);
                cced.setExchangeState1(CCEdgeDisplay.YELLOW);
                break;
                }
            }

        CCNode node = (CCNode)ccn.getExtraData("Me");

        // Was it an offer sent to me by one of my neighbors?
        if (to == node.getID())
            {
            CCClientDisplayArrow arrow = nacw.getArrow();
            if (arrow.getToNode() != null)
                {
                if (arrow.getToNode().getID() == from)
                    {
                    arrow.updateBubbleButton();
                    arrow.updateDisplayArrow();
                    }
                }
            }
        nacw.repaint();
        nacw.validate();  // Make sure any changes are hopefully displayed properly.
        }

    public ExptMessage getExptServerConnectionResponse(ExptServerConnection esc)
        {
        Object[] args = this.getArgs();

// System.err.println("ESR: CC Accept Offer Message");
    
        ExptComptroller ec = esc.getExptIndex();
        int index = esc.getUserNum();

        if (ec != null)
            {
            synchronized(ec)
                {
                if (index == ExptComptroller.EXPERIMENTER)
                    {
                    if (!ec.allRegistered())
                        {
                        Object[] err_args = new Object[2];
                        err_args[0] = new String("Least one user not registered.");
                        err_args[1] = new String("CCAcceptOfferMsg");
                        return new ExptErrorMsg(err_args);
                        }
                    ec.sendToAllUsers(new CCAcceptOfferMsg(args));
                    ec.sendToAllObservers(new CCAcceptOfferMsg(args));
                    return null; 
                    }
                else
                    {
                    if (!ec.allRegistered())
                        return null;
                    Object[] out_args = args;
                    ec.addServerMessage(new CCAcceptOfferMsg(out_args));
                    return null;
                    }
                }
            }
        else
            {
            Object[] err_args = new Object[2];
            err_args[0] = new String("No experiment.");
            err_args[1] = new String("CCAcceptOfferMsg");
            return new ExptErrorMsg(err_args);
            }
        }

    public void getExperimenterResponse(ExperimenterWindow ew)
        {
        Object[] args = this.getArgs();

        CCNetworkActionExperimenterWindow naew = (CCNetworkActionExperimenterWindow)ew;
        
        Boolean rr = (Boolean)naew.getNetwork().getExtraData("RoundRunning");

        if ((!rr.booleanValue()) || (!ew.getExpApp().getExptRunning()) || (ew.getExpApp().getExptStopping()))
            return;

        int from = ((Integer)args[0]).intValue();
        int to = ((Integer)args[1]).intValue();
        int fromKeep = ((Integer)args[2]).intValue();
        int toKeep = ((Integer)args[3]).intValue();

        CCNode fromNode = (CCNode)naew.getNetwork().getNode(from);
        CCNode toNode = (CCNode)naew.getNetwork().getNode(to);

        CCNodeResource fromCcnr = (CCNodeResource)fromNode.getExptData("CCNodeResource");
        CCNodeResource toCcnr = (CCNodeResource)toNode.getExptData("CCNodeResource");

        if ((fromCcnr.canNegoiateWith(toNode.getID())) && (toCcnr.canNegoiateWith(fromNode.getID())) && (!fromCcnr.tradeCompletedWith(toNode.getID())))
            {
            int tt = naew.getNetwork().getPeriod().getTime() - naew.getNetwork().getPeriod().getCurrentTime();
            long ct = naew.getPresentTime();
            if (toCcnr.getOffer(from) == null)
                {
                CCExchange fromOffer = new CCExchange(tt,ct,fromKeep,from,toKeep,to);
                fromOffer.setExchangeState(CCExchange.GREEN);
                toCcnr.addOffer(fromOffer);
                }
            else
                {
                CCExchange fromOffer = toCcnr.getOffer(from);
                fromOffer.getNode1().setResource(fromKeep);
                fromOffer.getNode2().setResource(toKeep);
                fromOffer.setExchangeState(CCExchange.GREEN);
                fromOffer.setTTime(tt);
                fromOffer.setRTime(ct);
                }

            fromCcnr.getOffer(to).setExchangeState(CCExchange.YELLOW);

            Enumeration enm = naew.getNetwork().getEdgeList().elements();
            while(enm.hasMoreElements())
                {
                CCEdge edge = (CCEdge)enm.nextElement();
                CCEdgeDisplay cced = (CCEdgeDisplay)edge.getExptData("CCEdgeDisplay");

                if ((from == edge.getNode1()) && (to == edge.getNode2()))
                    {
                    cced.setN1Display(0,""+fromKeep);
                    cced.setN1Display(1,""+toKeep);
                    cced.setN1Display(2,"*");
                    cced.setExchangeState1(CCEdgeDisplay.GREEN);
                    cced.setExchangeState2(CCEdgeDisplay.YELLOW);
                    break;
                    }
                if ((from == edge.getNode2()) && (to == edge.getNode1()))
                    {
                    cced.setN2Display(0,""+fromKeep);
                    cced.setN2Display(1,""+toKeep);
                    cced.setN2Display(2,"*");
                    cced.setExchangeState2(CCEdgeDisplay.GREEN);
                    cced.setExchangeState1(CCEdgeDisplay.YELLOW);
                    break;
                    }
                }
            }
        else
            {
            return; // Offer not valid don't process it.
            }

        naew.repaint();

    /* update data output here */
        int tt = naew.getNetwork().getPeriod().getTime() - naew.getNetwork().getPeriod().getCurrentTime();
        int cr = naew.getNetwork().getPeriod().getCurrentRound();

        CCOffersOutputObject data = new CCOffersOutputObject(ew.getExpApp().getExptOutputID(),naew.getExpApp().getActionIndex(),cr,from,to,fromKeep,toKeep,"Accept",tt,naew.getPresentTime());

        Vector outData = (Vector)naew.getNetwork().getExtraData("Data");
        outData.addElement(data);

        Object[] out_args = args;
        CCAcceptOfferMsg tmp = new CCAcceptOfferMsg(out_args);
        naew.getSML().sendMessage(tmp);
        }

    public void getObserverResponse(ObserverWindow ow)
        {
        if (!ow.getExpApp().getJoined())
            return;

        if (!(ow instanceof CCNetworkActionObserverWindow))
            {
            new ErrorDialog("Wrong Observer Window. - CCAcceptOfferMsg");
            return;
            }    

        Object[] args = this.getArgs();

        CCNetworkActionObserverWindow naow = (CCNetworkActionObserverWindow)ow;
        
        Boolean rr = (Boolean)naow.getNetwork().getExtraData("RoundRunning");

        if ((!rr.booleanValue()) || (!ow.getExpApp().getExptRunning()) || (ow.getExpApp().getExptStopping()))
            return;

        int from = ((Integer)args[0]).intValue();
        int to = ((Integer)args[1]).intValue();
        int fromKeep = ((Integer)args[2]).intValue();
        int toKeep = ((Integer)args[3]).intValue();

        CCNode fromNode = (CCNode)naow.getNetwork().getNode(from);
        CCNode toNode = (CCNode)naow.getNetwork().getNode(to);

        CCNodeResource fromCcnr = (CCNodeResource)fromNode.getExptData("CCNodeResource");
        CCNodeResource toCcnr = (CCNodeResource)toNode.getExptData("CCNodeResource");

        int tt = naow.getNetwork().getPeriod().getTime() - naow.getNetwork().getPeriod().getCurrentTime();

        if (toCcnr.getOffer(from) == null)
            {
            CCExchange fromOffer = new CCExchange(fromKeep,from,toKeep,to);
            fromOffer.setExchangeState(CCExchange.GREEN);
            toCcnr.addOffer(fromOffer);
            }
        else
            {
            CCExchange fromOffer = toCcnr.getOffer(from);
            fromOffer.getNode1().setResource(fromKeep);
            fromOffer.getNode2().setResource(toKeep);
            fromOffer.setExchangeState(CCExchange.GREEN);
            }
        fromCcnr.getOffer(to).setExchangeState(CCExchange.RED);

        Enumeration enm = naow.getNetwork().getEdgeList().elements();
        while(enm.hasMoreElements())
            {
            CCEdge edge = (CCEdge)enm.nextElement();
            CCEdgeDisplay cced = (CCEdgeDisplay)edge.getExptData("CCEdgeDisplay");

            if ((from == edge.getNode1()) && (to == edge.getNode2()))
                {
                cced.setN1Display(0,""+fromKeep);
                cced.setN1Display(1,""+toKeep);
                cced.setN1Display(2,"*");
                cced.setExchangeState1(CCEdgeDisplay.GREEN);
                cced.setExchangeState2(CCEdgeDisplay.YELLOW);
                break;
                }
            if ((from == edge.getNode2()) && (to == edge.getNode1()))
                {
                cced.setN2Display(0,""+fromKeep);
                cced.setN2Display(1,""+toKeep);
                cced.setN2Display(2,"*");
                cced.setExchangeState2(CCEdgeDisplay.GREEN);
                cced.setExchangeState1(CCEdgeDisplay.YELLOW);
                break;
                }
            }

        naow.repaint();
        }
    }