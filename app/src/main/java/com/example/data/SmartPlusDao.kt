package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SmartPlusDao {

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // Tournaments
    @Query("SELECT * FROM tournaments")
    fun getAllTournaments(): Flow<List<TournamentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournaments(tournaments: List<TournamentEntity>)

    @Query("UPDATE tournaments SET isRegistered = 1, playersCount = playersCount + 1 WHERE id = :tournamentId")
    suspend fun registerForTournament(tournamentId: String)

    // Posts
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("UPDATE posts SET isLiked = CASE WHEN isLiked = 1 THEN 0 ELSE 1 END, likesCount = CASE WHEN isLiked = 1 THEN likesCount - 1 ELSE likesCount + 1 END WHERE id = :postId")
    suspend fun toggleLikePost(postId: String)

    // Communities
    @Query("SELECT * FROM communities")
    fun getAllCommunities(): Flow<List<CommunityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunities(communities: List<CommunityEntity>)

    @Query("UPDATE communities SET isJoined = CASE WHEN isJoined = 1 THEN 0 ELSE 1 END WHERE id = :communityId")
    suspend fun toggleJoinCommunity(communityId: String)

    // Payments
    @Query("SELECT * FROM payments ORDER BY date DESC")
    fun getAllPayments(): Flow<List<PaymentTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentTransactionEntity)

    @Query("UPDATE user_profile SET walletBalanceFcfa = walletBalanceFcfa - :amount WHERE id = 1")
    suspend fun deductWalletBalance(amount: Int)

    @Query("UPDATE user_profile SET walletBalanceFcfa = walletBalanceFcfa + :amount WHERE id = 1")
    suspend fun addWalletBalance(amount: Int)

    // Chat Groups & Channels
    @Query("SELECT * FROM chat_groups")
    fun getAllChatGroups(): Flow<List<ChatGroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatGroups(groups: List<ChatGroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatGroup(group: ChatGroupEntity)

    @Query("UPDATE chat_groups SET lastMessage = :lastMessage, lastMessageTime = :time, unreadCount = 0 WHERE id = :groupId")
    suspend fun updateGroupLastMessage(groupId: String, lastMessage: String, time: String)

    // Chat Messages
    @Query("SELECT * FROM chat_messages WHERE groupId = :groupId ORDER BY id ASC")
    fun getMessagesForGroup(groupId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessages(messages: List<ChatMessageEntity>)
}
