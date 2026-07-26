package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ChatGroupEntity
import com.example.data.ChatMessageEntity
import com.example.data.CommunityEntity
import com.example.data.PaymentTransactionEntity
import com.example.data.PostEntity
import com.example.data.SmartPlusRepository
import com.example.data.TournamentEntity
import com.example.data.UserProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AuthState {
    LOGGED_OUT,
    SMS_VERIFICATION,
    LOGGED_IN
}

class SmartPlusViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SmartPlusRepository

    val userProfile: StateFlow<UserProfileEntity?>
    val tournaments: StateFlow<List<TournamentEntity>>
    val posts: StateFlow<List<PostEntity>>
    val communities: StateFlow<List<CommunityEntity>>
    val payments: StateFlow<List<PaymentTransactionEntity>>
    val chatGroups: StateFlow<List<ChatGroupEntity>>

    private val _activeGroup = MutableStateFlow<ChatGroupEntity?>(null)
    val activeGroup: StateFlow<ChatGroupEntity?> = _activeGroup.asStateFlow()

    private val _activeMessages = MutableStateFlow<List<ChatMessageEntity>>(emptyList())
    val activeMessages: StateFlow<List<ChatMessageEntity>> = _activeMessages.asStateFlow()

    private val _authState = MutableStateFlow(AuthState.LOGGED_IN)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _selectedGameFilter = MutableStateFlow("TOUS")
    val selectedGameFilter: StateFlow<String> = _selectedGameFilter.asStateFlow()

    private val _paymentModalTournament = MutableStateFlow<TournamentEntity?>(null)
    val paymentModalTournament: StateFlow<TournamentEntity?> = _paymentModalTournament.asStateFlow()

    private val _showTopUpModal = MutableStateFlow(false)
    val showTopUpModal: StateFlow<Boolean> = _showTopUpModal.asStateFlow()

    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage: StateFlow<String?> = _snackMessage.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).smartPlusDao()
        repository = SmartPlusRepository(dao)

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        tournaments = repository.tournaments.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        posts = repository.posts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        communities = repository.communities.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        payments = repository.payments.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        chatGroups = repository.chatGroups.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun openChatGroup(group: ChatGroupEntity) {
        _activeGroup.value = group
        viewModelScope.launch {
            repository.getMessagesForGroup(group.id).collect { msgs ->
                _activeMessages.value = msgs
            }
        }
    }

    fun closeChatGroup() {
        _activeGroup.value = null
        _activeMessages.value = emptyList()
    }

    fun sendChatMessage(text: String) {
        val group = _activeGroup.value ?: return
        if (text.isBlank()) return

        val username = userProfile.value?.username ?: "Moi"
        viewModelScope.launch {
            repository.sendMessage(group.id, text, username)

            // Simulate instant response from teammate if in squad group chat
            if (group.type == "GROUP") {
                kotlinx.coroutines.delay(1200)
                val teammateMsg = when ((1..3).random()) {
                    1 -> "Jordan Sniper: 'Reçu chef ! Je suis prêt pour le scrim 💥'"
                    2 -> "Sarra GM: 'Top ! Je viens de réserver ma place sur Smart Plus.'"
                    else -> "Viper Bot: 'Position confirmée. On fonce ! 🏆'"
                }
                repository.sendMessage(group.id, teammateMsg, "Squad Teammate")
            }
        }
    }

    fun createGroupOrChannel(name: String, type: String, gameType: String, description: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.createNewGroupOrChannel(name, type, gameType, description)
            showSnack("${if (type == "CHANNEL") "Chaîne" else "Groupe"} '$name' créé avec succès !")
        }
    }

    fun setPhoneNumber(phone: String) {
        _phoneNumber.value = phone
    }

    fun requestPhoneCode() {
        if (_phoneNumber.value.length >= 8) {
            _authState.value = AuthState.SMS_VERIFICATION
            showSnack("Code de vérification envoyé au ${_phoneNumber.value}")
        } else {
            showSnack("Veuillez entrer un numéro de téléphone valide")
        }
    }

    fun confirmSmsCode(code: String) {
        if (code.length == 6 || code == "123456") {
            _authState.value = AuthState.LOGGED_IN
            showSnack("Connexion réussie ! Bienvenue sur Smart Plus")
        } else {
            showSnack("Code incorrect. Utilisez 123456 pour démo")
        }
    }

    fun loginWithGoogle() {
        _authState.value = AuthState.LOGGED_IN
        showSnack("Connecté avec Google Sign-In !")
    }

    fun logout() {
        _authState.value = AuthState.LOGGED_OUT
        showSnack("Déconnexion effectuée")
    }

    fun setGameFilter(filter: String) {
        _selectedGameFilter.value = filter
    }

    fun openPaymentModal(tournament: TournamentEntity) {
        _paymentModalTournament.value = tournament
    }

    fun closePaymentModal() {
        _paymentModalTournament.value = null
    }

    fun openTopUpModal() {
        _showTopUpModal.value = true
    }

    fun closeTopUpModal() {
        _showTopUpModal.value = false
    }

    fun processTournamentPayment(method: String) {
        val tourn = _paymentModalTournament.value ?: return
        val currentWallet = userProfile.value?.walletBalanceFcfa ?: 0

        viewModelScope.launch {
            if (method == "Portefeuille Smart Plus" && currentWallet < tourn.priceFcfa) {
                showSnack("Solde insuffisant dans votre portefeuille !")
                return@launch
            }

            repository.processTournamentPayment(
                tournamentId = tourn.id,
                tournamentTitle = tourn.title,
                amountFcfa = tourn.priceFcfa,
                paymentMethod = method
            )

            closePaymentModal()
            showSnack("Participation confirmée pour ${tourn.title} !")
        }
    }

    fun processTopUp(amountFcfa: Int, method: String) {
        viewModelScope.launch {
            repository.addWalletFunds(amountFcfa, method)
            closeTopUpModal()
            showSnack("Portefeuille crédité de $amountFcfa FCFA via $method !")
        }
    }

    fun toggleLike(postId: String) {
        viewModelScope.launch {
            repository.toggleLike(postId)
        }
    }

    fun toggleJoinCommunity(communityId: String) {
        viewModelScope.launch {
            repository.toggleJoinCommunity(communityId)
        }
    }

    fun createPost(content: String, gameTag: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.createPost(content, gameTag)
            showSnack("Publication publiée sur la communauté !")
        }
    }

    fun updateProfile(username: String, bio: String, gameId: String) {
        viewModelScope.launch {
            repository.updateProfile(username, bio, gameId)
            showSnack("Profil mis à jour avec succès !")
        }
    }

    fun showSnack(msg: String) {
        _snackMessage.value = msg
    }

    fun clearSnack() {
        _snackMessage.value = null
    }

    fun getInviteLink(): String {
        val userId = userProfile.value?.userId ?: "guest_user"
        return "https://smartplus.com/invite/$userId"
    }
}
