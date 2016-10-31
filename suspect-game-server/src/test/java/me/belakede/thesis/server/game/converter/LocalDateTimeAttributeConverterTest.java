package me.belakede.thesis.server.game.converter;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LocalDateTimeAttributeConverterTest {

    private LocalDateTimeAttributeConverter testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new LocalDateTimeAttributeConverter();
    }

    @Test
    public void testConvertToDatabaseColumnShouldReturnATimestamp() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp actual = testSubject.convertToDatabaseColumn(now);
        assertThat(actual.toLocalDateTime(), is(now));
    }

    @Test
    public void testConvertToEntityAttributeShouldReturnALocalDateTime() {
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        Timestamp nowTimestamp = Timestamp.valueOf(nowLocalDateTime);
        LocalDateTime actual = testSubject.convertToEntityAttribute(nowTimestamp);
        assertThat(actual, is(nowLocalDateTime));
    }
}