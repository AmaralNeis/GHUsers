package com.example.ghusers.repository.service

import com.example.ghusers.model.Repository
import com.example.ghusers.model.UserDetail
import com.example.ghusers.model.UserItem
import kotlinx.coroutines.flow.Flow

interface UserService {
    fun getUsers(): Flow<List<UserItem>>
    fun getUserWith(name: String): Flow<UserDetail?>
    fun getRepos(userName: String): Flow<List<Repository>?>
}