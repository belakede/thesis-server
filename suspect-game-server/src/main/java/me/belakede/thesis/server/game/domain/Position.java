package me.belakede.thesis.server.game.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Position implements Serializable {
    private static final long serialVersionUID = -8266545070161240727L;

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(orphanRemoval = true)
    private Player player;
    @Column(name = "row_index", nullable = false)
    private Integer row;
    @Column(name = "column_index", nullable = false)
    private Integer column;

    public Position() {
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

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Position position = (Position) o;

        return (player != null ? player.equals(position.player) : position.player == null)
                && (row != null ? row.equals(position.row) : position.row == null)
                && (column != null ? column.equals(position.column) : position.column == null);
    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (row != null ? row.hashCode() : 0);
        result = 31 * result + (column != null ? column.hashCode() : 0);
        return result;
    }
}
