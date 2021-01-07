package net.perfectdreams.yetanothersimplemavenrepo

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.file
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.request.header
import io.ktor.request.receiveStream
import io.ktor.response.respondText
import io.ktor.routing.put
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.perfectdreams.yetanothersimplemavenrepo.config.YASMRConfig
import java.io.File
import java.util.*

class YetAnotherSimpleMavenRepo(val yasmrConfig: YASMRConfig) {
    companion object {
        private val logger = KotlinLogging.logger {}
        val repo = File("C:\\Users\\Leonardo\\Documents\\LorittaAssets\\YetAnotherMavenRepo\\repo\\")
    }

    fun start() {
        logger.info { "Starting YetAnotherSimpleMavenRepo..." }

        val repositoryFolderFile = File(yasmrConfig.repositoryFolder)

        embeddedServer(Netty, yasmrConfig.port, yasmrConfig.address) {
            // Gradle does a HEAD request before trying to request
            install(AutoHeadResponse)

            routing {
                if (yasmrConfig.isAssetsEnabled)
                    static("assets") {
                        staticRootFolder = File(yasmrConfig.assetsFolder)
                        files(".")
                    }

                static {
                    staticRootFolder = repositoryFolderFile
                    files(".")
                }

                if (yasmrConfig.isIndexEnabled)
                    file("/", yasmrConfig.indexPath)

                put("/{params...}") {
                    val params = call.parameters.getAll("params") ?: return@put

                    logger.info { "Received PUT with params $params" }

                    val path = params.dropLast(1).joinToString("/")
                    val fileName = params.last() // Sanitize the name a bit
                        .replace("\\", "")
                        .replace("/", "")

                    logger.info { "Received file $fileName and it will be written on $path. Check for authentication? ${yasmrConfig.useAuthentication}" }

                    if (yasmrConfig.useAuthentication) {
                        logger.info { "Checking authentication for file $fileName in $path..." }

                        val authHeader = call.request.header("Authorization") ?: run {
                            logger.warn { "Someone tried to upload file $fileName in $path without a authorization header!" }
                            call.respondText("", status = HttpStatusCode.Unauthorized)
                            return@put
                        }

                        if (!authHeader.startsWith("Basic ")) {
                            call.respondText("Unsupported authorization type", status = HttpStatusCode.Unauthorized)
                            return@put
                        }

                        val split = Base64.getDecoder()
                            .decode(authHeader.removePrefix("Basic "))
                            .toString(Charsets.UTF_8)
                            .split(":")

                        val username = split[0]
                        val password = split.drop(1).joinToString(":")

                        val credential = yasmrConfig.credentials.firstOrNull { it.user == username && it.password == password }
                        if (credential == null) {
                            logger.warn { "Someone tried to upload file $fileName in $path with a invalid authorization header! User: $username; Password: $password" }
                            call.respondText("Missing Authentication header", status = HttpStatusCode.Unauthorized)
                            return@put
                        }

                        val wildcard = credential.allowedPaths.any { it == "*" }
                        if (!wildcard) {
                            val allowed = credential.allowedPaths.any { path.startsWith(it) }

                            if (!allowed) {
                                logger.warn { "$username is uploading file $fileName in a non-whitelisted path $path" }
                                call.respondText("You aren't allowed to upload files to $path", status = HttpStatusCode.Forbidden)
                                return@put
                            }
                        }

                        logger.info { "$username is uploading file $fileName in $path, hey $username! :3" }
                    }

                    val x = File(repositoryFolderFile, path)
                    x.mkdirs()

                    withContext(Dispatchers.IO) {
                        File(x, fileName).writeBytes(call.receiveStream().readAllBytes())
                    }

                    logger.info { "Successfully received and written $fileName on $path!" }

                    call.respondText("")
                }
            }
        }.start(wait = true)
    }
}