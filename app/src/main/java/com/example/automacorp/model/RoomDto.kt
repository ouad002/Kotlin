package com.example.automacorp.model


data class RoomDto(
    val id: Long,
    var name: String,
    val currentTemperature: Double?,
    var targetTemperature: Double?,
    val windows: List<WindowDto>
)
