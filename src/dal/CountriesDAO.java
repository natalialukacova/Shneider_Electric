package dal;

import be.Countries;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.connector.DatabaseConnector;
import dal.interfaces.ICountriesDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountriesDAO implements ICountriesDAO {
    private PreparedStatement preparedStatement;
    private DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

    @Override
    public List<Countries> getAllCountries() {
        List<Countries> Countries = new ArrayList<>();
        try {
            String sql = "SELECT * FROM countries";
            preparedStatement = databaseConnector.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Countries.add(new Countries(
                        resultSet.getInt("id"),
                        resultSet.getString("countryName")
                ));
            }
            return Countries;

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
