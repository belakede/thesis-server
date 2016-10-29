package me.belakede.thesis.server.game.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "PLAYER_CARD_UNIQUE_KEY", columnNames = {"player_id", "card"}))
public class PlayerCard implements Serializable {
    private static final long serialVersionUID = -1246550676726996649L;

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = false)
    private Player player;
    private String card;

    public PlayerCard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerCard that = (PlayerCard) o;

        return (player != null ? player.equals(that.player) : that.player == null)
                && (card != null ? card.equals(that.card) : that.card == null);

    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (card != null ? card.hashCode() : 0);
        return result;
    }
}
