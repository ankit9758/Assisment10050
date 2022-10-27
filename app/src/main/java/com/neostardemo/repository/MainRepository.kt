package com.neostardemo.repository
import com.neostardemo.database.UserDao
import com.neostardemo.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository
@Inject
constructor(
    private val userDao: UserDao
) {
    fun fetchAllUsers() : Flow<List<User>>  {
        return userDao.getAllUsers()
    }


     suspend fun saveUserToDb(user: User) {
        userDao.saveUser(user)
    }
}