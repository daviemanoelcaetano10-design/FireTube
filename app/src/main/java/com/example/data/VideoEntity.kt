package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val author: String,
    val authorAvatarUrl: String? = null,
    val authorAvatarColor: Long = 0xFF5C6BC0,
    val subscribersCount: String = "1.2M",
    val viewsCount: String = "820M views",
    val timeAgo: String = "52y ago",
    val likesCount: Long = 3600000L,
    val dislikesCount: Long = 99000L,
    val commentsCount: Long = 556000L,
    val thumbnailDrawableName: String? = null,
    val thumbnailUri: String? = null,
    val thumbnailBadgeText: String? = null,
    val thumbnailFilter: String = "NORMAL", // NORMAL, SEPIA, CRT, VINTAGE, SATURATED
    val durationSeconds: Int = 360,
    val category: String = "Entertainment",
    val isUserCreated: Boolean = false,
    val isSubscribed: Boolean = false,
    val userLikeStatus: Int = 0, // 0 = none, 1 = liked, -1 = disliked
    val createdAt: Long = System.currentTimeMillis()
)
