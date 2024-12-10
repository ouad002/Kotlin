package com.example.automacorp.model

import androidx.core.text.isDigitsOnly
import com.example.automacorp.model.RoomDto
import com.example.automacorp.model.WindowDto
import com.example.automacorp.model.WindowStatus

object RoomService {
    // Sample data for room kinds, window kinds, and room numbers
    val ROOM_KIND: List<String> = listOf("Room", "Meeting", "Laboratory", "Office", "Boardroom")
    val ROOM_NUMBER: List<Char> = ('A'..'Z').toList()
    val WINDOW_KIND: List<String> = listOf("Sliding", "Bay", "Casement", "Hung", "Fixed")

    // Generate a fake Window
    fun generateWindow(id: Long, roomId: Long, roomName: String): WindowDto {
        return WindowDto(
            id = id,
            name = "${WINDOW_KIND.random()} Window $id",
            roomName = roomName,
            roomId = roomId,
            windowStatus = WindowStatus.values().random()
        )
    }

    // Generate a fake Room with multiple windows
    fun generateRoom(id: Long): RoomDto {
        val roomName = "${ROOM_NUMBER.random()}$id ${ROOM_KIND.random()}"
        val windows = (1..(1..6).random()).map { generateWindow(it.toLong(), id, roomName) }
        return RoomDto(
            id = id,
            name = roomName,
            currentTemperature = (15..30).random().toDouble(),
            targetTemperature = (15..22).random().toDouble(),
            windows = windows
        )
    }

    // Create a list of 50 rooms with fake data
    val ROOMS = (1..50).map { generateRoom(it.toLong()) }.toMutableList()

    // Find all rooms, sorted by name
    fun findAll(): List<RoomDto> {
        return ROOMS.sortedBy { it.name }
    }

    // Find a room by its id
    fun findById(id: Long): RoomDto? {
        return ROOMS.find { it.id == id }
    }

    // Find a room by its name
    fun findByName(name: String): RoomDto? {
        return ROOMS.find { it.name == name }
    }

    // Update a room's details
    fun updateRoom(id: Long, room: RoomDto): RoomDto? {
        val index = ROOMS.indexOfFirst { it.id == id }
        val updatedRoom = findById(id)?.copy(
            name = room.name,
            targetTemperature = room.targetTemperature,
            currentTemperature = room.currentTemperature
        ) ?: throw IllegalArgumentException("Room not found")
        return ROOMS.set(index, updatedRoom)
    }

    // Find a room by either name or id (string-based search)
    fun findByNameOrId(nameOrId: String?): RoomDto? {
        if (nameOrId != null) {
            return if (nameOrId.isDigitsOnly()) {
                findById(nameOrId.toLong())
            } else {
                findByName(nameOrId)
            }
        }
        return null
    }
}
