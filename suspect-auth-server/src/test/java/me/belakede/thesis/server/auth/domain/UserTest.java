package me.belakede.thesis.server.auth.domain;

import me.belakede.junit.ExtendedEqualsHashCodeTestCase;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(Enclosed.class)
public class UserTest {

    public static final class UserEqualsHashCodeTest extends ExtendedEqualsHashCodeTestCase {

        public UserEqualsHashCodeTest(String name) {
            super(name, User.class, Arrays.asList("username", "roles"));
        }

        @Override
        protected Object createInstance() throws Exception {
            return new User("admin", "password", true, new HashSet<>(Arrays.asList(Role.ADMIN)));
        }

        @Override
        protected Object createNotEqualInstance() throws Exception {
            return new User("user", "password", true, new HashSet<>(Arrays.asList(Role.USER)));
        }
    }

}