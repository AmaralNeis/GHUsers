package com.example.ghusers.repository.api.implementation

import com.example.ghusers.model.Repository
import com.example.ghusers.model.UserDetail
import com.example.ghusers.model.UserItem
import com.example.ghusers.repository.api.GHApi
import com.example.ghusers.repository.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryApi @Inject constructor(private val api: GHApi): UserService {
    override fun getUsers(): Flow<List<UserItem>> = flow {
        val items = api.getUsers().body() ?: listOf()
        emit(items)
    }

    override fun getUserWith(name: String): Flow<UserDetail?> = flow {
         emit(api.getUserWith(name).body())
    }

    override fun getRepos(userName: String): Flow<List<Repository>?> = flow {
        emit(api.getUserRepos(userName).body())
    }
}