package dal.connector;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {

    private SQLServerDataSource dataSource;
    private static DatabaseConnector instance;

    public static DatabaseConnector getInstance(){
        if (instance==null){
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public DatabaseConnector() {
        dataSource = new SQLServerDataSource();
        dataSource.setDatabaseName("SchneiderElectric");
        dataSource.setUser("CSe2023b_e_19");
        dataSource.setPassword("CSe2023bE19#23 ");
        dataSource.setServerName("EASV-DB4");
        dataSource.setPortNumber(1433);
        dataSource.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException
    {
        return dataSource.getConnection();
    }

    public static void main(String[] args) throws SQLException
    {
        DatabaseConnector dataBaseConnector = new DatabaseConnector();

        try(Connection connection = dataBaseConnector.getConnection())
        {
            System.out.println("is it open?" + !connection.isClosed());
        }
    }
}
