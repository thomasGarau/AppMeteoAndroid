package com.example.appmeteo.utils

import android.util.Log
import com.example.appmeteo.data.model.HourlyData
import com.example.appmeteo.data.model.MeteoResponse
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class MeteoResponseAdapter : TypeAdapter<MeteoResponse>() {

    override fun write(out: JsonWriter, value: MeteoResponse) {
        Log.d("MeteoResponseAdapter", "Starting write for MeteoResponse")

        try {
            out.beginObject()
            out.name("hourly")
            out.beginObject()

            // Writing hourly data with validation
            writeArray(out, "temperature_2m", value.hourly.temperature_2m)
            writeArray(out, "relative_humidity_2m", value.hourly.relative_humidity_2m)
            writeArray(out, "apparent_temperature", value.hourly.apparent_temperature)
            writeArray(out, "rain", value.hourly.rain)
            writeArray(out, "wind_speed_10m", value.hourly.wind_speed_10m)

            out.endObject()
            out.endObject()

            Log.d("MeteoResponseAdapter", "Finished writing MeteoResponse")
        } catch (e: Exception) {
            Log.e("MeteoResponseAdapter", "Error during write: ${e.message}", e)
            throw e
        }
    }

    private fun writeArray(out: JsonWriter, name: String, data: List<Double?>) {
        out.name(name).beginArray()
        data.forEachIndexed { index, it ->
            if (it == null) {
                Log.w(
                    "MeteoResponseAdapter",
                    "Null value in $name at index $index. Replacing with 0.0"
                )
                out.value(0.0)
            } else {
                out.value(it)
            }
        }
        out.endArray()
    }

    override fun read(input: JsonReader): MeteoResponse {
        Log.d("MeteoResponseAdapterStart", "Starting deserialization for MeteoResponse")
        var hourly = HourlyData(emptyList(), emptyList(), emptyList(), emptyList(), emptyList())

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "hourly" -> hourly = readHourlyData(input)
                else -> input.skipValue()
            }
        }
        input.endObject()

        return MeteoResponse(hourly)
    }

    private fun readHourlyData(input: JsonReader): HourlyData {
        var temperature = emptyList<Double>()
        var humidity = emptyList<Double>()
        var apparentTemp = emptyList<Double>()
        var rain = emptyList<Double>()
        var windSpeed = emptyList<Double>()

        try {
            input.beginObject()
            while (input.hasNext()) {
                when (input.nextName()) {
                    "temperature_2m" -> temperature = readDoubleArray(input)
                    "relative_humidity_2m" -> humidity = readDoubleArray(input)
                    "apparent_temperature" -> apparentTemp = readDoubleArray(input)
                    "rain" -> rain = readDoubleArray(input)
                    "wind_speed_10m" -> windSpeed = readDoubleArray(input)
                    else -> {
                        Log.w(
                            "MeteoResponseAdapter",
                            "Unexpected key in hourly data: ${input.peek()}"
                        )
                        input.skipValue()
                    }
                }
            }
            input.endObject()
        } catch (e: Exception) {
            Log.e("MeteoResponseAdapter", "Error reading hourly data: ${e.message}", e)
        }

        return HourlyData(temperature, humidity, apparentTemp, rain, windSpeed)
    }


    private fun readDoubleArray(input: JsonReader): List<Double> {
        val list = mutableListOf<Double>()
        try {
            input.beginArray()
            while (input.hasNext()) {
                when (input.peek()) {
                    JsonToken.NULL -> {
                        input.nextNull()
                        list.add(0.0) // Replace null with 0.0
                    }

                    JsonToken.NUMBER -> list.add(input.nextDouble())
                    JsonToken.STRING -> {
                        // Si une chaîne est trouvée, essayez de la convertir en double
                        try {
                            list.add(input.nextString().toDouble())
                        } catch (e: NumberFormatException) {
                            Log.e(
                                "MeteoResponseAdapter",
                                "Invalid number format in array: ${e.message}"
                            )
                            list.add(0.0) // Valeur par défaut
                        }
                    }

                    else -> {
                        Log.w("MeteoResponseAdapter", "Unexpected token in array: ${input.peek()}")
                        input.skipValue()
                    }
                }
            }
            input.endArray()
        } catch (e: Exception) {
            Log.e("MeteoResponseAdapter", "Error reading array: ${e.message}", e)
        }
        return list
    }
}

