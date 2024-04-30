package dal.interfaces;

import be.Countries;

import java.util.List;

public interface ICountriesDAO {
    List<Countries> getAllCountries();
    void addCountry(Countries countries);
    void updateCountry(Countries countries);
}
