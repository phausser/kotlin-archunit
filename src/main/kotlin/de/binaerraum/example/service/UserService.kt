package de.binaerraum.example.service

import de.binaerraum.example.model.User
import de.binaerraum.example.repository.UserRepository

class UserService(private val userRepository: UserRepository) {
    fun findUserById(id: Long): User? {
        return userRepository.findById(id)
    }
}
