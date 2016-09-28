package tripin.com.tripin_shipper.model;

import java.io.Serializable;

/**
 * Created by Android on 13/09/16.
 */
public class TruckBody implements Serializable {
    String body_type_id;
    String type;
     String Total;


    public TruckBody(){}
    public TruckBody(String body_type_id, String type, String Total)
    {
        this.body_type_id = body_type_id;
        this.type =type;
        this.Total = Total;

    }
    public String getbody_type_id() {
        return body_type_id;
    }

    public void setbody_type_id(String body_type_id) {
        this.body_type_id = body_type_id;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        this.Total = total;
    }

}
