package cst438hw3a.controller;

/**
 * This is the controller to handle URL like http://localhost:8080/cities/Miami
 *      It will return a html page about the Miami city's info that includes current temperature and time,
 *      and also send a request to the backend to book a trip to Miami with user's selected level of comfort.   
 * 
 * @author Max Halbert
 * @since 2021-05-09
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cst438hw3a.service.CityService;
import cst438hw3a.domain.*;

@Controller
public class CityController {

    @Autowired
    CityService cityService;

    // display the city info and trip offer to the city
    @GetMapping("/cities/{city}")
    public String getCityInfo(@PathVariable("city") String cityName, Model model) {

        ResponseEntity<CityInfo> info = cityService.getCityInfo(cityName);
        if (info.getStatusCode() == HttpStatus.NOT_FOUND) {
            return "showerror"; // invalid city name
        }
        CityInfo city = info.getBody();
        model.addAttribute("city", city);
        return "showcity";
    }
    
    // process and send the reservation request to the backend server
    @PostMapping("/cities/reservation")
    public String createReservation(
            @RequestParam("city") String cityName,
            @RequestParam("level") String level,
            @RequestParam("email") String email,
            Model model) {
        model.addAttribute("city", cityName);
        model.addAttribute("level", level);
        model.addAttribute("email", email);
        cityService.requestReservation(cityName, level, email);
        System.out.println(cityName + ";" + level + ";" + email);
        return "request_reservation";
    }
    
}


