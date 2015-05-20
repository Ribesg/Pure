# Pure [![Build Status](http://ci.ribesg.fr/job/Pure/badge/icon)](http://ci.ribesg.fr/job/Pure/)

Pure is a World Generator Bukkit plugin.

It was created to provide a simple way to use Minecraft Vanilla world generation on non-Vanilla
based Minecraft Server softwares like [Glowstone].

In the end, this will allow multiple cool things:
* Allow the use of the Vanilla world generator in a non-Vanilla context
* Keep the previous version World Generator for your Minecraft map while updating to the next version
* Have multiple maps with different Minecraft versions World Generators on the same server

It is written in [Kotlin]. The parts interacting with obfuscated Minecraft classes are in Java for clarity.

## How

The plugin downloads the required Minecraft Server jar files from their official repository on Amazon S3.
It then remaps the jar file content so that it doesn't clash with other versions.
Finally, it loads the jar file and use its obfuscated content to provide a Bukkit World Generator.

## Status

Implemented & planned Minecraft server versions:
- [ ] Alpha 0.1.4 (MC Alpha 1.0.17)
- [ ] Alpha 0.2.2.1 (MC Alpha 1.2)
- [x] Alpha 0.2.8 (MC Alpha 1.2.6)
- [ ] Beta 1.0.2
- [ ] Beta 1.1.2
- [ ] Beta 1.2.1
- [ ] Beta 1.3.1
- [ ] Beta 1.4.1
- [ ] Beta 1.5.2
- [ ] Beta 1.6.6
- [ ] Beta 1.7.3
- [ ] Beta 1.8.1
- [ ] Release 1.0.1
- [ ] Release 1.1.0
- [ ] Release 1.2.5
- [ ] Release 1.3.2
- [ ] Release 1.4.7
- [ ] Release 1.5.2
- [x] Release 1.6.4 (Bug: [#3](https://github.com/Ribesg/Pure/issues/3))
- [x] Release 1.7.10
- [x] *Release 1.8.0*
- [ ] Release 1.8.4

If you want another version to be added to this list (and implemented), please [open an issue](https://github.com/Ribesg/Pure/issues/new).

## Repository

Libraries required for this to work are stored into [another git repository][Pure-lib] to prevent bloating this
repository's git history.

To recursively clone this repository and its submodule, use the following command:
````
git clone --recursive https://github.com/Ribesg/Pure.git
````
Using the ``mvn`` command afterward will directly compile a working ``Pure.jar`` under ``target``.

The history of this repository has been cleaned when it has been separated from its libraries.
The old history can be found in the [Pure-old] repository.

## Credits

Pure is based on original work by [coelho] mostly on [VanillaGenerator] and a little on [VanillaNMS].
I first started by implementing a clean way to handle Minecraft Server jar files, then basically copy the
implementation of Minecraft 1.7.10 found in [VanillaGenerator]. I understood it, commented it, enhanced it and
fixed it. I added generated chests support among other things.

[Glowstone]: //www.glowstone.net
[Kotlin]: //kotlinlang.org
[Pure-lib]: //github.com/Ribesg/Pure-lib
[Pure-old]: //github.com/Ribesg/Pure-old
[coelho]: //github.com/coelho
[VanillaGenerator]: //github.com/coelho/VanillaGenerator
[VanillaNMS]: //github.com/coelho/VanillaGenerator
