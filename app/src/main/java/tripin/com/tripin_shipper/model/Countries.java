package tripin.com.tripin_shipper.model;

import java.util.List;

/**
 * Created by Android on 31/08/16.
 */
public class Countries {
    public String name;
    public String code;
    public String city_name;
    public String city_id;
    public Countries()
    {

    }
public Countries(String name, String code, String City_name, String City_id)
{
this.name= name;
this.code = code;
    this.city_name = City_name;
    this.city_id = City_id;

}
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
