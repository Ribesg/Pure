package fr.ribesg.bukkit.pure.util;

import org.objenesis.ObjenesisStd;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Ribesg
 */
public final class ReflectionUtils {

    /**
     * Creates a new instance of the provided class without calling any
     * constructor.
     *
     * @param clazz the class
     * @param <T>   the type
     *
     * @return an instance of the provided class
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final Class<T> clazz) {
        return new ObjenesisStd().newInstance(clazz);
    }

    /**
     * Sets the provided field to the provided value in the provided instance
     * of the provided class.
     *
     * @param clazz     the class
     * @param obj       the object
     * @param fieldName the name of the field
     * @param value     the new value of the field
     *
     * @throws ReflectiveOperationException if anything goes wrong
     */
    public static void set(final Class<?> clazz, final Object obj, final String fieldName, final Object value) throws ReflectiveOperationException {
        final Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        if (Modifier.isFinal(field.getModifiers())) {
            // Field is final, work around it
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        }

        field.set(obj, value);
    }

    /**
     * Gets the value of the provided field from the provided instance of the
     * provided class.
     *
     * @param clazz      the class of the object
     * @param obj        the object
     * @param fieldName  the name of the field
     * @param fieldClass the class of the field
     * @param <T>        the type of the field
     *
     * @return the field's value
     *
     * @throws ReflectiveOperationException if anything goes wrong
     */
    public static <T> T get(final Class<?> clazz, final Object obj, final String fieldName, final Class<T> fieldClass) throws ReflectiveOperationException {
        final Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return fieldClass.cast(field.get(obj));
    }
}
