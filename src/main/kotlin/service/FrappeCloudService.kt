package io.github.klahap.fraploy.service

import io.github.klahap.fraploy.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.coroutines.executeAsync


class FrappeCloudService(
    client: OkHttpClient,
    credentials: FraployConfig.Credentials,
) {
    private val frappeCloudClient = client.newBuilder().addInterceptor {
        it.proceed(
            it.request().newBuilder()
                .header("Authorization", "token ${credentials.token}")
                .header("X-Press-Team", credentials.team)
                .build()
        )
    }.build()

    suspend fun getReleaseGroup(bench: ReleaseGroupName) = get(
        doctype = "Release Group",
        name = bench.name,
    )["deploy_information"]!!.jsonObject["apps"]!!.let {
        json.decodeFromJsonElement<List<FrappeAppWithReleases>>(it)
    }

    suspend fun getReleaseGroupName(title: String) = list(
        doctype = "Release Group",
        fields = null,
        filters = mapOf("title" to title),
    ).let { array ->
        if (array.size == 0) throw ReleaseGroupNotFoundException(title)
        if (array.size > 1) throw MultipleReleaseGroupsFoundException(title)
        array.single().jsonObject["name"]!!.jsonPrimitive.content.let { ReleaseGroupName(it) }
    }

    suspend fun getDeployCandidate(deployName: DeployName) = get(
        doctype = "Deploy Candidate",
        name = deployName.name,
    ).let {
        json.decodeFromJsonElement<DeployCandidate>(it)
    }

    suspend fun deployAndUpdate(
        releaseGroupName: ReleaseGroupName,
        apps: Set<AppUpdate>,
        sites: Set<SiteUpdate>,
    ) = request {
        url(getApiUrl("press.api.bench.deploy_and_update"))
        val body = JsonObject(
            mapOf(
                "name" to JsonPrimitive(releaseGroupName.name),
                "apps" to json.encodeToJsonElement(apps),
                "sites" to json.encodeToJsonElement(sites),
                "run_will_fail_check" to JsonPrimitive(true),
            )
        )
        post(body)
    }.sendOrThrow { DeployName(getBody<JsonObject>()["message"]!!.jsonPrimitive.content) }

    suspend fun getSites(releaseGroupName: ReleaseGroupName) = list(
        doctype = "Site",
        fields = setOf("name", "bench", "group", "server"),
        filters = mapOf("group" to releaseGroupName.name),
    ).let {
        json.decodeFromJsonElement<List<Site>>(it)
    }.filter { it.group == releaseGroupName }

    private suspend fun get(doctype: String, name: String) = request {
        url(getUrl)
        post(mapOf("doctype" to doctype, "name" to name))
    }.sendOrThrow {
        getBody<JsonObject>()["message"]!!.jsonObject
    }

    private suspend fun list(doctype: String, fields: Set<String>?, filters: Map<String, String>) = request {
        url(listUrl)
        post(
            JsonObject(
                buildMap {
                    put("doctype", JsonPrimitive(doctype))
                    if (fields != null)
                        put("fields", JsonArray(fields.map { JsonPrimitive(it) }))
                    put("filters", JsonObject(filters.mapValues { JsonPrimitive(it.value) }))
                    put("limit", JsonPrimitive(99999))
                }
            )
        )
    }.sendOrThrow {
        getBody<JsonObject>()["message"]!!.jsonArray
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun <T> Request.sendOrThrow(block: Response.() -> T) =
        frappeCloudClient.newCall(this).executeAsync().run {
            if (!isSuccessful) throw FrappeCloudApiException(
                url = this@sendOrThrow.url,
                code = code,
                message = message.takeIf { it.isNotBlank() } ?: runCatching { body.string() }.getOrNull() ?: "",
            )
            block()
        }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
        private val apiUrl = "https://frappecloud.com/api/method".toHttpUrl()
        private fun getApiUrl(method: String) = apiUrl.newBuilder().addPathSegment(method).build()
        private val getUrl = getApiUrl("press.api.client.get")
        private val listUrl = getApiUrl("press.api.client.get_list")

        private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

        @OptIn(ExperimentalSerializationApi::class)
        private inline fun <reified T> Response.getBody() =
            json.decodeFromStream<T>(body.byteStream())

        private fun request(block: Request.Builder.() -> Unit) =
            Request.Builder().apply(block).build()

        private fun Request.Builder.post(body: Map<String, String>): Request.Builder {
            return post(JsonObject(body.mapValues { JsonPrimitive(it.value) }))
        }

        private fun Request.Builder.post(body: JsonObject): Request.Builder {
            val requestBody = json.encodeToString(body).toRequestBody(contentType = jsonMediaType)
            return post(requestBody)
        }

    }
}
