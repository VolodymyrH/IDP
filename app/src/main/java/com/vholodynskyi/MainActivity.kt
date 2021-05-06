package com.vholodynskyi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.vholodynskyi.game.GameView
import com.vholodynskyi.graph.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameView = findViewById<GameView>(R.id.gameView)

        val up = findViewById<AppCompatButton>(R.id.up)
        val down = findViewById<AppCompatButton>(R.id.down)
        val right = findViewById<AppCompatButton>(R.id.right)
        val left = findViewById<AppCompatButton>(R.id.left)
        val fire = findViewById<AppCompatButton>(R.id.fire)

        up.setOnClickListener { gameView.up() }
        down.setOnClickListener { gameView.down() }
        right.setOnClickListener { gameView.right() }
        left.setOnClickListener { gameView.left() }
        fire.setOnClickListener { gameView.fire() }
    }
}