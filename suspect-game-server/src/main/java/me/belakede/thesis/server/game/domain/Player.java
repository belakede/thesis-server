package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.equipment.Suspect;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "PLAYER_UNIQUE_KEY", columnNames = {"game_id", "username"}))
public class Player implements Serializable {
    private static final long serialVersionUID = -5448865337444775717L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Game game;

    @Column(nullable = false, updatable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Suspect figurine;

    @Column(nullable = false)
    private Boolean alive;

    @Column(nullable = false)
    private Integer ordinalNumber;

    @Column(nullable = false)
    private Boolean current;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "player", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<PlayerCard> cards;

    public Player() {
        // It's required for an entity
    }

    public Player(Suspect figurine, Set<PlayerCard> cards) {
        this.figurine = figurine;
        this.cards = new HashSet<>(cards);
        this.alive = true;
        this.current = false;
    }

    public Player(String username, Suspect figurine, Integer ordinalNumber) {
        this(null, username, figurine, ordinalNumber);
    }

    public Player(Game game, String username, Suspect figurine, Integer ordinalNumber) {
        this(game, username, figurine, ordinalNumber, true, false);
    }

    public Player(Game game, String username, Suspect figurine, Integer ordinalNumber, Boolean alive, Boolean current) {
        this.game = game;
        this.username = username;
        this.figurine = figurine;
        this.alive = alive;
        this.ordinalNumber = ordinalNumber;
        this.current = current;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Suspect getFigurine() {
        return figurine;
    }

    public void setFigurine(Suspect figurine) {
        this.figurine = figurine;
    }

    public Boolean isAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public Integer getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(Integer ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public Boolean isCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Set<PlayerCard> getCards() {
        return cards;
    }

    public void setCards(Set<PlayerCard> cards) {
        this.cards = cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Player player = (Player) o;

        return (game != null ? game.equals(player.game) : player.game == null)
                && (username != null ? username.equals(player.username) : player.username == null);

    }

    @Override
    public int hashCode() {
        int result = game != null ? game.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
