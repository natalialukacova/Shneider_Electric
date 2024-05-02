package gui.controller;

import be.Countries;
import dal.CountriesDAO;
import dal.interfaces.ICountriesDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CountryController extends JPanel{
    private JComboBox<String> countryComboBox;

    public CountryController(){
        // Set up the panel
        setLayout(new BorderLayout());

        // Initialize JComboBox
        countryComboBox = new JComboBox<>();
        add(countryComboBox, BorderLayout.CENTER);

        // Load countries into the JComboBox
        loadCountries();
    }

    private void loadCountries(){
        CountriesDAO countriesDAO = new CountriesDAO();
        try {
            List<Countries> countriesList = countriesDAO.getAllCountries();
            for (Countries country: countriesList){
                countryComboBox.addItem(country.getCountryName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading countries: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JComboBox<String> getCountryComboBox(){
        return countryComboBox;
    }

}
