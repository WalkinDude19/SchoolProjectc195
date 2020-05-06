package appModels;


public class Appointment {
    
    String appt_id;
    String appt_name;
    String appt_title; 
    String appt_description; 
    String appt_location; 
    String appt_contact; 
    String appt_type; 
    String appt_url;
    String appt_date;
    String appt_start_time;
    String appt_end_time;
    
    public Appointment() {}
    
    public Appointment(String id, String name, String title, String description, String location, String contact, String type, String url, String date, String start, String end) {
        this.appt_id = id;
        this.appt_name = name;
        this.appt_title = title;
        this.appt_description = description;
        this.appt_location = location;
        this.appt_contact = contact;
        this.appt_type = type;
        this.appt_url = url;
        this.appt_date = date;
        this.appt_start_time = start;
        this.appt_end_time = end;
    }
    
    
    


    public Appointment(String id, String name, String title, String description, String location, String type, String date, String start, String end) {
        this.appt_id = id;
        this.appt_name = name;
        this.appt_title = title;
        this.appt_description = description;
        this.appt_location = location;
        this.appt_type = type;
        this.appt_date = date;
        this.appt_start_time = start;
        this.appt_end_time = end;
    }

    public String get_Appt_ID() {
        return appt_id;
    }
    public String get_Appt_Name() {
        return appt_name;
    }
    public String get_Appt_Title() {
        return appt_title;
    }

    public String get_Appt_Desc() {
        return appt_description;
    }

    public String get_Appt_Loc() {
        return appt_location;
    }
    public String get_Appt_Contact() {
        return appt_contact;
    }

    public String get_Appt_Type() {
        return appt_type;
    }
    public String get_Appt_URL() {
        return appt_url;
    }
    public String get_Appt_Date() {
        return appt_date;
    }
    public String get_Appt_Start_Time() {
        return appt_start_time;
    }
    public String get_Appt_End_Time() {
        return appt_end_time;
    }
    public void set_Appt_ID(String id) {
        this.appt_id = id;
    }    
    public void set_Appt_Name(String name) {
        this.appt_name = name;
    }
    public void set_Appt_Title(String title) {
        this.appt_title = title;
    }
    public void set_Appt_Desc(String description) {
        this.appt_description = description;
    }

    public void set_Appt_Loc(String location) {
        this.appt_location = location;
    }
    public void set_Appt_Contact(String contact) {
        this.appt_contact = contact;
    }

    public void set_Appt_Type(String type) {
        this.appt_type = type;
    }
    public void set_Appt_URL(String url) {
        this.appt_url = url;
    }
    public void set_Appt_Date(String date) {
        this.appt_date = date;
    }
    public void set_Appt_Start_Time(String start) {
        this.appt_start_time = start;
    }
    public void set_Appt_End_Time(String end) {
        this.appt_end_time = end;
    }
}