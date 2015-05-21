package fr.ribesg.bukkit.pure

import org.bukkit.World.Environment
import org.bukkit.generator.ChunkGenerator

import java.net.MalformedURLException
import java.net.URL

/**
 * Lists Minecraft versions, with hashes of and links to their jar files.
 *
 * @author Ribesg
 */
enum class MCVersion {

    /**
     * Minecraft Alpha 1.2.6 (2010-12-03)
     *
     * Server version 0.2.8
     */
    A0_2_8 : MCVersion(
        "29BB180262250235131FB53F12D61F438D008620E1B428E372DBADEC731F5E09",
        "EBE3CF77CA4E7E3BE45A8B319AB9FE27CD51EEF734DD5CC16C763EE7A91526E8",
        "http://files.ribesg.fr/minecraft_server.jar/minecraft_server.a0.2.8.jar",
        javaClass<fr.ribesg.bukkit.pure.minecraft.a0_2_8.ProxyChunkGenerator>()
    )

    /**
     * Minecraft Beta 1.7.3 (2011-07-08)
     */
    B1_7_3 : MCVersion(
        "033A127E4A25A60B038F15369C89305A3D53752242A1CFF11AE964954E79BA4D",
        "AFF8849A9B625552045544925E578E70E0567BC5ED88834BE95DD8D9AC1F22B2",
        "http://files.ribesg.fr/minecraft_server.jar/minecraft_server.b1.7.3.jar",
        null
    )

    /**
     * Minecraft 1.2.5 (2012-03-29) TODO
     */
    R1_2_5 : MCVersion(
        "19285D7D16AEE740F5A0584F0D80A4940F273A97F5A3EAF251FC1C6C3F2982D1",
        "11BDC22C2CF6785774B09A29BE5743BC9597DE9343BE20262B49C0AC00979D72",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.2.5/minecraft_server.1.2.5.jar",
        null
    )

    /**
     * Minecraft 1.3.2 (2012-08-15) TODO
     */
    R1_3_2 : MCVersion(
        "0795E098D970B459832750D4C6C2C4F58EF55A333A67281418318275E6026EBA",
        "0C68314039E8A55B0ACFBDC10B69A1E5B116229CC255B78BD1EF04F0FE4CA6DE",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.3.2/minecraft_server.1.3.2.jar",
        null
    )

    /**
     * Minecraft 1.4.7 (2012-12-27) TODO
     */
    R1_4_7 : MCVersion(
        "96B7512AEAD2FB20DDF780D7DD74208D77F209E16058EA8944150179E65B4DD3",
        "0A9F5D2CAF06C85A383772BE2C01E6289690555483AC3BAB1DD2F78F75D124C6",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.4.7/minecraft_server.1.4.7.jar",
        null
    )

    /**
     * Minecraft 1.5.2 (2013-04-25) TODO
     */
    R1_5_2 : MCVersion(
        "4F0C7B79CA2B10716703436550F75FA14E784B999707FFAD0EA4E9C38CC256A0",
        "9AA1E33A276EC2EAC6E377679EDF1F9728E10CDBCD01EE91FFECCA773E13C13D",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.5.2/minecraft_server.1.5.2.jar",
        null
    )

    /**
     * Minecraft 1.6.4 (2013-09-19)
     */
    R1_6_4 : MCVersion(
        "81841A2FEDFE0CE19983156A06FA5294335284BEEB95C8CA872D3C1A5FCF5774",
        "B5F598584EE44B592D35856332495850B6083CE11F4F6928B4F078E14F23FB53",
        "https://s3.amazonaws.com/Minecraft.Download/versions/1.6.4/minecraft_server.1.6.4.jar",
        javaClass<fr.ribesg.bukkit.pure.minecraft.r1_6_4.ProxyChunkGenerator>()
    )

    /**
     * Minecraft 1.7.10 (2014-06-26)
     */
    R1_7_10 : MCVersion(
        "C70870F00C4024D829E154F7E5F4E885B02DD87991726A3308D81F513972F3FC",
        "AC886902C6357289ED76D651F03380ABC1835EFFB6953058202191A1E2BAC9DC",
        "http://s3.amazonaws.com/Minecraft.Download/versions/1.7.10/minecraft_server.1.7.10.jar",
        javaClass<fr.ribesg.bukkit.pure.minecraft.r1_7_10.ProxyChunkGenerator>()
    )

    /**
     * Minecraft 1.8 (2014-09-02)
     */
    R1_8 : MCVersion(
        "40E23F3823D6F0E3CBADC491CEDB55B8BA53F8AB516B68182DDD1536BABEB291",
        "950C597411A970CC3FCC59E3B04EDE6FCA78BB07D542BD56F077C85E9D45B0B8",
        "http://s3.amazonaws.com/Minecraft.Download/versions/1.8/minecraft_server.1.8.jar",
        javaClass<fr.ribesg.bukkit.pure.minecraft.r1_8.ProxyChunkGenerator>()
    )

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Vanilla jar hash (SHA-256)
     */
    private val vanillaHash: String

    /**
     * Remapped jar hash (SHA-256)
     */
    private val remappedHash: String

    /**
     * Jar location
     */
    private val url: URL

    /**
     * Proxy ChunkGenerator class
     */
    private val chunkGeneratorClass: Class<out ChunkGenerator>?

    /**
     * Builds a MCVersion enum value.
     *
     * @param vanillaHash         the vanilla jar hash (SHA-256)
     * @param remappedHash        the remapped jar hash (SHA-256)
     * @param url                 the jar location
     * @param chunkGeneratorClass the class of the associated {@link ChunkGenerator}
     */
    private constructor(
        vanillaHash: String,
        remappedHash: String,
        url: String,
        chunkGeneratorClass: Class<out ChunkGenerator>?
    ) {
        this.vanillaHash = vanillaHash
        this.remappedHash = remappedHash
        this.url = try {
            URL(url)
        } catch (e: MalformedURLException) {
            throw IllegalArgumentException(e)
        }
        this.chunkGeneratorClass = chunkGeneratorClass
    }

    /**
     * Gets the vanilla jar hash (SHA-256).
     *
     * @return the vanilla jar hash (SHA-256)
     */
    public fun getVanillaHash(): String = this.vanillaHash

    /**
     * Gets the remapped jar hash (SHA-256).
     *
     * @return the remapped jar hash (SHA-256)
     */
    public fun getRemappedHash(): String = this.remappedHash

    /**
     * Gets the URL.
     *
     * @return the URL
     */
    public fun getUrl(): URL = this.url

    /**
     * Gets a new instance of a Chunk Generator matching this version.
     *
     * @param environment the required environment
     *
     * @return the Chunk Generator instance
     */
    public fun getChunkGenerator(environment: Environment?): ChunkGenerator = try {
        try {
            this.chunkGeneratorClass?.getDeclaredConstructor(javaClass<Environment>())!!.newInstance(environment)
        } catch (ex: NoSuchMethodException) {
            try {
                if (environment != null) {
                    Pure.logger().warning("Ignored environment parameter for MC version " + this)
                }
                this.chunkGeneratorClass?.getDeclaredConstructor()!!.newInstance()
            } catch (ex: NoSuchMethodException) {
                throw RuntimeException("Associated proxy ChunkGenerator class has no valid constructor ("
                                       + this.chunkGeneratorClass?.getCanonicalName() + ')')
            }
        }
    } catch (e: ReflectiveOperationException) {
        throw RuntimeException("Failed to call associated proxy ChunkGenerator class constructor")
    } catch (e: NullPointerException) {
        throw IllegalStateException("Generator for Minecraft version " + this + " is not implemented yet.", e)
    }
}
