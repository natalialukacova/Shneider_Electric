package bll;

import be.Countries;
import dal.CountriesDAO;
import dal.interfaces.ICountriesDAO;

import java.util.List;

public class CountryManager implements ICountriesDAO {

    ICountriesDAO countriesDAO = new CountriesDAO();

    @Override
    public List<Countries> getAllCountries() {
            return countriesDAO.getAllCountries();
    }

}
