package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Game implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "local_result")
    private Integer localResult;

    @Column(name = "away_result")
    private Integer awayResult;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne
    @JoinColumn(name = "referee_id")
    private Referee referee;

    @ManyToOne
    @JoinColumn(name = "local_team_id")
    private Team localTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @OneToMany(mappedBy = "game")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Stadistics> stadisticss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getLocalResult() {
        return localResult;
    }

    public void setLocalResult(Integer localResult) {
        this.localResult = localResult;
    }

    public Integer getAwayResult() {
        return awayResult;
    }

    public void setAwayResult(Integer awayResult) {
        this.awayResult = awayResult;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Team getLocalTeam() {
        return localTeam;
    }

    public void setLocalTeam(Team team) {
        this.localTeam = team;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team team) {
        this.awayTeam = team;
    }

    public Set<Stadistics> getStadisticss() {
        return stadisticss;
    }

    public void setStadisticss(Set<Stadistics> stadisticss) {
        this.stadisticss = stadisticss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", localResult='" + localResult + "'" +
            ", awayResult='" + awayResult + "'" +
            '}';
    }
}
