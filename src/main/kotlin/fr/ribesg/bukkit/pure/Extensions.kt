package fr.ribesg.bukkit.pure

import java.io.Closeable as X

/**
 * @author Ribesg
 */

/**
 * Allows to use the 'use' function with 2 Closeables.
 */
inline fun use<A : X, B : X, R>(a: A, b: B, f: (A, B) -> R): R = a.use { b.use { f(a, b) } }
