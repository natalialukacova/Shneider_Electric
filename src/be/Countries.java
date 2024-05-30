package be;

public class Countries {
    private final int countryId;
    private final String countryName;

    public int getCountryId() {
        return countryId;
    }


    public String getCountryName() {
        return countryName;
    }


    public Countries(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    @Override
    public String toString(){
        return countryName;
    }
}
