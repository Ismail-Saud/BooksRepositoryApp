package com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

@OptIn(ExperimentalSerializationApi::class)
object DescriptionSerializer : KSerializer<String?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DescriptionSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String?) {
        if (value != null) {
            encoder.encodeString(value)
        } else {
            encoder.encodeNull()
        }
    }

    override fun deserialize(decoder: Decoder): String? {
        val jsonDecoder = decoder as? JsonDecoder ?: return try {
            decoder.decodeString()
        } catch (_: Exception) {
            null
        }

        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> element.contentOrNull
            is JsonObject -> {
                val valueElement = element["value"] as? JsonPrimitive
                valueElement?.contentOrNull
            }
            else -> null
        }
    }
}
