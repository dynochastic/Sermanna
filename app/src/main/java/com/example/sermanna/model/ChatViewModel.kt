import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.sermanna.activity.Account
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Message(
    val content: String = "",
    val senderEmail: String = "",
    val receiverEmail: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

class PrivateChatViewModel(
    private val senderEmail: String,
    private val receiverEmail: String
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    var messageText by mutableStateOf("")

    private fun getChatId() = listOf(senderEmail, receiverEmail).sorted().joinToString("_")

    init {
        loadMessages()
    }

    private fun loadMessages() {
        val chatId = getChatId()
        db.collection("privateChats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val msgs = snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
                _messages.value = msgs
            }
    }

    fun sendMessage() {
        if (messageText.isBlank()) return

        val chatId = getChatId()
        val msg = Message(
            content = messageText,
            senderEmail = senderEmail,
            receiverEmail = receiverEmail,
            timestamp = System.currentTimeMillis()
        )
        db.collection("privateChats")
            .document(chatId)
            .collection("messages")
            .add(msg)
            .addOnSuccessListener { messageText = "" }
    }

    fun loadReceiverInfo(email: String, onResult: (Account?) -> Unit) {
        db.collection("Accounts")
            .document(email)
            .get()
            .addOnSuccessListener { doc ->
                onResult(doc.toObject(Account::class.java))
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}
