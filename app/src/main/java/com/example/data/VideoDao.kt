package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY id ASC")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE isUserCreated = 1 ORDER BY createdAt DESC")
    fun getUserUploadedVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE author = :author ORDER BY createdAt DESC")
    fun getVideosByAuthor(author: String): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE id = :id")
    suspend fun getVideoById(id: Long): VideoEntity?

    @Query("SELECT * FROM videos WHERE id = :id")
    fun observeVideoById(id: Long): Flow<VideoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Update
    suspend fun updateVideo(video: VideoEntity)

    @Query("UPDATE videos SET likesCount = :likes, userLikeStatus = :status WHERE id = :id")
    suspend fun updateLikeStatus(id: Long, likes: Long, status: Int)

    @Query("UPDATE videos SET dislikesCount = :dislikes, userLikeStatus = :status WHERE id = :id")
    suspend fun updateDislikeStatus(id: Long, dislikes: Long, status: Int)

    @Query("UPDATE videos SET isSubscribed = :isSubscribed WHERE author = :author")
    suspend fun updateSubscription(author: String, isSubscribed: Boolean)

    @Query("UPDATE videos SET description = :description, thumbnailUri = :thumbnailUri, thumbnailBadgeText = :badgeText, thumbnailFilter = :filter, title = :title WHERE id = :id")
    suspend fun updateVideoDetails(
        id: Long,
        title: String,
        description: String,
        thumbnailUri: String?,
        badgeText: String?,
        filter: String
    )

    @Query("SELECT COUNT(*) FROM videos")
    suspend fun getVideosCount(): Int
}
