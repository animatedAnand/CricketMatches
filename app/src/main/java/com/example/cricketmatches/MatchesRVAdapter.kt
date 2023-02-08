package com.example.cricketmatches

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cricketmatches.model.MatchesRVModal
import com.example.cricketmatches.model.Score

class MatchesRVAdapter(private val matchList: List<MatchesRVModal>, private val ctx: Context) :
    RecyclerView.Adapter<MatchesRVAdapter.ViewHolder>() {

    // on below line creating a view holder class.
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        // on below line initializing variables with id.
        val matchTitleTV: TextView = itemView.findViewById(R.id.idTVMatchTitle)
        val team1TV: TextView = itemView.findViewById(R.id.idTvTeam1)
        val teams2TV: TextView = itemView.findViewById(R.id.idTvTeam2)
        val team1ScoreTV: TextView = itemView.findViewById(R.id.idTvScore1)
        val team2ScoreTV: TextView = itemView.findViewById(R.id.idTvScore2)
        val matchStatusTV: TextView = itemView.findViewById(R.id.idTVMatchStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // on below line inflating our layout file.
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.match_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // on below line setting data to text view.
        holder.matchTitleTV.text = matchList.get(position).matchName
        holder.matchStatusTV.text = matchList.get(position).status

        val teamsList: List<String> = matchList.get(position).teams
        holder.team1TV.text = matchList.get(position).teams.get(0)
        holder.teams2TV.text = matchList.get(position).teams.get(1)

        // on below line adding click listener for recycler view item.
        holder.itemView.setOnClickListener {
            // starting a new intent on below line.
            val intent = Intent(ctx, MatchScoreActivity::class.java)
            intent.putExtra("matchID", matchList.get(position).matchID)
            ctx.startActivity(intent)
        }
        val scoreList: List<Score> = matchList.get(position).scores
        holder.team1ScoreTV.text =
            matchList.get(position).scores.get(0).runs.toString() + "/" + matchList.get(position).scores.get(
                0
            ).wickets + "(" + matchList.get(position).scores.get(0).overs + ")"
        if (matchList.get(position).scores.size == 2) {
            holder.team2ScoreTV.text =
                matchList.get(position).scores.get(1).runs.toString() + "/" + matchList.get(position).scores.get(
                    1
                ).wickets + "(" + matchList.get(position).scores.get(1).overs + ")"
        }
    }

    override fun getItemCount(): Int {
        // returning size of list
        return matchList.size
    }
}
