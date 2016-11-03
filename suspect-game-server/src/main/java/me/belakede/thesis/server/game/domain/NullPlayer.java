package me.belakede.thesis.server.game.domain;

public class NullPlayer extends Player {

    public NullPlayer() {
        setId(0L);
        setCurrent(false);
        setAlive(false);
    }

    @Override
    public boolean equals(Object o) {
        return (this == o || getClass() == o.getClass());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
