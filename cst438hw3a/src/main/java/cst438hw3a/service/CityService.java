package cst438hw3a.service;

/**
 * This are two services in this class 
 * 1. getCityInfo - gets city information from database and external weather server.
 * 2. requestReservation - send the reservation request message to the Rabbit fanout exchange
 * 
 * @author Max Halbert
 * @since 2021-05-09
 */

import java.util.List;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import cst438hw3a.domain.*;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanout;

    // this constructor is used in test to stub out
    // cityRepository and weatherService.
    public CityService(CityRepository mockRepository, WeatherService mockWeather) {
        this.cityRepository = mockRepository;
        this.weatherService = mockWeather;
    }

    // it will return the city info in JSON format.
    public ResponseEntity<CityInfo> getCityInfo(String cityName) {
        // look up city info from database. Might be multiple cities with same name.
        List<City> cities = cityRepository.findByName(cityName);
        if (cities.size() == 0) {

            // city name not found. Send 404 return code.
            // return new CityInfo(HttpStatus.NOT_FOUND);
            return new ResponseEntity<CityInfo>(HttpStatus.NOT_FOUND);

        } else {
            // in case of multiple cities, take the first one.
            City city = cities.get(0);

            // call weather service to get temperature and time
            TempAndTime weather = weatherService.getTempAndTime(city.getName());
            city.setWeather(weather);

            CityInfo cityInfo = new CityInfo(city);

            return new ResponseEntity<CityInfo>(cityInfo, HttpStatus.OK);
        }

    }

    /**
     * It will enqueue a message to a RabbitMQ exchange
     * 
     */
    public void requestReservation(String cityName, String level, String email) {

        String msg = "{\"cityName\": \"" + cityName + "\" \"level\": \"" + level
                + "\" \"email\": \"" + email + "\"}";
        System.out.println("Sending message:" + msg);
        rabbitTemplate.convertSendAndReceive(fanout.getName(), "", // routing key none.
                msg);
    }
}
