package girard.sc.expt.sql;

import girard.sc.wl.io.WLServerConnection;
import girard.sc.wl.io.msg.WLMessage;
import girard.sc.wl.sql.WLQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Used to run the actual database query for getting a list of saved Experiment
 * files from the database.  Retrieves files based on the user ID or the access
 * group passed in.
 * <p>
 * <br> Started: 2001
 * <br> Modified: 10-23-2002
 * <p>
 * @author Dudley Girard
 * @version ExNet III 3.1
 * @since JDK1.1
 */

public class ExptFileListReq extends WLQuery
    { 
    String m_ag = null;

    public ExptFileListReq (String ag, WLServerConnection wlsc, WLMessage wlm)
        {
        super("exptDB", wlsc, wlm);
        m_ag = ag;
        }

    public ResultSet runQuery() 
        {
        synchronized (m_DBA)
            {
            try 
                {
                if (!createConnection())
                    return null;

                // get a Statement object from the Connection
                //
                Statement stmt = m_DB.createStatement();
 
                ResultSet rs;

                if (m_ag == null)
                    rs = stmt.executeQuery("SELECT * FROM Experiments_T WHERE ID_INT = "+m_userID+" AND App_ID IS NULL");
                else
                    rs = stmt.executeQuery("SELECT * FROM Experiments_T WHERE App_ID = '"+m_ag+"'");

                // closeConnection();

                return rs;
                }
            catch(SQLException sqle) 
                { 
                m_wlsc.addToLog(""+sqle); 
                closeConnection();
                }
            }
        return null;
        }

    public boolean runUpdate()
        {
        return true;
        }
    }
