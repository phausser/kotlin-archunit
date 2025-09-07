package de.binaerraum.example.repository

import de.binaerraum.example.model.User

class UserRepository {
    fun findById(id: Long): User? {
        return User(id, "Test User")
    }
}
