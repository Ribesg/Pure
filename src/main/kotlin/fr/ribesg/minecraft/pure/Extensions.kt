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
public inline fun use<A : X, B : X, R>(a: A, b: B, f: (A, B) -> R): R = a.use { b.use { f(a, b) } }

/**
 * Wtf Exception
 */
public class ノಠ益ಠノuoᴉʇdǝɔxƎ(msg: String? = null, t: Throwable? = null) : Throwable(msg, t)
