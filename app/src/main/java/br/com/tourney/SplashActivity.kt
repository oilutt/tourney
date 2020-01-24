package br.com.tourney

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.com.commons.SampleNavigation

class SplashActivity : AppCompatActivity() {

    private lateinit var mDelayHandler: Handler
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            SampleNavigation.dynamicStart?.let { startActivity(it) }
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mDelayHandler = Handler()
        mDelayHandler.postDelayed(mRunnable, 3000)
    }

    public override fun onDestroy() {
        mDelayHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }
}