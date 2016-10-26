package me.belakede.junit;

import junitx.extensions.EqualsHashCodeTestCase;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class ExtendedEqualsHashCodeTestCase<T> extends EqualsHashCodeTestCase {

    private final Class<T> typeClass;
    private final Set<String> nullableFields;

    public ExtendedEqualsHashCodeTestCase(String name, Class<T> typeClass, Collection<String> nullableFields) {
        super(name);
        this.typeClass = typeClass;
        this.nullableFields = new HashSet<>(nullableFields);
    }

    public final void testEqualsAgainstDifferentType() throws Exception {
        Object object = new Object();
        Object instance = createInstance();
        junitx.framework.Assert.assertNotEquals(instance, object);
    }

    public final void testEqualsAgainstNullableFields() throws Exception {
        nullableFields.forEach(f -> {
            try {
                T instance = (T) createInstance();
                T secondInstance = (T) createInstance();
                Field declaredField = typeClass.getDeclaredField(f);
                declaredField.setAccessible(true);
                declaredField.set(instance, null);
                junitx.framework.Assert.assertNotEquals(instance, secondInstance);
                junitx.framework.Assert.assertNotEquals(secondInstance, instance);
                declaredField.set(secondInstance, null);
                junitx.framework.Assert.assertEquals(instance, secondInstance);
                junitx.framework.Assert.assertEquals(secondInstance, instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public final void testHashCodeAgainstNullableFields() throws Exception {
        nullableFields.forEach(f -> {
            try {
                T instance = (T) createInstance();
                T secondInstance = (T) createInstance();
                Field declaredField = typeClass.getDeclaredField(f);
                declaredField.setAccessible(true);
                declaredField.set(instance, null);
                junitx.framework.Assert.assertNotEquals(instance.hashCode(), secondInstance.hashCode());
                junitx.framework.Assert.assertNotEquals(secondInstance.hashCode(), instance.hashCode());
                declaredField.set(secondInstance, null);
                junitx.framework.Assert.assertEquals(instance, secondInstance);
                junitx.framework.Assert.assertEquals(secondInstance, instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
