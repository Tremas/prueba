package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Stadistics.
 */
@Entity
@Table(name = "stadistics")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stadistics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "baskets")
    private Integer baskets;

    @Column(name = "faults")
    private Integer faults;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBaskets() {
        return baskets;
    }

    public void setBaskets(Integer baskets) {
        this.baskets = baskets;
    }

    public Integer getFaults() {
        return faults;
    }

    public void setFaults(Integer faults) {
        this.faults = faults;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stadistics stadistics = (Stadistics) o;
        return Objects.equals(id, stadistics.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stadistics{" +
            "id=" + id +
            ", baskets='" + baskets + "'" +
            ", faults='" + faults + "'" +
            '}';
    }
}
