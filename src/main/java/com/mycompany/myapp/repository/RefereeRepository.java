package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Referee;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Referee entity.
 */
public interface RefereeRepository extends JpaRepository<Referee,Long> {

}
