package me.belakede.thesis.server.note.domain;

import me.belakede.thesis.game.equipment.Room;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.game.equipment.Weapon;

import javax.persistence.*;

@Entity
public class Suggestion {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(optional = false)
    private Author author;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Suspect suspect;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Room room;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Weapon weapon;

    public Suggestion() {
        // It's necessary for entity classes
    }

    public Suggestion(Author author, Suspect suspect, Room room, Weapon weapon) {
        this.author = author;
        this.suspect = suspect;
        this.room = room;
        this.weapon = weapon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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

        Suggestion that = (Suggestion) o;

        return author != null ? author.equals(that.author) : that.author == null
                && suspect == that.suspect && room == that.room && weapon == that.weapon;
    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (suspect != null ? suspect.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + (weapon != null ? weapon.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "id=" + id +
                ", author=" + author +
                ", suspect=" + suspect +
                ", room=" + room +
                ", weapon=" + weapon +
                '}';
    }
}
