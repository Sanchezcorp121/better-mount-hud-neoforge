# Better Mount HUD (NeoForge)

Unofficial NeoForge port of [Better Mount HUD](https://modrinth.com/mod/better-mount-hud) by Lortseam.
Please don't report issues to Lortseam, use this repo's issue tracker.

This client-side mod improves the in-game HUD while riding a horse, donkey or other mount:

- Both your food and the health of your mount are shown at the same time
- The experience bar only gets replaced with the jump bar while actually jumping

No dependencies, works on vanilla servers, fine to put in modpacks.

| Minecraft | NeoForge | Java |
|---|---|---|
| 1.21.1 | 21.1.252 or newer | 21 |
| 1.21.4 | 21.4.161 or newer | 21 |
| 1.21.8 | 21.8.57 or newer | 21 |
| 1.21.10 | 21.10.64 or newer | 21 |
| 1.21.11 | 21.11.48 or newer | 21 |
| 26.1.2 | 26.1.2.122 or newer | 25 |
| 26.2 | 26.2.0.93 or newer | 25 |
| 26.3 | 26.3.0.94 or newer | 25 |

## Install
Put the jar for your Minecraft version in the `mods` folder of a NeoForge instance. Servers do not need it.

## Build
Each Minecraft version is its own Gradle project under `versions/`.

```
cd versions/26.3
./gradlew build
```

The jar lands in `versions/<version>/build/libs/`.

## Licence
GPL-3.0-only, same as the original. See [LICENSE](LICENSE). Original work copyright Lortseam and contributors.
