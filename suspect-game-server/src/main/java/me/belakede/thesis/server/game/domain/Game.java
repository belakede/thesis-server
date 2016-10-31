package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.board.BoardType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Game implements Serializable {
    private static final long serialVersionUID = 6620498616614285764L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, updatable = false)
    private String room;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private BoardType boardType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "game", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Player> players;

    public Game() {
        // It's required for an entity
    }

    public Game(BoardType boardType, Status status) {
        this.room = UUID.randomUUID().toString();
        this.created = LocalDateTime.now();
        this.boardType = boardType;
        this.status = status;
    }

    public Game(String room, LocalDateTime created, BoardType boardType, Status status) {
        this.room = room;
        this.created = created;
        this.boardType = boardType;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public void setBoardType(BoardType boardType) {
        this.boardType = boardType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
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

        return (room != null ? room.equals(game.room) : game.room == null)
                && (created != null ? created.equals(game.created) : game.created == null)
                && (boardType == game.boardType)
                && (status == game.status);
    }

    @Override
    public int hashCode() {
        int result = room != null ? room.hashCode() : 0;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (boardType != null ? boardType.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    public enum Status {
        CREATED,
        IN_PROGRESS,
        PAUSED,
        FINISHED
    }

}
