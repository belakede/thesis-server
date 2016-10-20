package me.belakede.thesis.server.note.domain;

import me.belakede.thesis.game.equipment.Marker;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "NOTE_UNIQUE_KEY", columnNames = {"author_id", "card", "owner"}))
public class Note {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Author author;

    @Column(nullable = false, updatable = false)
    private String card;

    @Column(nullable = false, updatable = false)
    private String owner;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Marker marker;

    public Note() {
    }

    public Note(Author author, String owner, String card, Marker marker) {
        this.author = author;
        this.owner = owner;
        this.card = card;
        this.marker = marker;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        return author != null ? author.equals(note.author) : note.author == null
                && (owner != null ? owner.equals(note.owner) : note.owner == null
                && (card != null ? card.equals(note.card) : note.card == null && marker == note.marker));
    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (card != null ? card.hashCode() : 0);
        result = 31 * result + (marker != null ? marker.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", author=" + author +
                ", owner=" + owner +
                ", card='" + card + '\'' +
                ", marker=" + marker +
                '}';
    }
}
