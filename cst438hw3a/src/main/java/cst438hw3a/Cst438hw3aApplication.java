package cst438hw3a;

/**
 * This application is a web application which provide two kind of URL inputs from user 
 * 1. Example: http://localhost:8080/cities/Miami
 *      It will return a html page about the Miami city's info that includes current temperature and time;
 *      It also allow the user to book a trip the the city that the user is looking for.
 * 2. Example: http://localhost:8080/api/cities/Miami
 *      It will return a JSON format string of Miami's info instead
 *      
 * @author Max Halbert
 * @since 2021-05-09
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Cst438hw3aApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cst438hw3aApplication.class, args);
	}

}
