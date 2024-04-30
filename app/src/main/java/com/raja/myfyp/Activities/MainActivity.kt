package com.raja.myfyp.Activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.raja.myfyp.BottomSheet.ExitBottomSheet
import com.raja.myfyp.Fragments.EmergencyFragment
import com.raja.myfyp.Fragments.HomeFragment
import com.raja.myfyp.Fragments.ProfileFragment
import com.raja.myfyp.Fragments.TipsFragment
import com.raja.myfyp.Interfaces.ExitListner
import com.raja.myfyp.ModelClasses.UserData
import com.raja.myfyp.R
import com.raja.myfyp.Services.CallService
import com.raja.myfyp.Services.PowerButtonService
import com.raja.myfyp.Services.ShakeDetectionService
import com.raja.myfyp.clearCredentials
import com.raja.myfyp.databinding.ActivityMainBinding
import io.paperdb.Paper

class MainActivity : BaseActivity(), ExitListner {
    lateinit var mainBinding: ActivityMainBinding
    var homeFragment: HomeFragment? = null
    var emergencyFragment: EmergencyFragment? = null
    var tipsFragment: TipsFragment? = null
    var profileFragment: ProfileFragment? = null
    var newFragment: Fragment? = null
    var ft: FragmentTransaction? = null
    var tag = ""
    var title = ""
    private var isBottomSheetVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = mainBinding!!.getRoot()
        setContentView(view)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        title = "home"
        tag = "home"


        val toggle = ActionBarDrawerToggle(
            this,
            mainBinding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        mainBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        mainBinding.menuId.setOnClickListener {
            if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                mainBinding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val logoutId =
            mainBinding.navigationView.getHeaderView(0)
                .findViewById<ImageView>(R.id.logout_id)

        val backBtn =
            mainBinding.navigationView.getHeaderView(0)
                .findViewById<ImageView>(R.id.back_btn_img)

        val myAcc = mainBinding.navigationView.getHeaderView(0)
            .findViewById<LinearLayout>(R.id.account_layout)
        val awareness =
            mainBinding.navigationView.getHeaderView(0).findViewById<LinearLayout>(R.id.awarenessId)
        val accessibility = mainBinding.navigationView.getHeaderView(0)
            .findViewById<LinearLayout>(R.id.accessibilityId)
        val settingsActivity =
            mainBinding.navigationView.getHeaderView(0).findViewById<LinearLayout>(R.id.settingsId)
        val safetyPolicy =
            mainBinding.navigationView.getHeaderView(0).findViewById<LinearLayout>(R.id.safetyId)
        val safetyLaws = mainBinding.navigationView.getHeaderView(0)
            .findViewById<LinearLayout>(R.id.safetylawsId)
        val help =
            mainBinding.navigationView.getHeaderView(0).findViewById<LinearLayout>(R.id.helpId)



        myAcc.setOnClickListener {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity, ManageProfileActivity::class.java))
        }

        awareness.setOnClickListener {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity, AwarenessDocumentActivity::class.java))
        }

        settingsActivity.setOnClickListener {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity, SOSSettingActivity::class.java))
        }

        safetyPolicy.setOnClickListener {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity, SafetyPoliciesActivity::class.java))
        }

        safetyLaws.setOnClickListener {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity, SafetyActsActivity::class.java))
        }
        help.setOnClickListener {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity, HelpCenterActivity::class.java))
        }

        accessibility.setOnClickListener {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity,AccessibilityActivity::class.java))
        }

        backBtn.setOnClickListener {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        logoutId.setOnClickListener {
            Firebase.auth.signOut()
            clearCredentials(this@MainActivity)
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@MainActivity, OTPActivity::class.java))
            finish()
        }


        val selectedColor = ContextCompat.getColor(this, R.color.one)

        val unselectedColor = ContextCompat.getColor(this, R.color.white)

        val iconTintList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(-android.R.attr.state_selected)
            ),
            intArrayOf(selectedColor, unselectedColor)
        )
        val textColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(-android.R.attr.state_selected)
            ),
            intArrayOf(selectedColor, unselectedColor)
        )


