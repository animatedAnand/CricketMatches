package com.example.cricketmatches
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cricketmatches.model.MatchesRVModal
import com.example.cricketmatches.model.Score
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MatchScoreActivity : AppCompatActivity() {

    // on below line creating variables for text views.
    lateinit var matchTitleTV: TextView
    lateinit var matchNameTV: TextView
    lateinit var team1TV: TextView
    lateinit var team2TV: TextView
    lateinit var team1ScoreTV: TextView
    lateinit var team2ScoreTV: TextView
    lateinit var matchStatusTV: TextView
    lateinit var matchDateTV: TextView
    lateinit var matchTypeTV: TextView
    lateinit var matchVenueTV: TextView
    lateinit var matchTossTV: TextView
    lateinit var loadingPB: ProgressBar
    var matchID: String? =""

    // on below line specifying refresh time for score.
    val period: Long = 5000

    // on below line specifying api url
    var apiURL: String =
        "https://api.cricapi.com/v1/currentMatches?apiKey=591473ba-0c66-4e61-a991-6767062605ce&offset=0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_score)

        // on below line initializing all variables.
        matchTitleTV = findViewById(R.id.idTVMatchTitle)
        matchNameTV = findViewById(R.id.idTVMatchName)
        team1TV = findViewById(R.id.idTvTeam1)
        team2TV = findViewById(R.id.idTvTeam2)
        team1ScoreTV = findViewById(R.id.idTvScore1)
        team2ScoreTV = findViewById(R.id.idTvScore2)
        matchStatusTV = findViewById(R.id.idTVMatchStatus)
        matchDateTV = findViewById(R.id.idTVMatchDate)
        matchTypeTV = findViewById(R.id.idTVMatchType)
        matchVenueTV = findViewById(R.id.idTVMatchVenue)
        matchTossTV = findViewById(R.id.idTVToss)
        loadingPB = findViewById(R.id.idPBLoading)

        // on below line getting match id through intent.
        matchID = intent.getStringExtra("matchID")

        // on below line specifying timer to
        // make api call after every 5 seconds.
        Timer().schedule(object : TimerTask() {
            @Override
            override fun run() {
                // on below line calling get score
                // data after specific time interval
                getScoreData()
            }
        }, 0, period)

    }

    private fun getScoreData() {
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)

        // on below line we are creating a variable for request
        // and initializing it with json object request
        val request = JsonObjectRequest(Request.Method.GET, apiURL + matchID, null, { response ->

            // on below line we are adding a try catch block.
            try {
                // on below line we are getting data from our response
                // and setting it in variables.
                val dataObj: JSONObject = response.getJSONObject("data")

                // on below line changing visibility of progress bar.
                loadingPB.visibility = View.GONE

                // on below line initializing list.
                val teamsList = ArrayList<String>()
                val scoreCardList = ArrayList<Score>()
                val matchesScoresRVList = ArrayList<MatchesRVModal>()

                // on below line getting data from api.
                var matchName: String = dataObj.getString("name")
                var matchType: String = dataObj.getString("matchType")
                var matchstatus: String = dataObj.getString("status")
                var matchvenue: String = dataObj.getString("venue")
                var matchdate: String = dataObj.getString("date")

                var teamsArray: JSONArray = dataObj.getJSONArray("teams")
                for (j in 0 until teamsArray.length()) {
                    teamsList.add(teamsArray.getString(j))
                }
                var scoreArray: JSONArray = dataObj.getJSONArray("score")
                for (k in 0 until scoreArray.length()) {
                    var scoreObj = scoreArray.getJSONObject(k)
                    var runs = scoreObj.getInt("r")
                    var overs = scoreObj.getInt("o")
                    var wickets = scoreObj.getInt("w")
                    var innings = scoreObj.getString("inning")
                    scoreCardList.add(Score(runs, overs, wickets, innings))
                }

                // on below line setting data
                // for our text views.
                matchTitleTV.text = matchName
                matchNameTV.text = matchName
                matchTypeTV.text = matchType
                matchStatusTV.text = matchstatus
                matchVenueTV.text = matchvenue
                matchDateTV.text = matchdate
                team1TV.text = teamsList.get(0)
                team2TV.text = teamsList.get(1)

                team1ScoreTV.text =
                    scoreCardList.get(0).runs.toString() + "/" + scoreCardList.get(
                        0
                    ).wickets + "(" + scoreCardList.get(0).overs + ")"

                team2ScoreTV.text =
                    scoreCardList.get(1).runs.toString() + "/" + scoreCardList.get(
                        1
                    ).wickets + "(" + scoreCardList.get(1).overs + ")"

                Log.e("TAG", "Data form api is : " + matchesScoresRVList)


            } catch (e: Exception) {
                // on below line we are
                // handling our exception.
                e.printStackTrace()
            }

        }, { error ->
            // this method is called when we get
            // any error while fetching data from our API
            // in this case we are simply displaying a toast message.
            Toast.makeText(this@MatchScoreActivity, "Fail to get response", Toast.LENGTH_SHORT)
                .show()
        })
        // at last we are adding
        // our request to our queue.
        queue.add(request)
    }
}
