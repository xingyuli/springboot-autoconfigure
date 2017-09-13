package com.mobisist.springbootseed.autoconfigure

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlin.reflect.KClass

private val gson = Gson()

private val gsonPrettyPrinting = GsonBuilder().setPrettyPrinting().create()

internal fun <T : Any> KClass<T>.jsonFrom(json: String): T = gson.fromJson(json, this.java)

internal fun <T> T.jsonStringify(prettyPrint: Boolean = false): String
        = if (prettyPrint) gsonPrettyPrinting.toJson(this) else gson.toJson(this)
