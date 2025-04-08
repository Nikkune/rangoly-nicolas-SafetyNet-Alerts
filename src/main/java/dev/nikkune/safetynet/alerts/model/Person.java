package dev.nikkune.safetynet.alerts.model;

public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    public Person(String firstName, String lastName, String address, String city, String zip, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Gets the first name of the person.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the person.
     *
     * @param firstName new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the person.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the person.
     *
     * @param lastName new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the address of the person.
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the person.
     *
     * @param address new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the city of the person.
     *
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the person.
     *
     * @param city new city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the zip code of the person.
     *
     * @return zip code
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the zip code of the person.
     *
     * @param zip new zip code
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Gets the phone number of the person.
     *
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the person.
     *
     * @param phone new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the email address of the person.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the person.
     *
     * @param email new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
