package cst438hw3a.domain;

/**
 * This is the interface to the country table in world database
 * 
 * @author Max Halbert
 * @since 2021-05-09
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    Country findByCode(String code);
}
