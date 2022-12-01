package com.api.parkingcontrol.services.exceptions;

public class ParkingSpotNotFoundException extends RuntimeException {
    public ParkingSpotNotFoundException(String msg) {
        super(msg);
    }
}
