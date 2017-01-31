package app.sosdemo.model;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class ContactModel {
    private String name;
    private String address;
    private String landline;
    private String contactperson;
    private String mobile;
    private String email;

    public ContactModel(String name, String address, String landline, String contactperson, String mobile, String email) {
        this.name = name;
        this.address = address;
        this.landline = landline;
        this.contactperson = contactperson;
        this.mobile = mobile;
        this.email = email;
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

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
