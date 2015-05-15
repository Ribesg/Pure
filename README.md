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

In its current state, Pure only supports Minecraft 1.7.10 and 1.8.0. More versions will be added in the
future, including Minecraft Alpha and Beta versions.

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
