package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Partner;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Partner entity.
 */
public interface PartnerRepository extends JpaRepository<Partner,Long> {

}
