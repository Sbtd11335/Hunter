package com.cistus.hunter

import org.json.JSONObject
import java.io.File

actual class JsonFile(private val path: String) {
    val file = File(path)

    actual fun isExists() = file.exists()
    private fun read(jsonObject: JSONObject) : Map<String, Any>{
        val ret = mutableMapOf<String, Any>()
        for (key in jsonObject.keys()) {
            if (jsonObject.get(key) is JSONObject)
                ret[key] = read(jsonObject.get(key) as JSONObject)
            else
                ret[key] = jsonObject.get(key)
        }
        return ret
    }
    actual fun read(): Map<String, *>? {
        return if (isExists()) {
            val jsonObject = JSONObject(file.readText())
            return read(jsonObject)
        }
        else null
    }
    private fun mapToJsonObject(map: Map<String, *>): JSONObject {
        val jsonObject = JSONObject(map)
        val contents = mutableMapOf<String, Any>()
        for (key in jsonObject.keys()) {
            if (jsonObject.get(key) is Map<*, *>)
                contents[key] = mapToJsonObject(jsonObject.get(key) as Map<String, *>)
            else
                contents[key] = jsonObject.get(key)
        }
        return jsonObject
    }
    actual fun write(contents: Map<String, *>): Boolean {
        file.createNewFile()
        file.writeText(mapToJsonObject(contents).toString())
        return true
    }
    actual fun append(contents: Map<String, *>): Boolean {
        val jsonObject: JSONObject = if (!isExists())
            JSONObject("{}")
        else
            JSONObject(file.readText())
        for (content in contents) {
            if (content.value is Map<*, *>)
                jsonObject.put(content.key, mapToJsonObject(content.value as Map<String, *>))
            else
                jsonObject.put(content.key, content.value)
        }
        file.createNewFile()
        file.writeText(jsonObject.toString())
        return true
    }
    actual fun delete(keys: List<Any?>?): Boolean {
        if (isExists())
            return false
        val jsonObject = JSONObject(file.readText())
        if (keys != null) {
            for (key in keys)
                jsonObject.remove(key.toString())
            return false
        }
        else
            return file.delete()
    }
    actual fun getPath(): String? = path
}
