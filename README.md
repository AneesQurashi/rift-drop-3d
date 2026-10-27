# RIFT DROP 3D

Original sci-fi industrial 3D arcade game: rotate a helix tower to let the bouncing ball fall through platform gaps, avoid orange hazards, chain combos, and smash platforms.

## Play in your browser

Open **index.html** in a desktop or mobile browser. The standalone HTML includes the game's styles, 3D graphics, controls, and scripts without external dependencies. An identical offline copy is provided at **RIFT_DROP_3D_PLAY.html**.

### Controls

- **A / D**, **Left / Right arrows**: rotate the tower
- **Mouse drag / touch drag**: rotate the tower
- **Gamepad left stick / D-pad**: rotate
- **Enter**: start the run
- **R**: restart immediately
- **Escape**: pause / resume

## Android APK

Android source lives in `android-app/`, using a full-screen landscape Android WebView that loads the *offline* `index.html` asset. The app does not request internet permission.

**Automatic APK:** Open the repository **Actions** tab, select **Build Android APK**, and after a successful run download the **RIFT-DROP-3D-debug-APK** artifact. Unzip it to find `app-debug.apk`. The automatically signed debug APK is for personal testing, **not a Play Store release**.

Build locally (requires JDK 17, Android SDK 35, Gradle 8.9):

```bash
gradle --project-dir android-app :app:assembleDebug --no-daemon
```

## Gameplay

18 platforms across three themed sections. The gap narrows, hazards increase, some hazard blocks rotate or pulse; multiple consecutive drops raise the combo multiplier and trigger smash.

## Original assets / license

The game design, graphics geometry, effects and code are original. This repo contains no third-party artwork or copyrighted music. See [ATTRIBUTION.md](./ATTRIBUTION.md).
