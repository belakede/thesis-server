package me.belakede.thesis.server.auth.domain;

import me.belakede.thesis.junit.ExtendedEqualsHashCodeTestCase;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

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

        @Test
        public void testEqualsShouldReturnFalseWhenEnabledFieldIsOpposite() throws Exception {
            User user = (User) createInstance();
            User otherUser = (User) createInstance();
            otherUser.setEnabled(false);
            assertThat(user, not(equalTo(otherUser)));
            assertThat(otherUser, not(equalTo(user)));
        }
    }

}