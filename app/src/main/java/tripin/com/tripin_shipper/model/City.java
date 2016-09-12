package tripin.com.tripin_shipper.model;

/**
 * Created by SUMEET on 05-09-2016.
 */
public class City {
    public String city_name;
    public String city_id;
    public City(){}
    public City(String City_name, String City_id)
    {

        this.city_name = City_name;
        this.city_id = City_id;

    }
    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
}
