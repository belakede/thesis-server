package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.equipment.Suspect;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "PLAYER_UNIQUE_KEY", columnNames = {"game_id", "name"}))
public class Player implements Serializable {
    private static final long serialVersionUID = -5448865337444775717L;

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = false)
    private Game game;
    @Column(nullable = false, updatable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Suspect suspect;
    @Column(nullable = false)
    private boolean alive;
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<PlayerCard> playerCards;

    public Player() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Suspect getSuspect() {
        return suspect;
    }

    public void setSuspect(Suspect suspect) {
        this.suspect = suspect;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Set<PlayerCard> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(Set<PlayerCard> playerCards) {
        this.playerCards = playerCards;
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

        return (alive == player.alive)
                && (game != null ? game.equals(player.game) : player.game == null)
                && (name != null ? name.equals(player.name) : player.name == null)
                && (suspect == player.suspect);
    }

    @Override
    public int hashCode() {
        int result = game != null ? game.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (suspect != null ? suspect.hashCode() : 0);
        result = 31 * result + (alive ? 1 : 0);
        return result;
    }
}
