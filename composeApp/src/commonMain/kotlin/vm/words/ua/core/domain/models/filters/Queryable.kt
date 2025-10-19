package vm.words.ua.core.domain.models.filters

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer

private fun appendQuery(obj: JsonObject, stringBuilder: MutableList<String>, prefix: String = "") {
    for ((key, value) in obj) {
        if (value is JsonNull) {
            continue
        }

        if (value is JsonPrimitive) {
            val primitiveValue = value.contentOrNull ?: continue
            stringBuilder.add("${prefix}${key}=${primitiveValue}")
            continue
        }

        if (value is JsonArray) {
            val params = value.joinToString(",") {
                (it as? JsonPrimitive)?.contentOrNull ?: ""
            }
            stringBuilder.add("${prefix}${key}=${params}")
            continue
        }

        if (value is JsonObject) {
            appendQuery(value, stringBuilder, "$prefix$key.")
        }
    }
}

private fun String.encodeURLParameter(): String {
    return this.encodeToByteArray()
        .joinToString("") { byte ->
            when (val char = byte.toInt().toChar()) {
                in 'a'..'z', in 'A'..'Z', in '0'..'9', '-', '_', '.', '~' -> char.toString()
                ' ' -> "+"
                else -> "%" + byte.toUByte().toString(16).padStart(2, '0').uppercase()
            }
        }
}

interface Queryable {

    @OptIn(InternalSerializationApi::class)
    fun toQueryMap(json: Json = Json { encodeDefaults = true }): Map<String, String> {
        val stringBuilder = mutableListOf<String>()

        // Получаем сериализатор для конкретного класса, а не интерфейса
        @Suppress("UNCHECKED_CAST")
        val serializer = this::class.serializer() as kotlinx.serialization.KSerializer<Any>
        val jsonElement = json.encodeToJsonElement(serializer, this as Any)

        if (jsonElement is JsonObject) {
            if (jsonElement.isEmpty()) {
                return emptyMap()
            }
            appendQuery(jsonElement, stringBuilder)
            // Возвращаем Map с декодированными значениями (как в Android версии)
            return stringBuilder.mapNotNull {
                val parts = it.split("=", limit = 2)
                if (parts.size == 2) parts[0] to parts[1] else null
            }.toMap()
        }

        return emptyMap()
    }

    /**
     * Конвертирует объект в query string формата ?key1=value1&key2=value2
     * Значения будут URL-закодированы
     */
    fun toQueryString(json: Json = Json { encodeDefaults = true }): String {
        val queryMap = toQueryMap(json)
        if (queryMap.isEmpty()) return ""
        return "?" + queryMap.entries.joinToString("&") {
            "${it.key.encodeURLParameter()}=${it.value.encodeURLParameter()}"
        }
    }
}
