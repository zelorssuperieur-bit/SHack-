package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val userId: String = "sp_user_9082",
    val username: String = "Nouvel utilisateur",
    val photoUrl: String = "",
    val bio: String = "Gamer pro | Passionné d'esport",
    val gameId: String = "FF_PRO_8841",
    val followersCount: Int = 1420,
    val followingCount: Int = 380,
    val walletBalanceFcfa: Int = 12500,
    val isVerified: Boolean = true,
    val rankTitle: String = "Legende Diamant"
)

@Entity(tableName = "tournaments")
data class TournamentEntity(
    @PrimaryKey val id: String,
    val title: String,
    val gameCategory: String, // e.g., "Free Fire", "PUBG Mobile", "COD Mobile", "FC 25"
    val priceFcfa: Int,
    val playersCount: Int,
    val maxPlayers: Int,
    val prizePoolFcfa: Int,
    val startTime: String,
    val status: String, // "A VENIR", "EN COURS", "TERMINE"
    val isRegistered: Boolean = false,
    val bannerType: String = "DEFAULT" // "FREE_FIRE", "PUBG", "HERO"
)

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorTag: String,
    val content: String,
    val gameTag: String,
    val timestamp: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false,
    val mediaType: String = "NONE" // "NONE", "IMAGE", "VIDEO_THUMB"
)

@Entity(tableName = "communities")
data class CommunityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val gameType: String,
    val membersCount: Int,
    val description: String,
    val isJoined: Boolean = false
)

@Entity(tableName = "payments")
data class PaymentTransactionEntity(
    @PrimaryKey val id: String,
    val method: String, // "Orange Money", "Mobile Money", "Carte bancaire", "PayPal"
    val amountFcfa: Int,
    val title: String,
    val date: String,
    val status: String // "SUCCESS", "PENDING"
)

@Entity(tableName = "chat_groups")
data class ChatGroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // "GROUP" or "CHANNEL"
    val gameType: String,
    val membersCount: Int,
    val description: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isVerified: Boolean = false,
    val iconType: String = "DEFAULT" // "DEFAULT", "SQUAD", "FIRE", "PUBG", "CHANNEL"
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val senderName: String,
    val senderTag: String,
    val message: String,
    val timestamp: String,
    val isMe: Boolean = false,
    val mediaType: String = "TEXT" // "TEXT", "IMAGE", "AUDIO"
)
