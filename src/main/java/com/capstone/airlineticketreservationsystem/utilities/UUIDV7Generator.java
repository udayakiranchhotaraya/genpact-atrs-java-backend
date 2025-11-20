package com.capstone.airlineticketreservationsystem.utilities;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class UUIDV7Generator {

    private UUIDV7Generator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /**
     * Utility function to generate a v7 UUID.
     *
     * @return a UUID v7 instance
     */
    public static UUID generateUUIDV7() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
