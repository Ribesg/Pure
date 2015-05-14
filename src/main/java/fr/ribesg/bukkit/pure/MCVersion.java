package fr.ribesg.bukkit.pure;

import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Lists Minecraft versions, with hashes of and links to their jar files.
 *
 * @author Ribesg
 */
public enum MCVersion {

    /**
     * Minecraft Alpha 1.2.6 (2010-12-03)
     *
     * Server version 0.2.8
     */
    A0_2_8(
        "29BB180262250235131FB53F12D61F438D008620E1B428E372DBADEC731F5E09",
        "0EA7FFC289665094C4752CF5D369EEB6063B434F32060FE054078166F0D0D3FC",
        "http://files.ribesg.fr/minecraft_server.jar/minecraft_server.a0.2.8.jar",
        fr.ribesg.bukkit.pure.minecraft.a0_2_8.ProxyChunkGenerator.class
    ),

    /**
     * Minecraft 1.2.5 (2012-03-29) TODO
     */
    R1_2_5(
        "19285D7D16AEE740F5A0584F0D80A4940F273A97F5A3EAF251FC1C6C3F2982D1",
        "6673133AB6763B7633B9CE9AA95A1A12E61469A12788794B2544DB79B22A439D",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.2.5/minecraft_server.1.2.5.jar",
        null
    ),

    /**
     * Minecraft 1.3.2 (2012-08-15) TODO
     */
    R1_3_2(
        "0795E098D970B459832750D4C6C2C4F58EF55A333A67281418318275E6026EBA",
        "18CDFDDADD69C1AE962BE49E98E2B57EDA5A6B497D919E5C5B1400B72BC966CF",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.3.2/minecraft_server.1.3.2.jar",
        null
    ),

    /**
     * Minecraft 1.4.7 (2012-12-27) TODO
     */
    R1_4_7(
        "96B7512AEAD2FB20DDF780D7DD74208D77F209E16058EA8944150179E65B4DD3",
        "1A7B2AFC5D3A2EEC67D00ACC4FE25B510CFF8B807AF6C76441FD2B9324871D68",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.4.7/minecraft_server.1.4.7.jar",
        null
    ),

    /**
     * Minecraft 1.5.2 (2013-04-25) TODO
     */
    R1_5_2(
        "4F0C7B79CA2B10716703436550F75FA14E784B999707FFAD0EA4E9C38CC256A0",
        "46E9389D3DE112F0AA843E076DECEFDB0179AADFC1B6DD48F2612D9458F52BEE",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.5.2/minecraft_server.1.5.2.jar",
        null
    ),

    /**
     * Minecraft 1.6.4 (2013-09-19) TODO
     */
    R1_6_4(
        "81841A2FEDFE0CE19983156A06FA5294335284BEEB95C8CA872D3C1A5FCF5774",
        "F551DAAAB723E6F2E5EFC940660121179657E061948E177507AFACD723F789CD",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.6.4/minecraft_server.1.6.4.jar",
        null
    ),

    /**
     * Minecraft 1.7.10 (2014-06-26)
     */
    R1_7_10(
        "C70870F00C4024D829E154F7E5F4E885B02DD87991726A3308D81F513972F3FC",
        "1BA7248AC5A9ACA1F8C6E3CA8FCBAC5190FA47A70832E3CE2EBBFE53B88BB0E9",
        "http://s3.amazonaws.com/Minecraft.Download/versions/1.7.10/minecraft_server.1.7.10.jar",
        fr.ribesg.bukkit.pure.minecraft.r1_7_10.ProxyChunkGenerator.class
    ),

    /**
     * Minecraft 1.8 (2014-09-02)
     */
    R1_8(
        "40E23F3823D6F0E3CBADC491CEDB55B8BA53F8AB516B68182DDD1536BABEB291",
        "1CA14C140603FC93D9202CA90D13F0BD216E4F1183084F890E8E23156560FD47",
        "http://s3.amazonaws.com/Minecraft.Download/versions/1.8/minecraft_server.1.8.jar",
        fr.ribesg.bukkit.pure.minecraft.r1_8.ProxyChunkGenerator.class
    ),

    /*/**
     * Minecraft 1.8.4 (2015-04-17)
     *//*
    R1_8_4(
        "394A9D0D5BCD03272A58F036B8736A47D26D63B45A4E7C820629114876E72107",
        "X",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.8.4/minecraft_server.1.8.4.jar",
        fr.ribesg.bukkit.pure.minecraft.r1_8_4.ProxyChunkGenerator.class
    ),*/

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */;

    /**
     * Vanilla jar hash (SHA-256)
     */
    private final String vanillaHash;

    /**
     * Remapped jar hash (SHA-256)
     */
    private final String remappedHash;

    /**
     * Jar location
     */
    private final URL url;

    /**
     * Proxy ChunkGenerator class
     */
    private final Class<? extends ChunkGenerator> chunkGeneratorClass;

    /**
     * Builds a MCVersion enum value.
     *
     * @param vanillaHash         the vanilla jar hash (SHA-256)
     * @param remappedHash        the remapped jar hash (SHA-256)
     * @param url                 the jar location
     * @param chunkGeneratorClass the class of the associated {@link ChunkGenerator}
     */
    MCVersion(final String vanillaHash, final String remappedHash, final String url, final Class<? extends ChunkGenerator> chunkGeneratorClass) {
        this.vanillaHash = vanillaHash;
        this.remappedHash = remappedHash;
        try {
            this.url = new URL(url);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
        this.chunkGeneratorClass = chunkGeneratorClass;
    }

    /**
     * Gets the vanilla jar hash (SHA-256).
     *
     * @return the vanilla jar hash (SHA-256)
     */
    public String getVanillaHash() {
        return this.vanillaHash;
    }

    /**
     * Gets the remapped jar hash (SHA-256).
     *
     * @return the remapped jar hash (SHA-256)
     */
    public String getRemappedHash() {
        return this.remappedHash;
    }

    /**
     * Gets the URL.
     *
     * @return the URL
     */
    public URL getUrl() {
        return this.url;
    }

    /**
     * Gets a new instance of a Chunk Generator matching this version.
     *
     * @param environment the required environment
     *
     * @return the Chunk Generator instance
     */
    public ChunkGenerator getChunkGenerator(final Environment environment) {
        try {
            try {
                return this.chunkGeneratorClass.getDeclaredConstructor(Environment.class).newInstance(environment);
            } catch (final NoSuchMethodException e1) {
                try {
                    if (environment != null) {
                        Pure.logger().warning("Ignored environment parameter for MC version " + this);
                    }
                    return this.chunkGeneratorClass.getDeclaredConstructor().newInstance();
                } catch (final NoSuchMethodException e2) {
                    throw new RuntimeException("Associated proxy ChunkGenerator class has no valid constructor ("
                                               + this.chunkGeneratorClass.getCanonicalName() + ')');
                }
            }
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException("Failed to call associated proxy ChunkGenerator class constructor");
        } catch (final NullPointerException e) {
            throw new IllegalStateException("Generator for Minecraft version " + this + " not implemented yet.", e);
        }
    }
}
