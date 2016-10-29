package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Mystery implements Serializable {

    private static final long serialVersionUID = 7236206386937594829L;

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(orphanRemoval = true)
    private Game game;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Suspect suspect;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Room room;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Weapon weapon;

    public Mystery() {
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

    public Suspect getSuspect() {
        return suspect;
    }

    public void setSuspect(Suspect suspect) {
        this.suspect = suspect;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Mystery mystery = (Mystery) o;

        return (game != null ? game.equals(mystery.game) : mystery.game == null)
                && (suspect == mystery.suspect)
                && (room == mystery.room)
                && (weapon == mystery.weapon);
    }

    @Override
    public int hashCode() {
        int result = game != null ? game.hashCode() : 0;
        result = 31 * result + (suspect != null ? suspect.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + (weapon != null ? weapon.hashCode() : 0);
        return result;
    }


}
