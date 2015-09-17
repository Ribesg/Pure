package fr.ribesg.minecraft.pure.util

/**
 * @author coelho
 * @author Ribesg
 */
object BiomeUtils {

    @JvmStatic
    fun translateBiomeName(input: String): String {
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
            return BiomeUtils.fixedName(fixedInput.toUpperCase().replace(' ', '_'))
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
        return BiomeUtils.fixedName(builder.toString())
    }

    private fun fixedName(input: String): String = when (input) {
    // Biomes with different name in MC than in Bukkit
        "MUSHROOM_ISLAND_SHORE" -> "MUSHROOM_SHORE"
        "THE_END"               -> "SKY"

    // Biome no longer supported in Bukkit Final
        "EXTREME_HILLS_EDGE"    -> "EXTREME_HILLS" // MC 1.6.4

    // For other cases, just return what has been passed
        else                    -> input
    }

}
