package com.example.OrderService.Service;

import com.example.OrderService.Repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    public String getLocation(int address_id){
        return locationRepository.getLocation(address_id);
    }
}
