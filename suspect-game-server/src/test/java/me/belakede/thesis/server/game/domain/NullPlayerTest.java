package me.belakede.thesis.server.game.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class NullPlayerTest {

    @Test
    public void testNullPlayerIsDead() {
        NullPlayer actual = new NullPlayer();
        assertThat(actual.isAlive(), is(false));
    }

    @Test
    public void testNullPlayerIdIsZero() {
        NullPlayer actual = new NullPlayer();
        assertThat(actual.getId(), is(0L));
    }

    @Test
    public void testNullPlayerIsNotTheCurrentPlayer() {
        NullPlayer actual = new NullPlayer();
        assertThat(actual.isCurrent(), is(false));
    }

    @Test
    public void testNullPlayerEqualsAlwaysTrueWhenOtherIsANullPlayer() {
        NullPlayer actual = new NullPlayer();
        assertThat(actual, is(new NullPlayer()));
        actual.setAlive(true);
        assertThat(actual, is(new NullPlayer()));
        actual.setId(12L);
        assertThat(actual, is(new NullPlayer()));
    }


}