# Arena of Echoes

A JavaFX arena battle game with puzzle mechanics.

> **⚠️ Private Use Only**
> This project is shared for personal, private use only. It is not licensed
> for redistribution, resale, or public release. See the **License** section
> at the bottom.

---

## Table of Contents

- [Quick Start (Windows)](#quick-start-windows)
- [Run From Source (Developers)](#run-from-source-developers)
- [Build Your Own Installer](#build-your-own-installer)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [Credits & Third-Party Resources](#credits--third-party-resources)
- [License](#license)

---

## Quick Start (Windows)

The easiest way to play. No Java, no Maven, no terminal needed.

1. Go to the [Releases](../../releases) page.
2. Download the latest **`Arena of Echoes-1.0.msi`**.
3. Double-click the `.msi` file.
4. Follow the installer prompts.
5. Launch **Arena of Echoes** from the Start Menu or desktop shortcut.

The installer bundles a trimmed Java runtime, so **you do not need to
install Java separately**.

### System requirements

- Windows 10 or 11 (64-bit)
- ~100 MB free disk space

---

## Run From Source (Developers)

If you want to modify the game or build it yourself.

### Requirements

- **JDK 17 or newer** — JDK 21 or 25 recommended
  - Download: https://adoptium.net/temurin/releases/
- **Git** (to clone)
- No Maven install needed — the project includes a Maven Wrapper (`mvnw`).

### Steps

```bash
git clone https://github.com/Silentraindroplets/AzureMoon.git
cd <your-repo>/demo
```

**Windows:**

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot"
.\mvnw.cmd clean javafx:run
```

**macOS / Linux:**

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # macOS
./mvnw clean javafx:run
```

The first run downloads Maven and JavaFX (~40 MB). Subsequent runs
start in seconds.

---

## Build Your Own Installer

To produce a Windows `.msi` yourself:

### Prerequisites

- JDK 21 or 25
- WiX Toolset v3 — https://github.com/wixtoolset/wix3/releases
  - Extract to `C:\Program Files\WiX`
  - Add `C:\Program Files\WiX` to your system `PATH`
  - Close and reopen your terminal

### Build

```powershell
cd demo
.\mvnw.cmd clean package

# Copy JavaFX jars into target\lib
$jfx = "$env:USERPROFILE\.m2\repository\org\openjfx"
$lib = "target\lib"
New-Item -ItemType Directory -Force -Path $lib | Out-Null
Copy-Item "$jfx\javafx-base\21.0.2\javafx-base-21.0.2-win.jar" $lib
Copy-Item "$jfx\javafx-graphics\21.0.2\javafx-graphics-21.0.2-win.jar" $lib
Copy-Item "$jfx\javafx-controls\21.0.2\javafx-controls-21.0.2-win.jar" $lib
Copy-Item "$jfx\javafx-fxml\21.0.2\javafx-fxml-21.0.2-win.jar" $lib

# Build the MSI
jpackage --type msi `
  --name "Arena of Echoes" `
  --input target `
  --main-jar arena-of-echoes-1.0.jar `
  --main-class com.arena.Main `
  --module-path target\lib `
  --add-modules javafx.controls,javafx.fxml,javafx.graphics `
  --win-shortcut `
  --win-menu `
  --dest dist
```

Output: `demo/dist/Arena of Echoes-1.0.msi`

---

## Project Structure

```
demo/
├── pom.xml                    Maven build config
├── mvnw, mvnw.cmd             Maven Wrapper scripts
├── .mvn/wrapper/              Wrapper config
└── src/main/java/
    ├── module-info.java       Java module declaration
    └── com/arena/
        ├── Main.java          Entry point
        ├── Game.java          Game loop
        ├── BattleScreen.java
        ├── MenuScreen.java
        ├── IntroScene.java
        ├── BattleIntro.java
        ├── Settings.java
        ├── model/             Combatants, heroes, enemies, skills
        ├── puzzles/           Puzzle logic and bank
        └── ui/                UI components (bars, cards, modals)
```

---

## Troubleshooting

### "JAVA_HOME not found"

Set the environment variable in the current PowerShell session:

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot"
```

To make it permanent: Windows search → "environment variables" →
add `JAVA_HOME` under User variables, pointing to your JDK folder
(**no** `\bin`, **no** trailing backslash).

### "Module javafx.fxml not found"

Confirm your `module-info.java` contains:

```java
requires javafx.fxml;
```

### Maven dependency resolution fails with `${javafx.platform}`

Known bug in Maven 3.9.7. The project pins Maven to **3.9.6** via
`.mvn/wrapper/maven-wrapper.properties`. Do not upgrade it.

### Game window doesn't open, no error

The first run downloads Maven + JavaFX. Wait 3–5 minutes. Check
Task Manager for a `java.exe` process — if CPU is above 0%, it's
still working.

### Installer won't build

Make sure WiX Toolset v3 is installed and `C:\Program Files\WiX`
is in your `PATH`. Open a **fresh** terminal after editing PATH.

---

## Credits & Third-Party Resources

This project uses the following third-party libraries and tools.
All are used under their own licenses and remain the property of
their respective authors.

| Resource                  | Purpose                   | License                          | Source                            |
| ------------------------- | ------------------------- | -------------------------------- | --------------------------------- |
| **OpenJDK / Temurin JDK** | Java runtime              | GPL-2.0 with Classpath Exception | https://adoptium.net/             |
| **JavaFX**                | UI framework              | GPL-2.0 with Classpath Exception | https://openjfx.io/               |
| **Apache Maven**          | Build tool                | Apache-2.0                       | https://maven.apache.org/         |
| **Maven Wrapper**         | Bundled Maven launcher    | Apache-2.0                       | https://maven.apache.org/wrapper/ |
| **WiX Toolset**           | Windows installer builder | MS-RL                            | https://wixtoolset.org/           |

### Assets

> **Replace this section with whatever assets you actually used.**
>
> If you used free or public-domain assets (sprites, fonts, sounds),
> list them here with their source and license. Example:

- Sprites / images: _(source, license)_
- Fonts: _(source, license)_
- Sound effects / music: _(source, license)_

If you created any asset yourself, state that explicitly.

---

## License

**Private Use Only.**

This project and its source code are provided for private, personal
use only. You may:

- Run it on your own machines.
- Modify it for your own private use.
- Share copies with friends for non-commercial, private purposes.

You may **not**:

- Sell it, or include it in any commercial product.
- Distribute it publicly (e.g., on app stores, public websites,
  or public repositories) without permission.
- Claim authorship of the original code or assets.

Third-party libraries and tools used by this project remain under
their own licenses and are governed by those licenses, not this one.

No warranty of any kind is provided. Use at your own risk.

---

_Last updated: <October 12, 2026>_
