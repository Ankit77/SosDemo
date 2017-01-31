package app.sosdemo.model;

/**
 * Created by ANKIT on 1/31/2017.
 */

public class ContactModel {
    private String OrgName;
    private String Address;
    private String Landline1;
    private String Landline2;
    private String Fax1;
    private String EmailAddress;

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLandline1() {
        return Landline1;
    }

    public void setLandline1(String landline1) {
        Landline1 = landline1;
    }

    public String getLandline2() {
        return Landline2;
    }

    public void setLandline2(String landline2) {
        Landline2 = landline2;
    }

    public String getFax1() {
        return Fax1;
    }

    public void setFax1(String fax1) {
        Fax1 = fax1;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }
}
