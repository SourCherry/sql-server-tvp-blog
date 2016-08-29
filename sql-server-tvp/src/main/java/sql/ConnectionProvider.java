package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by davidrichardson on 07/06/2016.
 */
public class ConnectionProvider {

    public static Connection getConnection() {
        String connectionUrl = "jdbc:sqlserver://xx.xx.xx.xx:1433;" +
                "databaseName=xxxxxxxx;user=xxxxxxxxx;password=xxxxxxxxx";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
