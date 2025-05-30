package com.example.sermanna.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.sermanna.activity.Account
import com.google.firebase.firestore.FirebaseFirestore

class SearchViewModel : ViewModel() {
    var searchResults by mutableStateOf<List<Account>>(emptyList())
        private set

    fun search(query: String) {
        if (query.isBlank()) {
            searchResults = emptyList()
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("Accounts")
            .orderBy("fullName")
            .startAt(query.lowercase())
            .endAt(query.lowercase() + "\uf8ff")
            .get()
            .addOnSuccessListener { result ->
                searchResults = result.toObjects(Account::class.java)
            }
    }
}
