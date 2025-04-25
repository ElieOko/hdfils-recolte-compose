package com.partners.hdfils_recolte.data.remote

import android.util.Log
import com.partners.hdfils_recolte.data.utils.Constants.Companion.HOST_DEV
import com.partners.hdfils_recolte.data.utils.Constants.Companion.HOST_PROD
import com.partners.hdfils_recolte.data.utils.Constants.Companion.IS_PROD
import com.partners.hdfils_recolte.data.utils.Constants.Companion.TokenLocal
import com.partners.hdfils_recolte.domain.models.UserSerializable

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.StringValuesBuilderImpl
import kotlinx.serialization.json.Json
import java.util.logging.Logger



private const val NETWORK_TIME_OUT = 6_000L
val httpClientAndroid = HttpClient(Android) {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                useAlternativeNames = true
                ignoreUnknownKeys = true
                encodeDefaults = false
            }
        )
    }
    install(HttpTimeout) {
        requestTimeoutMillis = NETWORK_TIME_OUT
        connectTimeoutMillis = NETWORK_TIME_OUT
        socketTimeoutMillis = NETWORK_TIME_OUT
    }
    install(ResponseObserver) {
        onResponse { response ->
            Log.d("HTTP status:", "${response.status.value}")
        }
    }
    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
    defaultRequest {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
    }
}
class ClientKtor {
    suspend fun postData(route: String, data: Any): HttpResponse {
        return httpClientAndroid.post {
            url {
                protocol = if (IS_PROD) URLProtocol.HTTPS else URLProtocol.HTTP
                host = if (IS_PROD) HOST_PROD else HOST_DEV
                encodedPath = route
            }
            headers {
                append(HttpHeaders.Authorization, TokenLocal)
                append(HttpHeaders.ContentType, "application/json") // Set Content-Type
            }
            setBody(data) // Serialize the body
        }
    }
    suspend fun getData(route: String): HttpResponse {
        return httpClientAndroid.get {
            url {
                protocol = if (IS_PROD) URLProtocol.HTTPS else URLProtocol.HTTP
                host = if (IS_PROD) HOST_PROD else HOST_DEV
                encodedPath = route
            }
            headers {
                append(HttpHeaders.Authorization, TokenLocal)
            }
        }
    }
}