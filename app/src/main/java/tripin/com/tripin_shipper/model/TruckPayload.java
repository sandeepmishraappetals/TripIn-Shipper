package tripin.com.tripin_shipper.model;

import java.io.Serializable;

/**
 * Created by Android on 13/09/16.
 */
public class TruckPayload implements Serializable {
    String Payload_id;
    String Payload;
    String Total;
    String Price_Per_Ton;

   public TruckPayload(){}
    public TruckPayload(String Payload_id, String Payload, String Total,String Price_Per_Ton)
    {
        this.Payload_id = Payload_id;
        this.Payload =Payload;
        this.Total = Total;
        this.Price_Per_Ton = Price_Per_Ton;
    }
    public String getPayload_id() {
        return Payload_id;
    }

    public void setPayload_id(String payload_id) {
        this.Payload_id = payload_id;
    }

    public String getPayload() {
        return Payload;
    }

    public void setPayload(String payload) {
        this.Payload = payload;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        this.Total = total;
    }

    public String getPrice_Per_Ton() {
        return Price_Per_Ton;
    }

    public void setPrice_Per_Ton(String price_Per_Ton) {
        this.Price_Per_Ton = price_Per_Ton;
    }

}
