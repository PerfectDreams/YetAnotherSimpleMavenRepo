
<p align="center">
<h1 align="center">📦 YetAnotherSimpleMavenRepo 📦</h1>


<p align="center">
<a href="https://mrpowergamerbr.com/"><img src="https://img.shields.io/badge/website-mrpowergamerbr-fe4221.svg"></a>
<a href="https://github.com/PerfectDreams/YetAnotherSimpleMavenRepo/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-AGPL%20v3-lightgray.svg"></a>
</p>

YetAnotherSimpleMavenRepo (or YASMR) is a very simple (less than 200 lines!) Maven repository server.

YASMR was created because sometimes you just want a very easy and quick to use Maven repository, instead of going through hoops and complex setup just to get a small repository server up and running.

Do I recommend using YASMR in production? Yeah... probably not. If you need a Maven repository that a lot of people can use I recommend using a battle-tested repository server like Sonatype Nexus!

## 🌟 Features
* Easy to use, you just need Java installed!
* Supports basic authentication + whitelisted groups for users!
* Allows hosting a basic `index.html` for your repository main page!

## 👨‍💻 Compiling YASMR

### `0.` 👷 Prerequisites

* PowerShell (Windows) or Terminal (Linux).
> ⚠️ While Windows' command prompt may work, it is better to use PowerShell!
* You need to have the [Java Development Kit](https://adoptopenjdk.net/) installed on your machine. The minimum required version to compile and run YASMR is JDK 11.
* You need to have Git installed on your machine.
* Check if your machine has the `JAVA_HOME` property set correctly, newer JDK versions downloaded from AdoptOpenJDK may already have the variable set correctly. You can check if the variable is set by using `echo $env:JAVA_HOME` in PowerShell.
* If you want to help to develop YASMR, or if you only want a good Kotlin IDE, then download [JetBrains IntelliJ IDEA](https://www.jetbrains.com/pt-br/idea/)! The community edition is enough, so you don't need to be like "oh my god I need to *pay* for it". 😉

### `1.` 🧹 Preparing the environment
* Clone the repository with git:
```bash
git clone https://github.com/PerfectDreams/YetAnotherSimpleMavenRepo.git
```

### `2.` 💻 Compiling
* Go inside the source code folder and open PowerShell or the terminal inside of it.
* Build YASMR with Gradle:
```bash
./gradlew build
```
> 💡 If you have Gradle installed on your computer, you can use `gradle build` instead of `./gradlew build`

> ⚠️ If Gradle complains that the `readAllBytes()` method is missing, then you are using an outdated method (pre-JDK 9) version, please update your JDK! We are in `${currentYear}`, get off your dinosaur and get on the [latest JDK from AdoptOpenJDK train]((https://adoptopenjdk.net/)), choo choo! 🚄
* If the build is successful, then congratulations 🎉! You have successfully compiled YASMR!
* The final artifacts will be inside of the `build/libs/*.jar` folder, YASMR's dependencies will be inside of the `libs/` folder.

*You did it! Now... why not run YASMR?* 🙃

## 🚀 Hosting YASMR

### `0.` 👷 Prerequisites

* You will need the same prerequisites from the Compiling YASMR section, please check that section first.

### `1.` 🧹 Preparing the environment
* Create a empty folder somewhere in your OS, why an empty folder? Just to keep things tidy! :3

### `2.` 📥 Getting the required JARs

#### If you compiled it yourself...

**YASMR's JAR**: `/build/libs/` (get the Fat JAR version!)

**YASMR's Libraries:** `libs/`

#### If you are lazy and don't want to compile it yourself...
**You can find precompiled artifacts on the "Release" section!**

You will need to get `YASMR` and `YASMR (Libs)`

### `3.` 🧹 Preparing the environment²
* Copy the `yet-another-simple-maven-repository-*-fat.jar` to your created folder.
* Copy the `libs` folder to your created folder.
* If you did everything right, you should have in the root folder...
* * A file named `yet-another-simple-maven-repository-*.jar`, this is YASMR's executable.
* * A folder named `libs` containing all YASMR's dependencies.

### `4.` 🚶 The pre-start saga

* Run YASMR with `java -jar yet-another-simple-maven-server-*-fat.jar` (replace the JAR name with the JAR in your folder)
* Update the configurations with your own values.

### `5.` 🏃‍♂️ Starting YASMR
* Run YASMR again with `java -jar yet-another-simple-maven-server-*-fat.jar` (replace the JAR name with the JAR in your folder)
* If everything went well, your very own YASMR instance should be up and running! Congratulations! 🎉
* *(Optional)* Don't forget to port forward your 

### `6.` 📤 Kotlin DSL Gradle Publishing Example

```kotlin
plugins {  
  kotlin("jvm") version "1.3.72"
  `maven-publish`
}

...

publishing {
    repositories {
        maven {
            name = "YourRepoNameHere"
            url = uri("http://repo.name.here.com/")

            credentials {
                username = "loritta"
                password = "lori_is_so_cute"
            }
        }
    }
    publications {
        register("YourRepoNameHere", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}
 ```
