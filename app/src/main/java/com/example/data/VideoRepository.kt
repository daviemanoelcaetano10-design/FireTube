package com.example.data

import kotlinx.coroutines.flow.Flow

class VideoRepository(
    private val videoDao: VideoDao,
    private val commentDao: CommentDao
) {
    val allVideos: Flow<List<VideoEntity>> = videoDao.getAllVideos()
    val userUploadedVideos: Flow<List<VideoEntity>> = videoDao.getUserUploadedVideos()

    fun getVideosByAuthor(author: String): Flow<List<VideoEntity>> = videoDao.getVideosByAuthor(author)

    fun observeVideo(id: Long): Flow<VideoEntity?> = videoDao.observeVideoById(id)

    suspend fun getVideo(id: Long): VideoEntity? = videoDao.getVideoById(id)

    fun getComments(videoId: Long): Flow<List<CommentEntity>> = commentDao.getCommentsForVideo(videoId)

    suspend fun insertVideo(video: VideoEntity): Long = videoDao.insertVideo(video)

    suspend fun updateVideo(video: VideoEntity) = videoDao.updateVideo(video)

    suspend fun updateVideoDetails(
        id: Long,
        title: String,
        description: String,
        thumbnailUri: String?,
        badgeText: String?,
        filter: String
    ) {
        videoDao.updateVideoDetails(
            id = id,
            title = title,
            description = description,
            thumbnailUri = thumbnailUri,
            badgeText = badgeText,
            filter = filter
        )
    }

    suspend fun toggleLike(video: VideoEntity) {
        val currentStatus = video.userLikeStatus
        val newStatus: Int
        val newLikes: Long
        var newDislikes = video.dislikesCount

        if (currentStatus == 1) {
            // Already liked, toggle off
            newStatus = 0
            newLikes = (video.likesCount - 1).coerceAtLeast(0)
        } else {
            // Like
            newStatus = 1
            newLikes = video.likesCount + 1
            if (currentStatus == -1) {
                newDislikes = (video.dislikesCount - 1).coerceAtLeast(0)
                videoDao.updateDislikeStatus(video.id, newDislikes, newStatus)
            }
        }
        videoDao.updateLikeStatus(video.id, newLikes, newStatus)
    }

    suspend fun toggleDislike(video: VideoEntity) {
        val currentStatus = video.userLikeStatus
        val newStatus: Int
        val newDislikes: Long
        var newLikes = video.likesCount

        if (currentStatus == -1) {
            // Already disliked, toggle off
            newStatus = 0
            newDislikes = (video.dislikesCount - 1).coerceAtLeast(0)
        } else {
            // Dislike
            newStatus = -1
            newDislikes = video.dislikesCount + 1
            if (currentStatus == 1) {
                newLikes = (video.likesCount - 1).coerceAtLeast(0)
                videoDao.updateLikeStatus(video.id, newLikes, newStatus)
            }
        }
        videoDao.updateDislikeStatus(video.id, newDislikes, newStatus)
    }

    suspend fun toggleSubscription(author: String, currentStatus: Boolean) {
        videoDao.updateSubscription(author, !currentStatus)
    }

    suspend fun addComment(videoId: Long, author: String, text: String, colorHex: Long) {
        val comment = CommentEntity(
            videoId = videoId,
            author = author,
            authorAvatarColor = colorHex,
            text = text,
            likes = 0,
            timeAgo = "Just now",
            timestamp = System.currentTimeMillis()
        )
        commentDao.insertComment(comment)
    }
}
