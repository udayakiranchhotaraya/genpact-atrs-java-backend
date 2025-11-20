package com.capstone.airlineticketreservationsystem.flights.services;

import com.capstone.airlineticketreservationsystem.flights.dtos.AirlineDTO;
import org.springframework.stereotype.Service;

import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAirlineRequest;
import com.capstone.airlineticketreservationsystem.flights.models.Airline;
import com.capstone.airlineticketreservationsystem.flights.repositories.AirlineRepositoryDAO;

@Service
public class AirlineService {

    public AirlineService(AirlineRepositoryDAO airlineRepositoryDAO) {
        this.airlineRepositoryDAO = airlineRepositoryDAO;
    }

    public AirlineRepositoryDAO airlineRepositoryDAO;

    public AirlineDTO createAirline(CreateAirlineRequest airlineRequest) {

        Airline airline = new Airline(
                airlineRequest.getAirlineCode(),
                airlineRequest.getAirlineName(),
                airlineRequest.getCountry(),
                airlineRequest.getLogoUrl()
        );

        Airline savedAirline = airlineRepositoryDAO.save(airline);

        return new AirlineDTO(
                savedAirline.getAirlineUUID(),
                savedAirline.getAirlineCode(),
                savedAirline.getAirlineName(),
                savedAirline.getCountry(),
                savedAirline.getLogoURL(),
                savedAirline.getCreatedAt()
        );
    }
}
