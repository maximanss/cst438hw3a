package cst438hw3a.service;

/**
* This is a test program to test the CityService using Mockito.
*  The CityRepository will be mocked with a stub and 
*  the WeatherService will be mocked, so no external Rest api call for the test.
* 
* @author Max Halbert
* @since 2021-05-10
*/
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import cst438hw3a.domain.*;


@SpringBootTest
public class CityServiceTest {

 // declare as @MockBean those classes which will be stubbed in the test
 // These classes must be Spring components (such as Repositories)
 // or @Service classes.

 @MockBean
 private WeatherService mockWeatherService;

 @MockBean
 private CityRepository mockCityRepository;

 @MockBean
 private CityService cs;

 // This method is executed before each Test.
 @BeforeEach
 public void setUpEach() {
     MockitoAnnotations.initMocks(this);

 }

 /**
  * This test will test for valid city name: "LA"; and the mock database will
  * return multiple countries have city name: "LA". Expected only the first city
  * will be selected in the list
  */
 @Test
 public void test1() throws Exception {

     Country country1 = new Country("C1", "Country 1");
     Country country2 = new Country("C2", "Country 2");
     Country country3 = new Country("C3", "Country 3");
     City city1 = new City(1, "LA", "District 1", 100000, country1);
     City city2 = new City(2, "LA", "District 2", 200000, country2);
     City city3 = new City(3, "LA", "District 3", 300000, country3);
     List<City> cities = new ArrayList<City>();
     cities.add(city1);
     cities.add(city2);
     cities.add(city3);

     cs = new CityService(mockCityRepository, mockWeatherService);

     // create the stub calls and return data for weather service
     // when the getWeather method is called with name parameter "LA",
     // the stub will return the given temperature (in degrees Kelvin), UTC time, and
     // time zone.
     given(mockWeatherService.getTempAndTime("LA"))
             .willReturn(new TempAndTime(294.8, 1620602021, -25200));

     // this is the stub for the CityRepository. When given input parm of "LA",
     // it will return a list of cities.
     given(mockCityRepository.findByName("LA")).willReturn(cities);

     ResponseEntity<CityInfo> cityResponse = cs.getCityInfo("LA");

     // verify that result is as expected
     assertThat(cityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

     CityInfo cityResult = cityResponse.getBody();

     CityInfo expectedResult = new CityInfo(1, "LA", "C1", "Country 1", "District 1", 100000,
             71.0, "4:13 PM");

     // System.out.println(cityResult);
     // System.out.println(expectedResult);

     // compare actual return data with expected data
     // MUST implement .equals( ) method for CityInfo class.
     assertThat(cityResult).isEqualTo(expectedResult);
 }

 /**
  * This test will test CityService if city name is invalid
  */
 @Test
 public void test2() throws Exception {

     List<City> cities = new ArrayList<City>(); // empty cities list returned from invalid city
                                                // name

     // replace the CityRepository and WeatherService with Mock stubs in the
     // CityService
     cs = new CityService(mockCityRepository, mockWeatherService);

     // create the stub calls and return data for weather service
     // when the getWeather method is called with name parameter "LA",
     // the stub will return the given temperature (in degrees Kelvin), UTC time, and
     // time zone.
     given(mockWeatherService.getTempAndTime("InvalidCity"))
             .willReturn(new TempAndTime(0, 0, 0));

     // this is the stub for the CityRepository. When given input parm of
     // "InvalidCity",
     // it will return a list of cities, which is empty in this test case.
     given(mockCityRepository.findByName("InvalidCity")).willReturn(cities);

     ResponseEntity<CityInfo> cityResponse = cs.getCityInfo("InvalidCity");

     // verify that result is as expected
     assertThat(cityResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

 }
}
