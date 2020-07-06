package net.perfectdreams.yetanothersimplemavenrepo.config

class YASMRConfig(data: Map<String, Any?>) {
    val address: String by data
    val port: Int by data
    val repositoryFolder: String by data
    val isAssetsEnabled: Boolean by data
    val assetsFolder: String by data
    val isIndexEnabled: Boolean by data
    val indexPath: String by data
    val useAuthentication: Boolean by data
    val credentials: List<CredentialsConfig> = (data["credentials"] as List<Map<String, Any?>>)
        .map { CredentialsConfig(it) }

    class CredentialsConfig(data: Map<String, Any?>) {
        val user: String by data
        val password: String by data
        val allowedPaths: List<String> by data
    }
}