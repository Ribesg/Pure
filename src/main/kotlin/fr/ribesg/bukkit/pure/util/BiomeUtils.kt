package fr.ribesg.bukkit.pure.util

import kotlin.platform.platformStatic

/**
 * @author coelho
 * @author Ribesg
 */
object BiomeUtils {

    platformStatic
    public fun translateBiomeName(input: String): String {
        var fixedInput = input
        if (fixedInput.endsWith(" F")) {
            fixedInput = fixedInput.substring(0, input.length() - 1) + "Forest"
        }
        if (fixedInput.endsWith(" M")) {
            fixedInput = fixedInput.substring(0, input.length() - 1) + "Mountains"
        }
        if (fixedInput.contains("+")) {
            fixedInput = fixedInput.replace("+", " Plus")
        }
        if (fixedInput.contains(" ")) {
            return BiomeUtils.specialCase(fixedInput.toUpperCase().replace(' ', '_'))
        }
        val builder = StringBuilder()
        val chars = input.toCharArray()
        for (i in chars.indices) {
            if (i != 0 && Character.isUpperCase(chars[i])) {
                builder.append("_")
                builder.append(chars[i])
            } else {
                builder.append(Character.toUpperCase(chars[i]))
            }
        }
        return BiomeUtils.specialCase(builder.toString())
    }

    private fun specialCase(input: String): String = when (input) {
        "MUSHROOM_ISLAND_SHORE" -> "MUSHROOM_SHORE"
        "THE_END"               -> "SKY"
        else                    -> input
    }
}
