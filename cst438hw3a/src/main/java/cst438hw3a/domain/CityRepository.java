package cst438hw3a.domain;

/**
 * This is the interface to the city table in world database
 * 
 * @author Max Halbert
 * @since 2021-05-09
 */

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    List<City> findByName(String name);

}
