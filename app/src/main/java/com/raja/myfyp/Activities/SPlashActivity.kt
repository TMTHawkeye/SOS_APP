package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.raja.myfyp.R
import com.raja.myfyp.isFirstTimeLaunch

class SPlashActivity : BaseActivity() {
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.one)
        handler = Handler()
        handler!!.postDelayed({
//            if (isFirstTimeLaunch()) {
                val intent = Intent(this, UserCategoryActivity::class.java)
                startActivity(intent)
                finish()
//            } else {
//                startActivity(
//                    Intent(
//                        this@SPlashActivity,
//                        MainActivity::class.java
//                    )
//                )
//                finish()
//            }
        }, 3000)
    }
}