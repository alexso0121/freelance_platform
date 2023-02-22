package com.example.OrderService.Service;

import com.example.OrderService.Entity.Location;
import com.example.OrderService.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

//class for storing 18 district info at the beginning in running
@Configuration
public class LocationInitService {

    @Autowired
    private LocationRepository locationRepository;

    @Bean
    public void insertLocation(){
        if(locationRepository.findAll().size()==0){
            saveLocation();
        }
    }
    public void saveLocation(){
        locationRepository.save(new Location(1,"Island"));
        locationRepository.save(new Location(9,"Yuen Long"));
        locationRepository.save(new Location(10,"Kowloon City"));
        locationRepository.save(new Location(11,"Kwun Tong"));
        locationRepository.save(new Location(12,"Sham Shui Po"));
        locationRepository.save(new Location(13,"Wong Tai Sin"));
        locationRepository.save(new Location(14,"Yau Tsim Wong"));
        locationRepository.save(new Location(15,"Central and Western"));
        locationRepository.save(new Location(16,"Eastern"));
        locationRepository.save(new Location(17,"Southern"));
        locationRepository.save(new Location(18,"Wan Chai"));


    }
}
