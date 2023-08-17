package com.example.timerpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.lang.Math.abs
import java.util.Random
import java.util.Timer

class MainActivity : AppCompatActivity() {

    var p_num = 2
    var k = 1 // index of player.
    val score_list = mutableListOf<Float>()
    var isBlind = false

    fun start() {

        setContentView(R.layout.activity_start) // load XML file of Main Page
        val tv_pnum: TextView = findViewById(R.id.tv_pnum)
        val btn_minus: TextView = findViewById(R.id.btn_minus)
        val btn_plus: TextView = findViewById(R.id.btn_plus)
        val btn_start: TextView = findViewById(R.id.btn_start)
        val btn_blind: TextView = findViewById(R.id.btn_blind)

        btn_blind.setOnClickListener {
            isBlind = !isBlind
            if(isBlind == true) {
                btn_blind.text = "Blind Mode ON"
            } else {
                btn_blind.text = "Blind Mode OFF"
            }
        }

        tv_pnum.text = p_num.toString()

        btn_minus.setOnClickListener {
            p_num--
            if(p_num == 0) {
                p_num = 1
            }
            tv_pnum.text = p_num.toString()
        }
        btn_plus.setOnClickListener {
            p_num++
            tv_pnum.text = p_num.toString()
        }
        btn_start.setOnClickListener {
            main()
        }

    }


    fun main() {

        setContentView(R.layout.activity_main) // load XML file

        var timerTask: Timer? = null // nullable
        var stage = 1
        var sec: Int = 0

        // Views
        val tv: TextView = findViewById(R.id.tv_pnum)
        val tv_t: TextView = findViewById(R.id.tv_timer)
        val tv_d: TextView = findViewById(R.id.tv_diff)
        val tv_players: TextView = findViewById(R.id.tv_players)
        val btn: Button = findViewById(R.id.btn_start)

        // Set random time when the application is started.
        val random_box = Random()
        val num = random_box.nextInt(1001)

        tv.text = (num.toFloat()/100).toString() // Show random number on 'tv(tv_random)'
        btn.text = "Start"
        tv_players.text = "Player $k"

        btn.setOnClickListener {
            stage++ // there is 3 kinds of stage
            // stage 1 :
            // stage 2 : time flows, the text of btn would be changed into 'Stop'
            // stage 3 :
            if(stage == 2) {
                timerTask = kotlin.concurrent.timer(period = 10) {
                    sec++
                    runOnUiThread {
                        if(isBlind == false) {
                            tv_t.text = (sec.toFloat() / 100).toString()
                        } else if(isBlind == true && stage == 2) {
                            tv_t.text = "?.??"
                        }
                    }
                }
                btn.text = "Stop"
            } else if(stage == 3) {
                tv_t.text = (sec.toFloat() / 100).toString()
                timerTask?.cancel()
                val diff = abs(sec - num).toFloat() / 100

                score_list.add(diff)

                tv_d.text = diff.toString()
                btn.text = "Next Player"
                stage = 0
            } else if(stage == 1) {
                if(k < p_num) {
                    k++
                    main()
                } else {
                    end()
                }
            }
        }
    }

    fun end() {
        setContentView(R.layout.activity_end)

        val tv_worst: TextView = findViewById(R.id.tv_worst)
        val tv_wScore: TextView = findViewById(R.id.tv_wScore)
        val btn_restart: TextView = findViewById(R.id.btn_restart)

        tv_wScore.text = (score_list.maxOrNull()).toString()
        var index_last = score_list.indexOf(score_list.maxOrNull())
        tv_worst.text = "Player " + (index_last + 1).toString()

        btn_restart.setOnClickListener {
            k = 1
            score_list.clear()
            start()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        start()
    }
}