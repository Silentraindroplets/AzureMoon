
```
New folder
└─ demo
   ├─ pom.xml
   ├─ src
   │  └─ main
   │     └─ java
   │        ├─ com
   │        │  └─ arena
   │        │     ├─ BattleIntro.java
   │        │     ├─ BattleScreen.java
   │        │     ├─ Game.java
   │        │     ├─ IntroScene.java
   │        │     ├─ Main.java
   │        │     ├─ MenuScreen.java
   │        │     ├─ model
   │        │     │  ├─ Combatant.java
   │        │     │  ├─ Enemy.java
   │        │     │  ├─ Hero.java
   │        │     │  └─ Skill.java
   │        │     ├─ puzzles
   │        │     │  ├─ Puzzle.java
   │        │     │  └─ PuzzleBank.java
   │        │     ├─ Settings.java
   │        │     └─ ui
   │        │        ├─ Battler.java
   │        │        ├─ CharacterCard.java
   │        │        ├─ PauseMenu.java
   │        │        ├─ PuzzleModal.java
   │        │        ├─ SettingsPanel.java
   │        │        ├─ Sprite.java
   │        │        ├─ Sprites.java
   │        │        └─ StatBar.java
   │        └─ module-info.java
   └─ target
      ├─ classes
      │  ├─ com
      │  │  └─ arena
      │  │     ├─ BattleIntro.class
      │  │     ├─ BattleScreen.class
      │  │     ├─ Game.class
      │  │     ├─ IntroScene$Line.class
      │  │     ├─ IntroScene.class
      │  │     ├─ Main.class
      │  │     ├─ MenuScreen.class
      │  │     ├─ model
      │  │     │  ├─ Combatant.class
      │  │     │  ├─ Enemy.class
      │  │     │  ├─ Hero.class
      │  │     │  ├─ Skill$Type.class
      │  │     │  └─ Skill.class
      │  │     ├─ puzzles
      │  │     │  ├─ Puzzle.class
      │  │     │  └─ PuzzleBank.class
      │  │     ├─ Settings$Difficulty.class
      │  │     ├─ Settings$PuzzleMode.class
      │  │     ├─ Settings.class
      │  │     └─ ui
      │  │        ├─ Battler.class
      │  │        ├─ CharacterCard.class
      │  │        ├─ PauseMenu.class
      │  │        ├─ PuzzleModal.class
      │  │        ├─ SettingsPanel.class
      │  │        ├─ Sprite.class
      │  │        ├─ Sprites.class
      │  │        └─ StatBar.class
      │  └─ module-info.class
      └─ test-classes

```
```
New folder
├─ demo
│  ├─ .mvn
│  │  └─ wrapper
│  │     └─ maven-wrapper.jar
│  ├─ maven-wrapper-3.3.4
│  │  ├─ DEPENDENCIES
│  │  ├─ Jenkinsfile
│  │  ├─ LICENSE
│  │  ├─ maven-wrapper
│  │  │  ├─ pom.xml
│  │  │  └─ src
│  │  │     ├─ main
│  │  │     │  └─ java
│  │  │     │     └─ org
│  │  │     │        └─ apache
│  │  │     │           └─ maven
│  │  │     │              └─ wrapper
│  │  │     │                 ├─ BootstrapMainStarter.java
│  │  │     │                 ├─ cli
│  │  │     │                 │  ├─ AbstractCommandLineConverter.java
│  │  │     │                 │  ├─ AbstractPropertiesCommandLineConverter.java
│  │  │     │                 │  ├─ CommandLineArgumentException.java
│  │  │     │                 │  ├─ CommandLineConverter.java
│  │  │     │                 │  ├─ CommandLineOption.java
│  │  │     │                 │  ├─ CommandLineParser.java
│  │  │     │                 │  ├─ ParsedCommandLine.java
│  │  │     │                 │  ├─ ParsedCommandLineOption.java
│  │  │     │                 │  ├─ ProjectPropertiesCommandLineConverter.java
│  │  │     │                 │  └─ SystemPropertiesCommandLineConverter.java
│  │  │     │                 ├─ DefaultDownloader.java
│  │  │     │                 ├─ Downloader.java
│  │  │     │                 ├─ HashAlgorithmVerifier.java
│  │  │     │                 ├─ Installer.java
│  │  │     │                 ├─ Logger.java
│  │  │     │                 ├─ MavenWrapperMain.java
│  │  │     │                 ├─ PathAssembler.java
│  │  │     │                 ├─ SystemPropertiesHandler.java
│  │  │     │                 ├─ Verifier.java
│  │  │     │                 ├─ WrapperConfiguration.java
│  │  │     │                 └─ WrapperExecutor.java
│  │  │     ├─ site
│  │  │     │  ├─ apt
│  │  │     │  │  └─ index.apt
│  │  │     │  └─ site.xml
│  │  │     └─ test
│  │  │        ├─ java
│  │  │        │  └─ org
│  │  │        │     └─ apache
│  │  │        │        └─ maven
│  │  │        │           └─ wrapper
│  │  │        │              ├─ DownloaderTest.java
│  │  │        │              ├─ HashAlgorithmVerifierTest.java
│  │  │        │              ├─ InstallerTest.java
│  │  │        │              ├─ PathAssemblerTest.java
│  │  │        │              ├─ SnapshotDistributionTest.java
│  │  │        │              ├─ SystemPropertiesHandlerTest.java
│  │  │        │              └─ WrapperExecutorTest.java
│  │  │        └─ resources
│  │  │           ├─ org
│  │  │           │  └─ apache
│  │  │           │     └─ maven
│  │  │           │        └─ wrapper
│  │  │           │           └─ wrapper.properties
│  │  │           └─ zip-slip.zip
│  │  ├─ maven-wrapper-distribution
│  │  │  ├─ pom.xml
│  │  │  └─ src
│  │  │     ├─ assembly
│  │  │     │  ├─ bin.xml
│  │  │     │  ├─ only-script.xml
│  │  │     │  ├─ script.xml
│  │  │     │  └─ source.xml
│  │  │     ├─ resources
│  │  │     │  ├─ mvn
│  │  │     │  │  └─ wrapper
│  │  │     │  │     └─ MavenWrapperDownloader.java
│  │  │     │  ├─ mvnw
│  │  │     │  ├─ mvnw.cmd
│  │  │     │  ├─ mvnwDebug
│  │  │     │  ├─ mvnwDebug.cmd
│  │  │     │  ├─ only-mvnw
│  │  │     │  ├─ only-mvnw.cmd
│  │  │     │  ├─ only-mvnwDebug
│  │  │     │  └─ only-mvnwDebug.cmd
│  │  │     └─ site
│  │  │        ├─ apt
│  │  │        │  └─ index.apt.vm
│  │  │        └─ site.xml
│  │  ├─ maven-wrapper-plugin
│  │  │  ├─ pom.xml
│  │  │  └─ src
│  │  │     ├─ it
│  │  │     │  ├─ projects
│  │  │     │  │  ├─ default
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ includeDebug
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ mavenversion
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ mavenversion-empty1
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ mavenversion-empty2
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ sha256_distribution
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ sha256_type_only-script
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ sha256_wrapper
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ sh_type_only-script
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ snapshot_distribution_test
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ setup.groovy
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ test_mvnd
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_bin
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_only-script
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_only-script-fail
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_script
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_source
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_unknown
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  └─ upgrade_with_existing_type
│  │  │     │  │     ├─ .mvn
│  │  │     │  │     │  └─ wrapper
│  │  │     │  │     │     └─ maven-wrapper.properties
│  │  │     │  │     ├─ invoker.properties
│  │  │     │  │     ├─ pom.xml
│  │  │     │  │     └─ verify.groovy
│  │  │     │  ├─ repository
│  │  │     │  │  ├─ apache-maven-4.9.999-20250710.120440-1-bin.zip
│  │  │     │  │  │  └─ apache-maven-4.9.999-SNAPSHOT
│  │  │     │  │  │     └─ bin
│  │  │     │  │  │        ├─ mvn
│  │  │     │  │  │        └─ mvn.cmd
│  │  │     │  │  └─ apache-maven-4.9.999-20250710.120440-1.pom
│  │  │     │  └─ settings.xml
│  │  │     ├─ main
│  │  │     │  └─ java
│  │  │     │     └─ org
│  │  │     │        └─ apache
│  │  │     │           └─ maven
│  │  │     │              └─ plugins
│  │  │     │                 └─ wrapper
│  │  │     │                    └─ WrapperMojo.java
│  │  │     ├─ site
│  │  │     │  ├─ markdown
│  │  │     │  │  ├─ index.md.vm
│  │  │     │  │  └─ usage.md
│  │  │     │  └─ site.xml
│  │  │     └─ test
│  │  │        └─ java
│  │  │           └─ org
│  │  │              └─ apache
│  │  │                 └─ maven
│  │  │                    └─ plugins
│  │  │                       └─ wrapper
│  │  │                          └─ WrapperMojoTest.java
│  │  ├─ NOTICE
│  │  ├─ pom.xml
│  │  ├─ README.md
│  │  └─ src
│  │     └─ site
│  │        ├─ markdown
│  │        │  ├─ CHANGELOG.old.md
│  │        │  └─ index.md
│  │        ├─ resources
│  │        │  └─ download.cgi
│  │        ├─ site.xml
│  │        └─ xdoc
│  │           └─ download.xml.vm
│  ├─ mvnw
│  ├─ mvnw.cmd
│  ├─ pom.xml
│  ├─ src
│  │  └─ main
│  │     └─ java
│  │        ├─ com
│  │        │  └─ arena
│  │        │     ├─ BattleIntro.java
│  │        │     ├─ BattleScreen.java
│  │        │     ├─ Game.java
│  │        │     ├─ IntroScene.java
│  │        │     ├─ Main.java
│  │        │     ├─ MenuScreen.java
│  │        │     ├─ model
│  │        │     │  ├─ Combatant.java
│  │        │     │  ├─ Enemy.java
│  │        │     │  ├─ Hero.java
│  │        │     │  └─ Skill.java
│  │        │     ├─ puzzles
│  │        │     │  ├─ Puzzle.java
│  │        │     │  └─ PuzzleBank.java
│  │        │     ├─ Settings.java
│  │        │     └─ ui
│  │        │        ├─ Battler.java
│  │        │        ├─ CharacterCard.java
│  │        │        ├─ PauseMenu.java
│  │        │        ├─ PuzzleModal.java
│  │        │        ├─ SettingsPanel.java
│  │        │        ├─ Sprite.java
│  │        │        ├─ Sprites.java
│  │        │        └─ StatBar.java
│  │        └─ module-info.java
│  └─ target
│     ├─ classes
│     │  ├─ com
│     │  │  └─ arena
│     │  │     ├─ BattleIntro.class
│     │  │     ├─ BattleScreen.class
│     │  │     ├─ Game.class
│     │  │     ├─ IntroScene$Line.class
│     │  │     ├─ IntroScene.class
│     │  │     ├─ Main.class
│     │  │     ├─ MenuScreen.class
│     │  │     ├─ model
│     │  │     │  ├─ Combatant.class
│     │  │     │  ├─ Enemy.class
│     │  │     │  ├─ Hero.class
│     │  │     │  ├─ Skill$Type.class
│     │  │     │  └─ Skill.class
│     │  │     ├─ puzzles
│     │  │     │  ├─ Puzzle.class
│     │  │     │  └─ PuzzleBank.class
│     │  │     ├─ Settings$Difficulty.class
│     │  │     ├─ Settings$PuzzleMode.class
│     │  │     ├─ Settings.class
│     │  │     └─ ui
│     │  │        ├─ Battler.class
│     │  │        ├─ CharacterCard.class
│     │  │        ├─ PauseMenu.class
│     │  │        ├─ PuzzleModal.class
│     │  │        ├─ SettingsPanel.class
│     │  │        ├─ Sprite.class
│     │  │        ├─ Sprites.class
│     │  │        └─ StatBar.class
│     │  └─ module-info.class
│     └─ test-classes
├─ maven-wrapper-distribution-3.3.4-bin
│  ├─ .mvn
│  │  └─ wrapper
│  │     └─ maven-wrapper.jar
│  ├─ mvnw
│  ├─ mvnw.cmd
│  ├─ mvnwDebug
│  └─ mvnwDebug.cmd
├─ maven-wrapper-distribution-3.3.4-bin.zip
└─ README.md

```
```
New folder
├─ demo
│  ├─ .mvn
│  │  └─ wrapper
│  │     └─ maven-wrapper.jar
│  ├─ maven-wrapper-3.3.4
│  │  ├─ DEPENDENCIES
│  │  ├─ Jenkinsfile
│  │  ├─ LICENSE
│  │  ├─ maven-wrapper
│  │  │  ├─ pom.xml
│  │  │  └─ src
│  │  │     ├─ main
│  │  │     │  └─ java
│  │  │     │     └─ org
│  │  │     │        └─ apache
│  │  │     │           └─ maven
│  │  │     │              └─ wrapper
│  │  │     │                 ├─ BootstrapMainStarter.java
│  │  │     │                 ├─ cli
│  │  │     │                 │  ├─ AbstractCommandLineConverter.java
│  │  │     │                 │  ├─ AbstractPropertiesCommandLineConverter.java
│  │  │     │                 │  ├─ CommandLineArgumentException.java
│  │  │     │                 │  ├─ CommandLineConverter.java
│  │  │     │                 │  ├─ CommandLineOption.java
│  │  │     │                 │  ├─ CommandLineParser.java
│  │  │     │                 │  ├─ ParsedCommandLine.java
│  │  │     │                 │  ├─ ParsedCommandLineOption.java
│  │  │     │                 │  ├─ ProjectPropertiesCommandLineConverter.java
│  │  │     │                 │  └─ SystemPropertiesCommandLineConverter.java
│  │  │     │                 ├─ DefaultDownloader.java
│  │  │     │                 ├─ Downloader.java
│  │  │     │                 ├─ HashAlgorithmVerifier.java
│  │  │     │                 ├─ Installer.java
│  │  │     │                 ├─ Logger.java
│  │  │     │                 ├─ MavenWrapperMain.java
│  │  │     │                 ├─ PathAssembler.java
│  │  │     │                 ├─ SystemPropertiesHandler.java
│  │  │     │                 ├─ Verifier.java
│  │  │     │                 ├─ WrapperConfiguration.java
│  │  │     │                 └─ WrapperExecutor.java
│  │  │     ├─ site
│  │  │     │  ├─ apt
│  │  │     │  │  └─ index.apt
│  │  │     │  └─ site.xml
│  │  │     └─ test
│  │  │        ├─ java
│  │  │        │  └─ org
│  │  │        │     └─ apache
│  │  │        │        └─ maven
│  │  │        │           └─ wrapper
│  │  │        │              ├─ DownloaderTest.java
│  │  │        │              ├─ HashAlgorithmVerifierTest.java
│  │  │        │              ├─ InstallerTest.java
│  │  │        │              ├─ PathAssemblerTest.java
│  │  │        │              ├─ SnapshotDistributionTest.java
│  │  │        │              ├─ SystemPropertiesHandlerTest.java
│  │  │        │              └─ WrapperExecutorTest.java
│  │  │        └─ resources
│  │  │           ├─ org
│  │  │           │  └─ apache
│  │  │           │     └─ maven
│  │  │           │        └─ wrapper
│  │  │           │           └─ wrapper.properties
│  │  │           └─ zip-slip.zip
│  │  ├─ maven-wrapper-distribution
│  │  │  ├─ pom.xml
│  │  │  └─ src
│  │  │     ├─ assembly
│  │  │     │  ├─ bin.xml
│  │  │     │  ├─ only-script.xml
│  │  │     │  ├─ script.xml
│  │  │     │  └─ source.xml
│  │  │     ├─ resources
│  │  │     │  ├─ mvn
│  │  │     │  │  └─ wrapper
│  │  │     │  │     └─ MavenWrapperDownloader.java
│  │  │     │  ├─ mvnw
│  │  │     │  ├─ mvnw.cmd
│  │  │     │  ├─ mvnwDebug
│  │  │     │  ├─ mvnwDebug.cmd
│  │  │     │  ├─ only-mvnw
│  │  │     │  ├─ only-mvnw.cmd
│  │  │     │  ├─ only-mvnwDebug
│  │  │     │  └─ only-mvnwDebug.cmd
│  │  │     └─ site
│  │  │        ├─ apt
│  │  │        │  └─ index.apt.vm
│  │  │        └─ site.xml
│  │  ├─ maven-wrapper-plugin
│  │  │  ├─ pom.xml
│  │  │  └─ src
│  │  │     ├─ it
│  │  │     │  ├─ projects
│  │  │     │  │  ├─ default
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ includeDebug
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ mavenversion
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ mavenversion-empty1
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ mavenversion-empty2
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ sha256_distribution
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ sha256_type_only-script
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ sha256_wrapper
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ sh_type_only-script
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ snapshot_distribution_test
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ setup.groovy
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ test_mvnd
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_bin
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_only-script
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_only-script-fail
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_script
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_source
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  ├─ type_unknown
│  │  │     │  │  │  ├─ invoker.properties
│  │  │     │  │  │  ├─ pom.xml
│  │  │     │  │  │  ├─ test.properties
│  │  │     │  │  │  └─ verify.groovy
│  │  │     │  │  └─ upgrade_with_existing_type
│  │  │     │  │     ├─ .mvn
│  │  │     │  │     │  └─ wrapper
│  │  │     │  │     │     └─ maven-wrapper.properties
│  │  │     │  │     ├─ invoker.properties
│  │  │     │  │     ├─ pom.xml
│  │  │     │  │     └─ verify.groovy
│  │  │     │  ├─ repository
│  │  │     │  │  ├─ apache-maven-4.9.999-20250710.120440-1-bin.zip
│  │  │     │  │  │  └─ apache-maven-4.9.999-SNAPSHOT
│  │  │     │  │  │     └─ bin
│  │  │     │  │  │        ├─ mvn
│  │  │     │  │  │        └─ mvn.cmd
│  │  │     │  │  └─ apache-maven-4.9.999-20250710.120440-1.pom
│  │  │     │  └─ settings.xml
│  │  │     ├─ main
│  │  │     │  └─ java
│  │  │     │     └─ org
│  │  │     │        └─ apache
│  │  │     │           └─ maven
│  │  │     │              └─ plugins
│  │  │     │                 └─ wrapper
│  │  │     │                    └─ WrapperMojo.java
│  │  │     ├─ site
│  │  │     │  ├─ markdown
│  │  │     │  │  ├─ index.md.vm
│  │  │     │  │  └─ usage.md
│  │  │     │  └─ site.xml
│  │  │     └─ test
│  │  │        └─ java
│  │  │           └─ org
│  │  │              └─ apache
│  │  │                 └─ maven
│  │  │                    └─ plugins
│  │  │                       └─ wrapper
│  │  │                          └─ WrapperMojoTest.java
│  │  ├─ NOTICE
│  │  ├─ pom.xml
│  │  ├─ README.md
│  │  └─ src
│  │     └─ site
│  │        ├─ markdown
│  │        │  ├─ CHANGELOG.old.md
│  │        │  └─ index.md
│  │        ├─ resources
│  │        │  └─ download.cgi
│  │        ├─ site.xml
│  │        └─ xdoc
│  │           └─ download.xml.vm
│  ├─ mvnw
│  ├─ mvnw.cmd
│  ├─ pom.xml
│  ├─ src
│  │  └─ main
│  │     └─ java
│  │        ├─ com
│  │        │  └─ arena
│  │        │     ├─ BattleIntro.java
│  │        │     ├─ BattleScreen.java
│  │        │     ├─ Game.java
│  │        │     ├─ IntroScene.java
│  │        │     ├─ Main.java
│  │        │     ├─ MenuScreen.java
│  │        │     ├─ model
│  │        │     │  ├─ Combatant.java
│  │        │     │  ├─ Enemy.java
│  │        │     │  ├─ Hero.java
│  │        │     │  └─ Skill.java
│  │        │     ├─ puzzles
│  │        │     │  ├─ Puzzle.java
│  │        │     │  └─ PuzzleBank.java
│  │        │     ├─ Settings.java
│  │        │     └─ ui
│  │        │        ├─ Battler.java
│  │        │        ├─ CharacterCard.java
│  │        │        ├─ PauseMenu.java
│  │        │        ├─ PuzzleModal.java
│  │        │        ├─ SettingsPanel.java
│  │        │        ├─ Sprite.java
│  │        │        ├─ Sprites.java
│  │        │        └─ StatBar.java
│  │        └─ module-info.java
│  └─ target
│     ├─ classes
│     │  ├─ com
│     │  │  └─ arena
│     │  │     ├─ BattleIntro.class
│     │  │     ├─ BattleScreen.class
│     │  │     ├─ Game.class
│     │  │     ├─ IntroScene$Line.class
│     │  │     ├─ IntroScene.class
│     │  │     ├─ Main.class
│     │  │     ├─ MenuScreen.class
│     │  │     ├─ model
│     │  │     │  ├─ Combatant.class
│     │  │     │  ├─ Enemy.class
│     │  │     │  ├─ Hero.class
│     │  │     │  ├─ Skill$Type.class
│     │  │     │  └─ Skill.class
│     │  │     ├─ puzzles
│     │  │     │  ├─ Puzzle.class
│     │  │     │  └─ PuzzleBank.class
│     │  │     ├─ Settings$Difficulty.class
│     │  │     ├─ Settings$PuzzleMode.class
│     │  │     ├─ Settings.class
│     │  │     └─ ui
│     │  │        ├─ Battler.class
│     │  │        ├─ CharacterCard.class
│     │  │        ├─ PauseMenu.class
│     │  │        ├─ PuzzleModal.class
│     │  │        ├─ SettingsPanel.class
│     │  │        ├─ Sprite.class
│     │  │        ├─ Sprites.class
│     │  │        └─ StatBar.class
│     │  └─ module-info.class
│     └─ test-classes
└─ README.md

```