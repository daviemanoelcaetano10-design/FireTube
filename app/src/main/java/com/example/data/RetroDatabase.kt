package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [VideoEntity::class, CommentEntity::class], version = 1, exportSchema = false)
abstract class RetroDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var INSTANCE: RetroDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): RetroDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RetroDatabase::class.java,
                    "retrotube_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.videoDao(), database.commentDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(videoDao: VideoDao, commentDao: CommentDao) {
            if (videoDao.getVideosCount() > 0) return

            val video1Id = videoDao.insertVideo(
                VideoEntity(
                    id = 1,
                    title = "Evolution of Dance",
                    description = "The funniest 6 minutes you will ever witness! Judson Laipply dances through the evolution of dance from Elvis Presley to MC Hammer, Michael Jackson, Village People, and more! Broadcast Yourself.",
                    author = "judsonlaipply",
                    subscribersCount = "1.2M",
                    viewsCount = "820M views",
                    timeAgo = "52y ago",
                    likesCount = 3600000L,
                    dislikesCount = 99000L,
                    commentsCount = 556000L,
                    thumbnailDrawableName = "thumb_evolution_dance_1791051945417",
                    durationSeconds = 360,
                    category = "Entertainment",
                    isUserCreated = false,
                    isSubscribed = false
                )
            )

            val video2Id = videoDao.insertVideo(
                VideoEntity(
                    id = 2,
                    title = "Charlie bit my finger - again !",
                    description = "Charlie bit my finger! And it really hurt! Legendary viral hit of two brothers in an armchair. HDClips original upload.",
                    author = "HDCYT",
                    subscribersCount = "890K",
                    viewsCount = "880M views",
                    timeAgo = "50y ago",
                    likesCount = 2400000L,
                    dislikesCount = 81000L,
                    commentsCount = 420000L,
                    thumbnailDrawableName = "thumb_retro_video_1791051971650",
                    durationSeconds = 56,
                    category = "Comedy",
                    isUserCreated = false,
                    isSubscribed = false
                )
            )

            val video3Id = videoDao.insertVideo(
                VideoEntity(
                    id = 3,
                    title = "Keyboard Cat! - THE ORIGINAL",
                    description = "Play him off, keyboard cat! Fatso the cat rocking the electronic synthesizer keyboard like a maestro. Vintage internet gold.",
                    author = "CharlieSchmidt",
                    subscribersCount = "450K",
                    viewsCount = "95M views",
                    timeAgo = "48y ago",
                    likesCount = 980000L,
                    dislikesCount = 12000L,
                    commentsCount = 78000L,
                    thumbnailDrawableName = "thumb_retro_video_1791051971650",
                    durationSeconds = 54,
                    category = "Pets & Animals",
                    isUserCreated = false,
                    isSubscribed = false
                )
            )

            val video4Id = videoDao.insertVideo(
                VideoEntity(
                    id = 4,
                    title = "Me at the zoo",
                    description = "The first video on YouTube! Recorded by Yakov Lapitsky at the San Diego Zoo in front of the elephants. Really cool.",
                    author = "jawed",
                    subscribersCount = "4.2M",
                    viewsCount = "290M views",
                    timeAgo = "53y ago",
                    likesCount = 14000000L,
                    dislikesCount = 320000L,
                    commentsCount = 11000000L,
                    thumbnailDrawableName = "thumb_evolution_dance_1791051945417",
                    durationSeconds = 19,
                    category = "Vlog",
                    isUserCreated = false,
                    isSubscribed = false
                )
            )

            // Seed initial comments matching screenshot
            commentDao.insertComments(
                listOf(
                    CommentEntity(
                        videoId = video1Id,
                        author = "pixel_watcher",
                        authorAvatarColor = 0xFFFFA726,
                        text = "who else still watches this daily in 2058?",
                        likes = 45210,
                        timeAgo = "1 month ago"
                    ),
                    CommentEntity(
                        videoId = video1Id,
                        author = "retro_vibe2006",
                        authorAvatarColor = 0xFF42A5F5,
                        text = "The golden era of the internet. No algorithms, just pure fun and creativity!",
                        likes = 12430,
                        timeAgo = "3 months ago"
                    ),
                    CommentEntity(
                        videoId = video1Id,
                        author = "dance_master_99",
                        authorAvatarColor = 0xFF66BB6A,
                        text = "The transition to MC Hammer was legendary back then haha",
                        likes = 8290,
                        timeAgo = "1 year ago"
                    ),
                    CommentEntity(
                        videoId = video2Id,
                        author = "classic_fan",
                        authorAvatarColor = 0xFFAB47BC,
                        text = "'Charlie that really hurt!' - Iconic quote etched in history forever.",
                        likes = 3120,
                        timeAgo = "2 weeks ago"
                    )
                )
            )
        }
    }
}
