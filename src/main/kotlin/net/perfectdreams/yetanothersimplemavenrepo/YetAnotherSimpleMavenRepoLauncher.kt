package net.perfectdreams.yetanothersimplemavenrepo

import net.perfectdreams.yetanothersimplemavenrepo.config.YASMRConfig
import org.yaml.snakeyaml.Yaml
import java.io.File

object YetAnotherSimpleMavenRepoLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val configFile = File("config.yml")
        if (!configFile.exists()) {
            val inputStream = YetAnotherSimpleMavenRepoLauncher::class.java.getResourceAsStream("/config.yml")
            configFile.writeBytes(inputStream.readAllBytes())
            println("Welcome to YetAnotherSimpleMavenRepo!")
            println("You can call me YASMR, because YetAnother... is just too damn long!")
            println("")
            println("Before we can start, please configure the newly generated \"config.yml\" file and then launch me again!")
            println("")
            println("See you soon!")
            return
        }

        val yaml = Yaml()
        val config = yaml.load<Map<String, Any?>>(configFile.readText())

        val yasmr = YetAnotherSimpleMavenRepo(YASMRConfig(config))
        yasmr.start()
    }
}