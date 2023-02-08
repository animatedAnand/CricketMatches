package com.example.cricketmatches

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cricketmatches.model.MatchesRVModal
import com.example.cricketmatches.model.Score
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    // on below line creating variable for
    // api url, recycler view and progress bar.
    var apiURL: String =
        "https://api.cricapi.com/v1/currentMatches?apiKey=591473ba-0c66-4e61-a991-6767062605ce&offset=0"
    lateinit var matchRV: RecyclerView
    lateinit var loadingPB: ProgressBar

    // on below line specifying
    // refresh time for score.
    val period: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing variables on below line.
        matchRV = findViewById(R.id.idRVMatches)
        loadingPB = findViewById(R.id.idPBLoading)

        // on below line specifying timer to make
        // api call after every 5 seconds.
        Timer().schedule(object : TimerTask() {
            @Override
            override fun run() {
                // on below line calling get matches
                // data after specific time interval
                getMatchesData()
            }
        }, 0, period)

    }

    private fun getMatchesData() {
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)

        // on below line we are creating a variable for request
        // and initializing it with json object request
        val request = JsonObjectRequest(Request.Method.GET, apiURL, null, { response ->

            // on below line we are adding a try catch block.
            try {
                // on below line we are getting
                // data from our response
                // and setting it in variables.
                val dataArray: JSONArray = response.getJSONArray("data")

                loadingPB.visibility = View.GONE
                matchRV.visibility = View.VISIBLE

                val matchesScoresRVList = ArrayList<MatchesRVModal>()

                for (i in 0 until dataArray.length()) {

                    var dataObj = dataArray.getJSONObject(i)
                    var matchName: String = dataObj.getString("name")
                    var matchType: String = dataObj.getString("matchType")
                    var matchstatus: String = dataObj.getString("status")
                    var matchvenue: String = dataObj.getString("venue")
                    var matchdate: String = dataObj.getString("date")
                    var matchID: String = dataObj.getString("id")


                    val teamsList = ArrayList<String>()
                    val scoreCardList = ArrayList<Score>()

                    var teamsArray: JSONArray = dataObj.getJSONArray("teams")
                    for (j in 0 until teamsArray.length()) {
                        teamsList.add(teamsArray.getString(j))
                    }
                    Log.e("TAG", "TEams list is : " + teamsList)

                    var scoreArray: JSONArray = dataObj.getJSONArray("score")
                    for (k in 0 until scoreArray.length()) {
                        var scoreObj = scoreArray.getJSONObject(k)
                        var runs = scoreObj.getInt("r")
                        var overs = scoreObj.getInt("o")
                        var wickets = scoreObj.getInt("w")
                        var innings = scoreObj.getString("inning")
                        scoreCardList.add(Score(runs, wickets, overs, innings))
                    }
                    Log.e("TAG", "Score list is : " + scoreCardList)


                    matchesScoresRVList.add(
                        MatchesRVModal(
                            matchID,
                            matchName,
                            matchType,
                            matchstatus,
                            matchvenue,
                            matchdate,
                            teamsList,
                            scoreCardList
                        )
                    )
                    Log.e("TAG", "Data form api is : " + matchesScoresRVList)


                }
                val matchRVAdapter = MatchesRVAdapter(matchesScoresRVList, this)
                matchRV.adapter = matchRVAdapter


            } catch (e: Exception) {
                // on below line we are
                // handling our exception.
                e.printStackTrace()
            }

        }, { error ->
            // this method is called when we get
            // any error while fetching data from our API
            // in this case we are simply displaying a toast message.
            Toast.makeText(this@MainActivity, "Fail to get response", Toast.LENGTH_SHORT)
                .show()
        })
        // at last we are adding
        // our request to our queue.
        queue.add(request)

    }
}
