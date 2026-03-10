package com.example.iyengaryoga20.model

import com.google.gson.annotations.SerializedName

// Обертка для v8
data class MobiV8Response(
    @SerializedName("data") val data: List<MobiClassElement>?
)

data class MobiClassElement(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("start") val start: String?,
    @SerializedName("end") val end: String?,
    @SerializedName("teacher") val teacher: MobiTeacher?,
    @SerializedName("room") val room: MobiRoom?,
    @SerializedName("activity") val activity: MobiActivity?,
    @SerializedName("available_slots") val availableSlots: Int?
)

data class MobiTeacher( @SerializedName("name") val name: String? )
data class MobiRoom( @SerializedName("title") val title: String? )
data class MobiActivity(
    @SerializedName("title") val title: String?,
    @SerializedName("color") val color: String?
)