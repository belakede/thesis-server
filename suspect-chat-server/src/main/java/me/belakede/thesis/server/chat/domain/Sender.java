package me.belakede.thesis.server.chat.domain;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "SENDER_UNIQUE_KEY", columnNames = {"name", "room"}))
public class Sender {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private String room;

    public Sender() {
    }

    public Sender(String name) {
        this.name = name;
    }

    public Sender(String name, String room) {
        this.name = name;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sender sender = (Sender) o;

        if (name != null ? !name.equals(sender.name) : sender.name != null) return false;
        return room != null ? room.equals(sender.room) : sender.room == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (room != null ? room.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sender{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", room=" + room +
                '}';
    }
}
