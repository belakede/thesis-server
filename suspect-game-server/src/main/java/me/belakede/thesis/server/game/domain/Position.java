package me.belakede.thesis.server.game.domain;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Position implements Serializable {
    private static final long serialVersionUID = -8266545070161240727L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private String figurine;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Game game;

    @Column(nullable = false)
    private int rowIndex;

    @Column(nullable = false)
    private int columnIndex;

    public Position() {
        // It's required for an entity
    }

    public Position(String figurine, Integer rowIndex, int columnIndex) {
        this(figurine, null, rowIndex, columnIndex);
    }

    public Position(String figurine, Game game, Integer rowIndex, int columnIndex) {
        this.figurine = figurine;
        this.game = game;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFigurine() {
        return figurine;
    }

    public void setFigurine(String figurine) {
        this.figurine = figurine;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
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

        return (figurine != null ? figurine.equals(position.figurine) : position.figurine == null)
                && (game != null ? game.equals(position.game) : position.game == null)
                && (rowIndex == position.rowIndex)
                && (columnIndex == position.columnIndex);
    }

    @Override
    public int hashCode() {
        int result = figurine != null ? figurine.hashCode() : 0;
        result = 31 * result + (game != null ? game.hashCode() : 0);
        result = 31 * result + rowIndex;
        result = 31 * result + columnIndex;
        return result;
    }
}
