package girard.sc.be.obj;

import girard.sc.expt.obj.DataOutputObject;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 * This object stores the earnings for each nod for  coalitions that are part of
 * a BENetworkAction for saving the data to the database or reading the data
 * from a database.
 * <p>
 * <br> Started: 08-05-2003
 * <p>
 * @author Dudley Girard
 */

public class BECoalNEOutputObject extends DataOutputObject implements Serializable
    {
/**
 * Unique identifier for the experiment this data is tied to.
 */
    int     m_exptID;  
    int     m_actionIndex;
    int     m_period;
    int     m_round;
    int     m_node;
    int     m_coalition;
    int     m_totalEarnings;
        
    public BECoalNEOutputObject ()
        {
        }
    public BECoalNEOutputObject(int exptID, int ai, int p, int r, int n, int c, int earnings)
        {
        m_exptID = exptID;
        m_actionIndex = ai;
        m_period = p;
        m_round = r;
        m_node = n;
        m_coalition = c;
        m_totalEarnings = earnings;
        }
    public BECoalNEOutputObject(ResultSet rs)
        {
        try
            {
            m_exptID = rs.getInt("Expt_Out_ID_INT");
            m_actionIndex = rs.getInt("Action_Index_INT");
            m_period = rs.getInt("Period_INT");
            m_round = rs.getInt("Round_INT");
            m_node = rs.getInt("Node_INT");
            m_coalition = rs.getInt("Coalition_INT");
            m_totalEarnings = rs.getInt("Total_Earnings_INT");
            }
        catch(Exception e) 
            {
            }
        }

    public void formatInsertStatement(CallableStatement cs) throws java.sql.SQLException 
        {
        cs.setInt(1,m_exptID);
        cs.setInt(2,m_actionIndex);
        cs.setInt(3,m_period);
        cs.setInt(4,m_round);
        cs.setInt(5,m_node);
        cs.setInt(6,m_coalition);
        cs.setInt(7,m_totalEarnings);
        }

    
    public int getActionIndex()
        {
        return m_actionIndex;
        }
    public int getCoalition()
        {
        return m_coalition;
        }
    public int getExptID()
        {
        return m_exptID;
        }
    public String getInsertFormat()
        {
    // Expt_Out_ID_INT, Action_Index, Period_INT, Round_INT, Node_INT, Coalition_INT, Total_Earnings_INT
        return new String("{call up_insert_JBEExptCoalNEData (?, ?, ?, ?, ?, ?, ?)}");
        }
    public int getNode()
        {
        return m_node;
        }
    public int getPeriod()
        {
        return m_period;
        }
    public int getRound()
        {
        return m_round;
        }
    public int getTotalEarnings()
        {
        return m_totalEarnings;
        }
    
    public void setActionIndex(int value)
        {
        m_actionIndex = value;
        }
    public void setCoalition(int value)
        {
        m_coalition = value;
        }
    public void setExptID(int value)
        {
        m_exptID = value;
        }
    public void setNode(int value)
        {
        m_node = value;
        }
    public void setPeriod(int value)
        {
        m_period = value;
        }
    public void setRound(int value)
        {
        m_round = value;
        }
    public void setTotalEarnings(int value)
        {
        m_totalEarnings = value;
        }
    }
