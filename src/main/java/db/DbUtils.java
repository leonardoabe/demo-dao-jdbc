package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbUtils {
    public static void closeConnections(PreparedStatement pstm, ResultSet rs) {
        if (pstm != null) DB.closeStatement(pstm);
        if (rs != null) DB.closeResultSet(rs);
    }
}
