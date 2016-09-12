package tripin.com.tripin_shipper.model;

import java.io.Serializable;

/**
 * Created by Android on 07/09/16.
 */
public class AddressBook implements Serializable{
    String address_id ;
    String firm_name ;
    String working_hour_from ;
    String working_hour_to;
    String survey_no;
    String address ;
    String state;
    String mobile;
    String landmark;
    String city ;
    String pincode ;
    String created_by ;
    String landline ;
    String order_address_id ;
    String name_of_person ;
    String email;

    public AddressBook()
    {}
    public AddressBook(  String address_id, String firm_name ,String working_hour_from, String working_hour_to, String survey_no, String address ,
            String state,
            String city ,
            String pincode ,
            String created_by ,
            String landline ,
            String order_address_id ,
            String name_of_person, String mobile, String landmark , String email)

    {
this.address_id = address_id;
        this.firm_name = firm_name;
        this.working_hour_from = working_hour_from;
        this.working_hour_to = working_hour_to;
        this.survey_no = survey_no;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.created_by = created_by;
        this.landline = landline;
        this.order_address_id = order_address_id;
        this.name_of_person = name_of_person;
        this.mobile = mobile;
        this.landmark = landmark;
        this.email = email;
    }
    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getFirm_name() {
        return firm_name;
    }

    public void setFirm_name(String firm_name) {
        this.firm_name = firm_name;
    }

    public String getWorking_hour_from() {
        return working_hour_from;
    }

    public void setWorking_hour_from(String working_hour_from) {
        this.working_hour_from = working_hour_from;
    }

    public String getWorking_hour_to() {
        return working_hour_to;
    }

    public void setWorking_hour_to(String working_hour_to) {
        this.working_hour_to = working_hour_to;
    }

    public String getSurvey_no() {
        return survey_no;
    }

    public void setSurvey_no(String survey_no) {
        this.survey_no = survey_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getOrder_address_id() {
        return order_address_id;
    }

    public void setOrder_address_id(String order_address_id) {
        this.order_address_id = order_address_id;
    }

    public String getName_of_person() {
        return name_of_person;
    }

    public void setName_of_person(String name_of_person) {
        this.name_of_person = name_of_person;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
