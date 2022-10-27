package com.neostardemo.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    var avatar: String,
    var firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val gender: String,
    val password: String,
    val address: String,
    val landmark: String,
    val city: String,
    val state: String,
    val pinCode: String,
    val qualification: String,
    val passingYear: String,
    val grade: String,
    val experience: String,
    val designation: String,
    val domain: String
)
