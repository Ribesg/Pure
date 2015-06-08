package fr.ribesg.minecraft.pure.common

import kotlin.jvm.jvmOverloads as overloaded
import kotlin.platform.platformStatic as static

/**
 * A static Logger using either a Java Logger or slf4j Logger
 *
 * @author Ribesg
 */
public object Log {

    private enum class Level {
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL
    }

    private var javaLogger: java.util.logging.Logger? = null
    private var slf4jLogger: org.slf4j.Logger? = null

    public static fun initJavaLogger(logger: java.util.logging.Logger) {
        // True if both are null only
        assert(javaLogger == slf4jLogger, "Logger already initialized!")
        this.javaLogger = logger
    }

    public static fun initSlf4jLogger(logger: org.slf4j.Logger) {
        // True if both are null only
        assert(javaLogger == slf4jLogger, "Logger already initialized!")
        this.slf4jLogger = logger
    }

    public static fun dereferenceLogger() {
        this.javaLogger = null
        this.slf4jLogger = null
    }

    public static overloaded fun debug(msg: String, t: Throwable? = null): Unit
        = this.log(Level.DEBUG, msg, t)

    public static overloaded fun info(msg: String, t: Throwable? = null): Unit
        = this.log(Level.INFO, msg, t)

    public static overloaded fun warn(msg: String, t: Throwable? = null): Unit
        = this.log(Level.WARN, msg, t)

    public static overloaded fun error(msg: String, t: Throwable? = null): Unit
        = this.log(Level.ERROR, msg, t)

    private fun log(level: Level, msg: String, t: Throwable?) {
        if (this.javaLogger != null) {
            if (t == null) {
                this.javaLogger?.log(this.toJavaLoggerLevel(level), msg)
            } else {
                this.javaLogger?.log(this.toJavaLoggerLevel(level), msg, t)
            }
        } else if (this.slf4jLogger != null) {
            if (t == null) when (level) {
                Level.DEBUG -> this.slf4jLogger?.debug(msg)
                Level.INFO  -> this.slf4jLogger?.info(msg)
                Level.WARN  -> this.slf4jLogger?.warn(msg)
                Level.ERROR -> this.slf4jLogger?.error(msg)
                else        -> throw UnsupportedOperationException("Logging Level $level unsupported!")
            } else when (level) {
                Level.DEBUG -> this.slf4jLogger?.debug(msg, t)
                Level.INFO  -> this.slf4jLogger?.info(msg, t)
                Level.WARN  -> this.slf4jLogger?.warn(msg, t)
                Level.ERROR -> this.slf4jLogger?.error(msg, t)
                else        -> throw UnsupportedOperationException("Logging Level $level unsupported!")
            }
        } else {
            throw IllegalStateException("Tried to use Log while not initialized!")
        }
    }

    private fun toJavaLoggerLevel(level: Level) = when (level) {
        Level.DEBUG -> java.util.logging.Level.FINE
        Level.INFO  -> java.util.logging.Level.INFO
        Level.WARN  -> java.util.logging.Level.WARNING
        Level.ERROR -> java.util.logging.Level.SEVERE
        else        -> throw UnsupportedOperationException("Logging Level $level unsupported!")
    }

}
