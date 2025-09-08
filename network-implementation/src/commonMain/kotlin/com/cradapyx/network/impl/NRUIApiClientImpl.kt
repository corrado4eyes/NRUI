package com.cradapyx.network.impl

import com.cradapyx.nrui.common.PublishingType
import com.cradapyx.nrui.network.NRUIApiClient
import com.cradapyx.nrui.network.model.OssrhStagingApiError
import com.cradapyx.nrui.network.model.RepositoriesRemote
import com.cradapyx.nrui.network.model.RepositoryState
import com.cradapyx.nrui.network.model.RequestingIp
import com.cradapyx.nrui.network.model.UnauthorizedException
import com.cradapyx.nrui.service.AccessTokenService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.toByteArray
import kotlin.io.encoding.Base64

class NRUIApiClientImpl(
    engine: HttpClientEngine,
    accessTokenService: AccessTokenService
) : NRUIApiClient {

    private val client = HttpClient(engine) {
        install(ContentNegotiation) {
            json()
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = accessTokenService.accessToken(),
                        refreshToken = null
                    )
                }
            }
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
            }
            host = "ossrh-staging-api.central.sonatype.com"
            headers {
                append(HttpHeaders.ContentType, "application/json")
            }
        }
        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status

                when (statusCode) {
                    HttpStatusCode.Unauthorized -> throw UnauthorizedException()
                    HttpStatusCode.BadRequest -> {
                        val localException = response.body<OssrhStagingApiError>()
                        throw localException
                    }
                    else -> return@validateResponse
                }
            }
        }
    }

    override suspend fun uploadRepository(
        repositoryKey: String,
        deploymentName: String,
        publishingType: PublishingType
    ) = client.post {
        url {
            path("/manual/upload/repository/$repositoryKey")
            parameters.append("publishing_type", publishingType.name.lowercase())
        }
    }.body<Unit>()

    override suspend fun deleteRepository(repositoryKey: String) =
        client.delete("/manual/drop/repository/$repositoryKey").body<Unit>()

    override suspend fun getRepositories(
        profileId: String?,
        state: RepositoryState,
        ip: RequestingIp
    ): RepositoriesRemote = client.get("/manual/search/repositories").body()
}