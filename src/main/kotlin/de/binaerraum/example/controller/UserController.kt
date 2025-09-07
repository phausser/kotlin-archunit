package de.binaerraum.example.controller

import de.binaerraum.example.model.User
import de.binaerraum.example.service.UserService

class UserController(private val userService: UserService) {
    fun getUser(id: Long): User? {
        return userService.findUserById(id)
    }
}
