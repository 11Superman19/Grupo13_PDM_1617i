package com.example.pedrofialho.myweatherapp.comms

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.IOException



class GetRequest<DTO>(url: String,
                      private val dtoType: Class<DTO>,
                      success: (DTO) -> Unit,
                      error: (VolleyError) -> Unit)

: JsonRequest<DTO>(Method.GET, url, "", success, error) {

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }


    override fun parseNetworkResponse(response: NetworkResponse): Response<DTO> {

        try {
            // TODO: Refine to handle error responses
            val dto = mapper.readValue(response.data, dtoType)
            return Response.success(dto, null)
        } catch (e: IOException) {
            e.printStackTrace()
            return Response.error(VolleyError())
        }
    }


}