//        binding.bottomNav.itemBackground = ContextCompat.getDrawable(this, R.drawable.bottom_nav_item_background)

        mainBinding.bottomNavigationView.itemIconTintList = iconTintList


        homeFragment = HomeFragment.Companion.newInstance()
        newFragment = homeFragment
        loadFragment()
        mainBinding!!.bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            // Handle Bottom Navigation item clicks
            if (item.itemId == R.id.menu_home) { // Handle item 1 click
                title = "home"
                tag = "home"
                homeFragment = HomeFragment.Companion.newInstance()
                newFragment = homeFragment
                loadFragment()
                return@setOnNavigationItemSelectedListener true
            } else if (item.itemId == R.id.menu_emergency) {
                title = "emergency"
                tag = "emergency"
                emergencyFragment = EmergencyFragment.Companion.newInstance()
                newFragment = emergencyFragment
                loadFragment()
                return@setOnNavigationItemSelectedListener true
            } else if (item.itemId == R.id.menu_tips) {
                title = "tips"
                tag = "tips"
                tipsFragment = TipsFragment.Companion.newInstance()
                newFragment = tipsFragment
                loadFragment()
                return@setOnNavigationItemSelectedListener true
            } else if (item.itemId == R.id.menu_profile) {
                title = "profile"
                tag = "profile"
                profileFragment = ProfileFragment.Companion.newInstance()
                newFragment = profileFragment
                loadFragment()
                return@setOnNavigationItemSelectedListener true
            }
            false
        }


        // Create a callback for back button press
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = getCurrentFragment()
                if (currentFragment is HomeFragment) {
                    if (!isBottomSheetVisible) {
                        showBottomSheet()
                    }
//                    showExitDialog()
//                    finish()
                } else {
                    title = "home"
                    tag = "home"
                    homeFragment = HomeFragment.Companion.newInstance()
                    newFragment = homeFragment
                    loadFragment()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    fun showBottomSheet() {
        val bottomSheetFragment =
            ExitBottomSheet(this@MainActivity)
        bottomSheetFragment.setListener(this)
        bottomSheetFragment.show(supportFragmentManager, "effects")
        isBottomSheetVisible = true

    }


    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.fragment_container)
    }


    private fun loadFragment() {
        if (newFragment != null) {
            try {
                ft = supportFragmentManager.beginTransaction()
                ft!!.replace(R.id.fragment_container, newFragment!!, tag).addToBackStack(null)
                    .commit()
            } catch (ie: Exception) {
                ie.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val powerState = Paper.book().read<Boolean>("POWER_SERVICE", false)
        val shakeState = Paper.book().read<Boolean>("SHAKE_SERVICE", false)

        powerState?.let {
            if (it) {
                startService(Intent(this@MainActivity, PowerButtonService::class.java))
                stopService(Intent(this@MainActivity,ShakeDetectionService::class.java))
            }
        }
        shakeState?.let {
            if (it) {
                startService(Intent(this@MainActivity, ShakeDetectionService::class.java))
                stopService(Intent(this@MainActivity,PowerButtonService::class.java))

            }
        }


        val nameId =
            mainBinding.navigationView.getHeaderView(0)
                .findViewById<TextView>(R.id.userNameId)
        val phoneId =
            mainBinding.navigationView.getHeaderView(0)
                .findViewById<TextView>(R.id.phoneId)


        val userData = Paper.book().read<UserData>("USER_DATA")
        userData?.let {
            nameId.text = it.name
            phoneId.text = it.phone
        }
    }


    fun setTitleFrgment(fragmentName: String) {
        mainBinding.fragmentNameId.text = fragmentName
    }

    override fun exitBottomSheetCallback(isExit: Boolean) {
        isBottomSheetVisible = false
        if (isExit) {
            Firebase.auth.signOut()
            clearCredentials(this@MainActivity)
            startActivity(Intent(this@MainActivity, OTPActivity::class.java))
            finish()
        }

    }


}