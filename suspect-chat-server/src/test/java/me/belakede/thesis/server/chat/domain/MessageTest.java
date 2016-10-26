package me.belakede.thesis.server.chat.domain;

import me.belakede.junit.ExtendedEqualsHashCodeTestCase;
import me.belakede.time.TimeMachine;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.Arrays;

@RunWith(Enclosed.class)
public class MessageTest {

    @BeforeClass
    public static void setUp() {
        TimeMachine.useFixedClockAt(LocalDateTime.now().minusDays(1));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        TimeMachine.useSystemDefaultZoneClock();
    }

    public static final class MessageEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public MessageEqualsHashCodeTest(String name) {
            super(name, Message.class, Arrays.asList("sender", "time", "message"));
        }

        @Override
        protected Message createInstance() throws Exception {
            return new Message("admin", "hello user!", TimeMachine.now());
        }

        @Override
        protected Message createNotEqualInstance() throws Exception {
            return new Message("admin", "hello admin!", TimeMachine.now());
        }
    }

}