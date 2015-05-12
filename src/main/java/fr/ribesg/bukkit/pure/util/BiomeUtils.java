package fr.ribesg.bukkit.pure.util;

/**
 * @author coelho
 * @author Ribesg
 */
public final class BiomeUtils {

    public static String translateBiomeName(String input) {
        if (input.endsWith(" F")) {
            input = input.substring(0, input.length() - 1) + "Forest";
        }
        if (input.endsWith(" M")) {
            input = input.substring(0, input.length() - 1) + "Mountains";
        }
        if (input.contains("+")) {
            input = input.replace("+", " Plus");
        }
        if (input.contains(" ")) {
            return BiomeUtils.specialCase(input.toUpperCase().replace(' ', '_'));
        }
        final StringBuilder builder = new StringBuilder();
        final char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != 0 && Character.isUpperCase(chars[i])) {
                builder.append("_");
                builder.append(chars[i]);
            } else {
                builder.append(Character.toUpperCase(chars[i]));
            }
        }
        return BiomeUtils.specialCase(builder.toString());
    }

    private static String specialCase(final String input) {
        if (input.equals("MUSHROOM_ISLAND_SHORE")) {
            return "MUSHROOM_SHORE";
        } else if (input.equals("THE_END")) {
            return "SKY";
        }
        return input;
    }
}
