package me.belakede.thesis.server.chat.domain;

import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Enclosed.class)
public class SenderTest {

    public static final class SenderEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public SenderEqualsHashCodeTest(String name) {
            super(name, Sender.class, Arrays.asList("name", "room"));
        }

        @Override
        protected Sender createInstance() throws Exception {
            return new Sender("admin", "room");
        }

        @Override
        protected Sender createNotEqualInstance() throws Exception {
            return new Sender("demo", "room");
        }
    }

}