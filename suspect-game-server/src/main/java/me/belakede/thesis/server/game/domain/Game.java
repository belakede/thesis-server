package me.belakede.thesis.server.game.domain;

import me.belakede.thesis.game.board.BoardType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Game implements Serializable {

    private static final long serialVersionUID = 6620498616614285764L;
    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Mystery mystery;
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, updatable = false, nullable = false)
    private String roomId;
    @Column(updatable = false, nullable = false)
    private LocalDateTime created;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    public Game() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Mystery getMystery() {
        return mystery;
    }

    public void setMystery(Mystery mystery) {
        this.mystery = mystery;
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

        return (roomId != null ? roomId.equals(game.roomId) : game.roomId == null)
                && (created != null ? created.equals(game.created) : game.created == null)
                && (boardType == game.boardType && status == game.status);
    }

    @Override
    public int hashCode() {
        int result = roomId != null ? roomId.hashCode() : 0;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (boardType != null ? boardType.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

}
