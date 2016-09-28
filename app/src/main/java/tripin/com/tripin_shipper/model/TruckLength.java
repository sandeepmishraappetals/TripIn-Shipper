package tripin.com.tripin_shipper.model;

import java.io.Serializable;

/**
 * Created by Android on 13/09/16.
 */
public class TruckLength implements Serializable {
    String length_id;
    String length;
    String Total;


    public TruckLength(){}
    public TruckLength(String length_id, String length, String Total)
    {
        this.length_id = length_id;
        this.length =length;
        this.Total = Total;

    }
    public String getlength_id() {
        return length_id;
    }

    public void setlength_id(String length_id) {
        this.length_id = length_id;
    }

    public String getlength() {
        return length;
    }

    public void setlength(String length) {
        this.length = length;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        this.Total = total;
    }

}
