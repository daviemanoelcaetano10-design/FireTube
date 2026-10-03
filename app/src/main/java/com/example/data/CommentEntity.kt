package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val videoId: Long,
    val author: String,
    val authorAvatarColor: Long = 0xFFFF7043,
    val text: String,
    val likes: Int = 120,
    val timeAgo: String = "2 weeks ago",
    val timestamp: Long = System.currentTimeMillis()
)
