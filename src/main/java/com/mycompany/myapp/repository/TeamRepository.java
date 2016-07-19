package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Team;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Team entity.
 */
public interface TeamRepository extends JpaRepository<Team,Long> {

    @Query("select distinct team from Team team left join fetch team.partners")
    List<Team> findAllWithEagerRelationships();

    @Query("select team from Team team left join fetch team.partners where team.id =:id")
    Team findOneWithEagerRelationships(@Param("id") Long id);

}
