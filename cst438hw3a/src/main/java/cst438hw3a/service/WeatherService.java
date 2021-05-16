package cst438hw3a.service;

/**
 * This is the WeatherService class that creates Rest Call to the external weather web server to get
 *  weather information of given city.
 * 
 * @author Max Halbert
 * @since 2021-05-09
 */

import cst438hw3a.domain.TempAndTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private RestTemplate restTemplate;

    private String weatherUrl;
    private String apiKey;

    // retrieve url and apikey for weather service from application.properties file
    public WeatherService(@Value("${weather.url}") final String weatherUrl,
            @Value("${weather.apikey}") final String apiKey) {
        this.restTemplate = new RestTemplate();
        this.weatherUrl = weatherUrl;
        this.apiKey = apiKey;
    }

    // it returns the raw temperature, time and timezone
    public TempAndTime getTempAndTime(String cityName) {
        ResponseEntity<JsonNode> response = restTemplate
                .getForEntity(weatherUrl + "?q=" + cityName + "&appid=" + apiKey, JsonNode.class);
        JsonNode json = response.getBody();
        log.info("Status code from weather server:" + response.getStatusCodeValue());
        double temp = json.get("main").get("temp").asDouble();
        long time = json.get("dt").asLong();
        int timezone = json.get("timezone").asInt();

        return new TempAndTime(temp, time, timezone);
    }

}