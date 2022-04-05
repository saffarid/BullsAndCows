package com.saffarid.bowsandcows.gui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.saffarid.bowsandcows.gui.fragments.PlayerFragment
import com.saffarid.bowsandcows.R
import com.saffarid.bowsandcows.gui.fragments.HistoryFragment
import com.saffarid.bowsandcows.gui.fragments.LeadFragment
import com.saffarid.bowsandcows.model.Difficulty
import com.saffarid.bowsandcows.model.Game
import com.google.android.gms.ads.*
import com.saffarid.bowsandcows.model.GameStatus

class MainActivity : AppCompatActivity() {

    /**
     * Контейнер длля UI-фрагмента игрока
     * */
    private var playerLayout: FrameLayout? = null
    /**
     * Контейнер длля UI-фрагмента ведущего
     * */
    private lateinit var gameLayout: ViewPager

    /**
     * Рекламный баннер
     * */
    private lateinit var adsBanner: AdView

    private val fragments = arrayListOf(LeadFragment(), HistoryFragment())

    private lateinit var fragmentManager:FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        MobileAds.initialize(this, resources.getString(R.string.appAdId))

        playerLayout = findViewById(R.id.player);
        gameLayout = findViewById(R.id.fragment_game)
        adsBanner = findViewById(R.id.banner)
        adsBanner.loadAd(AdRequest.Builder().build())

        fragmentManager = supportFragmentManager
        Game.listeners.add(this::checkGame)

        var fragmentPlayer = fragmentManager!!.findFragmentById(R.id.player)
        if(fragmentPlayer == null){
            fragmentPlayer = PlayerFragment( Game.getPlayer(), this::checkGame );
            fragmentManager
                .beginTransaction()
                .add(R.id.player, fragmentPlayer)
                .commit();
        }

        gameLayout.adapter = object : FragmentPagerAdapter(fragmentManager){
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }
            override fun getCount(): Int {
                return fragments.size
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.new_game -> { showNewGameDialog() }
            R.id.rules -> { AlertDialog.Builder(this).setView(R.layout.rules_fragment).create().show() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkGame(){
        if(Game.getGameStatus() == GameStatus.RUN) return
        var textResult:String
        if(Game.getGameStatus() == GameStatus.FAIL){
            textResult = String.format(resources.getString(R.string.fail), Game.getNumber())
        } else {
            textResult = resources.getString(R.string.win)
        }
        val v = LayoutInflater.from(this).inflate(R.layout.dialog_result, null)
        val resultDialog = AlertDialog.Builder(this)
            .setView(v)
            .create()
        v.findViewById<TextView>(R.id.result_message).text = textResult
        v.findViewById<Button>(R.id.start_new_game).setOnClickListener {
            showNewGameDialog()
            resultDialog.dismiss()
        }

        resultDialog.show()
//        showNewGameDialog()
    }

    private fun showNewGameDialog(){
//        StartNewGameDialog().show(fragmentManager, "new_game")
        val interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = resources.getString(R.string.interstitialAd)
        interstitialAd.adListener = object : AdListener(){
            override fun onAdLoaded() {
                if (interstitialAd.isLoaded){
                    interstitialAd.show()
                }
            }
        }
        interstitialAd.loadAd(AdRequest.Builder().build())
        Game.startNewGame(Difficulty.NORMAL)
    }

}
