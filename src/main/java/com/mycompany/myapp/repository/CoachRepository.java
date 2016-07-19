package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Coach;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Coach entity.
 */
public interface CoachRepository extends JpaRepository<Coach,Long> {

}
