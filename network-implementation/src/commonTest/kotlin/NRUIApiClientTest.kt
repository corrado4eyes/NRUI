import com.cradapyx.network.impl.NRUIApiClientImpl
import com.cradapyx.nrui.common.PublishingType
import com.cradapyx.nrui.network.NRUIApiClient
import com.cradapyx.nrui.network.model.OssrhStagingApiError
import com.cradapyx.nrui.network.model.RepositoriesRemote
import com.cradapyx.nrui.network.model.RepositoryRemote
import com.cradapyx.nrui.network.model.RepositoryState
import com.cradapyx.nrui.network.model.UnauthorizedException
import com.cradapyx.nrui.service.AccessTokenServiceMock
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.engine.mock.respondOk
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NRUIApiClientTest {

    private lateinit var apiClient: NRUIApiClient
    private lateinit var accessTokenService: AccessTokenServiceMock

    @BeforeTest
    fun setUp() {
        accessTokenService = AccessTokenServiceMock()
        accessTokenService.accessTokenReturns = "accessToken"
    }

    @Test
    fun `Get all repositories responds 200`() = runTest {
        apiClient = NRUIApiClientImpl(
            engine = MockEngine { request ->
                respond(
                    content = ByteReadChannel("""
                        {
                            repositories: [
                                {
                                  "description": "Name or namespace",
                                  "key": "key",
                                  "portal_deployment_id": "id",
                                  "state": "open"
                                }
                            ]
                        }
                    """.trimIndent()),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            },
            accessTokenService = accessTokenService
        )

        assertEquals(
            expected = RepositoriesRemote(
                repositories = listOf(
                    RepositoryRemote(
                        key = "key",
                        description = "Name or namespace",
                        portalDeploymentId = "id",
                        state = RepositoryState.OPEN
                    )
                )
            ),
            actual = apiClient.getRepositories())
    }

    @Test
    fun `Get all repositories responds 401`() = runTest {
        testUnauthorizedIsThrown { apiClient.getRepositories() }
    }

    @Test
    fun `Get all repositories responds 400`() = runTest {
        testOssrhStagingApiErrorIsThrown { apiClient.getRepositories() }
    }

    @Test
    fun `Post uploadRepository responds 200`() = runTest {
        apiClient = NRUIApiClientImpl(
            engine = MockEngine { request ->
                respondOk(
                    content = "",
                )
            },
            accessTokenService = accessTokenService
        )

        assertEquals(
            expected = Unit,
            actual = apiClient.uploadRepository(
                repositoryKey = "repositoryKey",
                deploymentName = "deploymentName",
                publishingType = PublishingType.USER_MANAGED
            )
        )
    }

    @Test
    fun `Post uploadRepository responds 401`() = runTest {
        testUnauthorizedIsThrown {
            apiClient.uploadRepository(
                repositoryKey = "repositoryKey",
                deploymentName = "deploymentName",
                publishingType = PublishingType.USER_MANAGED
            )
        }
    }

    @Test
    fun `Post uploadRepository responds 400`() = runTest {
        testOssrhStagingApiErrorIsThrown {
            apiClient.uploadRepository(
                repositoryKey = "repositoryKey",
                deploymentName = "deploymentName",
                publishingType = PublishingType.USER_MANAGED
            )
        }
    }

    @Test
    fun `Delete deleteRepository responds 200`() = runTest {
        apiClient = NRUIApiClientImpl(
            engine = MockEngine { request ->
                respondOk(
                    content = "",
                )
            },
            accessTokenService = accessTokenService
        )

        assertEquals(
            expected = Unit,
            actual = apiClient.deleteRepository(repositoryKey = "repositoryKey")
        )
    }

    @Test
    fun `Delete deleteRepository responds 401`() = runTest {
        testUnauthorizedIsThrown {
            apiClient.deleteRepository(repositoryKey = "repositoryKey")
        }
    }

    @Test
    fun `Delete deleteRepository responds 400`() = runTest {
        testOssrhStagingApiErrorIsThrown {
            apiClient.deleteRepository(repositoryKey = "repositoryKey")
        }
    }

    private suspend inline fun testUnauthorizedIsThrown(request: suspend () -> Unit) {
        apiClient = NRUIApiClientImpl(
            engine = MockEngine { request ->
                respondError(
                    content = """{}""",
                    status = HttpStatusCode.Unauthorized,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            },
            accessTokenService = accessTokenService
        )

        assertFailsWith<UnauthorizedException> {
            request()
        }
    }

    private suspend inline fun testOssrhStagingApiErrorIsThrown(
        error: OssrhStagingApiError = OssrhStagingApiError("BadRequest"),
        request: suspend () -> Unit
    ) {
        apiClient = NRUIApiClientImpl(
            engine = MockEngine { request ->
                respondError(
                    content = """{error: "${error.error}"}""",
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            },
            accessTokenService = accessTokenService
        )
        val exception = assertFailsWith<OssrhStagingApiError> { request() }

        assertEquals(error, exception)
    }
}