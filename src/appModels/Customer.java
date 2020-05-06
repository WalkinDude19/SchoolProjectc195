package appModels;

public class Customer {
    
    String cust_id;
    String cust_name;
    String cust_address;
    String cust_address2;
    String cust_city;
    String cust_zip;
    String cust_country;
    String cust_phone;
    
    public Customer() {}



    public Customer(String id, String name, String address, String address2, String city, String zip, String country, String phone) {
        this.cust_id = id;
        this.cust_name = name;
        this.cust_address = address;
        this.cust_address2 = address2;
        this.cust_city = city;
        this.cust_zip = zip;
        this.cust_country = country;
        this.cust_phone = phone;
    }
    
   
    public Customer(String id, String name) {
        this.cust_id = id;
        this.cust_name = name;
    }    
    
        
    public String get_Cust_ID() {
        return cust_id;
    }
    public String get_Cust_Name() {
        return cust_name;
    }
    public String get_Cust_Add() {
        return cust_address;
    }
    public String get_Cust_Add2() {
        return cust_address2;
    }
    public String get_Cust_City() {
        return cust_city;
    }
    public String get_Cust_Zip() {
        return cust_zip;
    }
    public String get_Cust_Country() {
        return cust_country;
    }

    public String get_Cust_Phone() {
        return cust_phone;
    }
            
    public void set_Cust_ID(String id) {
        this.cust_id = id;
    }
    public void set_Cust_Name(String name) {
        this.cust_name = name;
    }

    public void set_Cust_Add(String address) {
        this.cust_address = address;
    }
    
    public void set_Cust_Add2(String address2) {
        this.cust_address2 = address2;
    }
    
    public void set_Cust_City(String city) {
        this.cust_city = city;
    }

    public void set_Cust_Zip(String zip) {
        this.cust_zip = zip;
    }

    public void set_Cust_Country(String country) {
        this.cust_country = country;
    }

    public void set_Cust_Phone(String phone) {
        this.cust_phone = phone;
    }
}
