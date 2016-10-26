package me.belakede.thesis.server.chat.domain;

import junitx.extensions.EqualsHashCodeTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class SenderTest {

    public static final class SenderEqualsHashCodeTest extends EqualsHashCodeTestCase {

        public SenderEqualsHashCodeTest(String name) {
            super(name);
        }

        @Override
        protected Sender createInstance() throws Exception {
            return new Sender("admin");
        }

        @Override
        protected Sender createNotEqualInstance() throws Exception {
            return new Sender("demo");
        }
    }

}