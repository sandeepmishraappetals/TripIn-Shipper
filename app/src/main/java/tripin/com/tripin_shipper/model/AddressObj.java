package tripin.com.tripin_shipper.model;

import java.io.Serializable;

/**
 * Created by SANJEEV on 11-09-2016.
 */
public class AddressObj implements Serializable {
    private int id;
    private String name;
    private String address;
    private String lat;
    private String lon;



    public AddressObj(int id, String name, String address,String lat, String lon){
        setId(id);
        setName(name);
        setAddress(address);
        setLat(lat);
        setLon(lon);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
