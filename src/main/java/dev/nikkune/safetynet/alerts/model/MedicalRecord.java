package dev.nikkune.safetynet.alerts.model;

import java.util.ArrayList;

public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private ArrayList<String> medications;
    private ArrayList<String> allergies;

    public MedicalRecord(String firstName, String lastName, String birthdate, ArrayList<String> medications, ArrayList<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
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
     * Gets the birthdate of the person in the format MM/dd/yyyy.
     *
     * @return birthdate
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the birthdate of the person in the format MM/dd/yyyy.
     *
     * @param birthdate new birthdate
     */
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Gets the list of medications for the person.
     *
     * @return list of medications
     */
    public ArrayList<String> getMedications() {
        return medications;
    }

    /**
     * Sets the list of medications for the person.
     *
     * @param medications new list of medications
     */
    public void setMedications(ArrayList<String> medications) {
        this.medications = medications;
    }

    /**
     * Gets the list of allergies for the person.
     *
     * @return list of allergies
     */
    public ArrayList<String> getAllergies() {
        return allergies;
    }

    /**
     * Sets the list of allergies for the person.
     *
     * @param allergies new list of allergies
     */
    public void setAllergies(ArrayList<String> allergies) {
        this.allergies = allergies;
    }

    /**
     * Adds a medication to the list of medications for the person.
     *
     * @param medication the medication to add
     */
    public void addMedication(String medication) {
        this.medications.add(medication);
    }

    /**
     * Removes a medication from the list of medications for the person.
     *
     * @param medication the medication to remove
     */
    public void removeMedication(String medication) {
        this.medications.remove(medication);
    }

    /**
     * Adds an allergy to the list of allergies for the person.
     *
     * @param allergy the allergy to add
     */
    public void addAllergy(String allergy) {
        this.allergies.add(allergy);
    }

    /**
     * Removes an allergy from the list of allergies for the person.
     *
     * @param allergy the allergy to remove
     */
    public void removeAllergy(String allergy) {
        this.allergies.remove(allergy);
    }
}
