package fr.ribesg.minecraft.pure.util

import org.objenesis.ObjenesisStd

import java.lang.reflect.Field
import java.lang.reflect.Modifier

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
    @Suppress("unchecked")
    @JvmStatic
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
    @Throws(ReflectiveOperationException::class)
    @JvmStatic
    fun set(clazz: Class<*>, obj: Any?, fieldName: String, value: Any?) {
        val field = clazz.getDeclaredField(fieldName)
        field.isAccessible = true

        if (Modifier.isFinal(field.modifiers)) {
            // Field is final, work around it
            val modifiersField = Field::class.java.getDeclaredField("modifiers")
            modifiersField.isAccessible = true
            modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
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
    @Throws(ReflectiveOperationException::class)
    @JvmStatic
    fun<T> get(clazz: Class<*>, obj: Any?, fieldName: String, fieldClass: Class<T>): T {
        val field = clazz.getDeclaredField(fieldName)
        field.isAccessible = true
        return fieldClass.cast(field.get(obj))
    }

}
