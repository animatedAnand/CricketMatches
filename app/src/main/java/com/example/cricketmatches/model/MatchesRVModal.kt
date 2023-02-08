package com.example.cricketmatches.model

data class MatchesRVModal(
    // on below line creating variables
    var matchID: String,
    var matchName: String,
    var matchType: String,
    var status: String,
    var venue: String,
    var date: String,
    var teams: List<String>,
    var scores: List<Score>
)
