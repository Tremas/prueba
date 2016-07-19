package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Stadistics;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Stadistics entity.
 */
public interface StadisticsRepository extends JpaRepository<Stadistics,Long> {

}
