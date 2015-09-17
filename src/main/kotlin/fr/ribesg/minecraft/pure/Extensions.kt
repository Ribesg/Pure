package fr.ribesg.minecraft.pure

import java.io.Closeable as X

/**
 * Defines extensions and other really short things which doesn't justify a
 * new file.
 *
 * @author Ribesg
 */

/**
 * Allows to use the 'use' function with 2 Closeables.
 */
inline fun use<A : X, B : X, R>(a: A, b: B, f: (A, B) -> R): R = a.use { b.use { f(a, b) } }
