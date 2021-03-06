package girard.sc.wl.sql;

import girard.sc.wl.io.WLServerConnection;
import girard.sc.wl.io.msg.WLMessage;

import java.sql.ResultSet;
import java.sql.Statement;

public class WLAccessGroupListReq extends WLQuery
    { 

    public WLAccessGroupListReq (WLServerConnection wlsc, WLMessage wlm)
        {
        super("weblabDB", wlsc, wlm);
        }

    public ResultSet runQuery()
        {
        synchronized (m_DBA)
            {
            try 
                {
                createConnection();

                // get a Statement object from the Connection
                //
                Statement stmt = m_DB.createStatement();
 
                ResultSet rs = stmt.executeQuery("SELECT DISTINCT App_Name_VC, App_Desc_VC, App_ID FROM view_Apps_for_User_ID WHERE ((ID_INT = "+m_userID+" OR App_Owner_INT = "+m_userID+") AND App_Type_INT = 4)");

                // closeConnection();

                return rs;
                }
            catch( Exception e ) 
                {
                m_wlsc.addToLog(e.getMessage());
                closeConnection();
                return null;
                }
            }
        }

    public boolean runUpdate()
        {
        return false;
        }
    }