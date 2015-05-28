package fr.ribesg.bukkit.pure.log

import kotlin.platform.platformStatic

/**
 * A static Logger using either a Java Logger and and slf4j Logger
 *
 * @author Ribesg
 */
object Log {

    private enum class Level {
        DEBUG
        INFO
        WARN
        ERROR
        FATAL
    }

    private var javaLogger: java.util.logging.Logger? = null
    private var slf4jLogger: org.slf4j.Logger? = null

    fun initJavaLogger(logger: java.util.logging.Logger) {
        // True if both are null only
        assert(javaLogger == slf4jLogger, "Logger already initialized!")
        this.javaLogger = logger
    }

    fun initSlf4jLogger(logger: org.slf4j.Logger) {
        // True if both are null only
        assert(javaLogger == slf4jLogger, "Logger already initialized!")
        this.slf4jLogger = logger
    }

    fun dereferenceLogger() {
        this.javaLogger = null
        this.slf4jLogger = null
    }

    platformStatic fun debug(msg: String): Unit
        = this.log(Level.DEBUG, msg)

    platformStatic fun debug(msg: String, t: Throwable): Unit
        = this.log(Level.DEBUG, msg, t)

    platformStatic fun info(msg: String): Unit
        = this.log(Level.INFO, msg)

    platformStatic fun info(msg: String, t: Throwable): Unit
        = this.log(Level.INFO, msg, t)

    platformStatic fun warn(msg: String): Unit
        = this.log(Level.WARN, msg)

    platformStatic fun warn(msg: String, t: Throwable): Unit
        = this.log(Level.WARN, msg, t)

    platformStatic fun error(msg: String): Unit
        = this.log(Level.ERROR, msg)

    platformStatic fun error(msg: String, t: Throwable): Unit
        = this.log(Level.ERROR, msg, t)

    private fun log(level: Level, msg: String) {
        if (this.javaLogger != null) {
            this.javaLogger?.log(this.toJavaLoggerLevel(level), msg)
        } else if (this.slf4jLogger != null) when (level) {
            Level.DEBUG -> this.slf4jLogger?.debug(msg)
            Level.INFO  -> this.slf4jLogger?.info(msg)
            Level.WARN  -> this.slf4jLogger?.warn(msg)
            Level.ERROR -> this.slf4jLogger?.error(msg)
            else        -> throw UnsupportedOperationException("Logging Level $level unsupported!")
        }
    }

    private fun log(level: Level, msg: String, t: Throwable) {
        if (this.javaLogger != null) {
            this.javaLogger?.log(this.toJavaLoggerLevel(level), msg, t)
        } else if (this.slf4jLogger != null) when (level) {
            Level.DEBUG -> this.slf4jLogger?.debug(msg, t)
            Level.INFO  -> this.slf4jLogger?.info(msg, t)
            Level.WARN  -> this.slf4jLogger?.warn(msg, t)
            Level.ERROR -> this.slf4jLogger?.error(msg, t)
            else        -> throw UnsupportedOperationException("Logging Level $level unsupported!")
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
