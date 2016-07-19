package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Season;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Season entity.
 */
public interface SeasonRepository extends JpaRepository<Season,Long> {

    @Query("select distinct season from Season season left join fetch season.teams")
    List<Season> findAllWithEagerRelationships();

    @Query("select season from Season season left join fetch season.teams where season.id =:id")
    Season findOneWithEagerRelationships(@Param("id") Long id);

}
