package fr.ribesg.bukkit.pure.util

import org.objenesis.ObjenesisStd

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.platform.platformStatic

/**
 * @author Ribesg
 */
object ReflectionUtils {

    /**
     * Creates a new instance of the provided class without calling any
     * constructor.
     *
     * @param clazz the class
     * @param <T>   the type
     *
     * @return an instance of the provided class
     */
    platformStatic
    SuppressWarnings("unchecked")
    fun <T> newInstance(clazz: Class<T>): T = ObjenesisStd().newInstance(clazz)

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
    platformStatic
    throws(javaClass<ReflectiveOperationException>())
    fun set(clazz: Class<*>, obj: Any?, fieldName: String, value: Any?) {
        val field = clazz.getDeclaredField(fieldName)
        field.setAccessible(true)

        if (Modifier.isFinal(field.getModifiers())) {
            // Field is final, work around it
            val modifiersField = javaClass<Field>().getDeclaredField("modifiers")
            modifiersField.setAccessible(true)
            modifiersField.setInt(field, field.getModifiers() and Modifier.FINAL.inv())
        }

        field.set(obj, value)
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
    platformStatic
    throws(javaClass<ReflectiveOperationException>())
    fun<T> get(clazz: Class<*>, obj: Any?, fieldName: String, fieldClass: Class<T>): T {
        val field = clazz.getDeclaredField(fieldName)
        field.setAccessible(true)
        return fieldClass.cast(field.get(obj))
    }
}
