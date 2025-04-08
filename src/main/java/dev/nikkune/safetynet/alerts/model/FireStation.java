package dev.nikkune.safetynet.alerts.model;

public class FireStation {
    private String address;
    private int station;

    public FireStation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    /**
     * Gets the address of this FireStation.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of this FireStation.
     *
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the station number of this FireStation.
     *
     * @return the station number
     */
    public int getStation() {
        return station;
    }

    /**
     * Sets the station number of this FireStation.
     *
     * @param station the new station number
     */
    public void setStation(int station) {
        this.station = station;
    }
}
