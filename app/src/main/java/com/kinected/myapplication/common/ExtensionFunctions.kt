package com.kinected.myapplication.common

import com.google.gson.Gson
import com.kinected.myapplication.data.ErrorResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject

fun ResponseBody.getErrorMessage(): String?{
    return try {
        val errorBody = this
        val gson = Gson()
        val errorResponse = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
        errorResponse.message
    } catch (e: Exception) {
        e.printStackTrace()
        "An unknown error occurred"
    }
}

fun JSONObject.convertJSONToRequestBody(): RequestBody {
    return RequestBody.create(
        "application/json; charset=utf-8".toMediaTypeOrNull(),
        this.toString()
    )
}