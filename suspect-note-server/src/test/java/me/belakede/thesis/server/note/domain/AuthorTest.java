package me.belakede.thesis.server.note.domain;

import me.belakede.junit.ExtendedEqualsHashCodeTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Enclosed.class)
public class AuthorTest {

    public static final class AuthorEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public AuthorEqualsHashCodeTest(String name) {
            super(name, Author.class, Arrays.asList("name", "room"));
        }

        @Override
        protected Author createInstance() throws Exception {
            return new Author("admin", "test-room");
        }

        @Override
        protected Author createNotEqualInstance() throws Exception {
            return new Author("demo", "test-room");
        }
    }

}