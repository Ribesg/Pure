package fr.ribesg.bukkit.pure

import java.io.Closeable

/**
 * @author Ribesg
 */

/**
 * Allows to use the 'use' function with 2 Closeables.
 */
inline fun use<A : Closeable, B : Closeable, R>(a: A, b: B, body: (A, B) -> R): R {
    return a.use { b.use { body(a, b) } }
}
