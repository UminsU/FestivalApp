package com.example.app

import FragmentHome
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // 광고 뷰
    lateinit var mAdView: AdView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 툴바 설정
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_include)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null

        // 광고 초기화 및 로드
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // DrawerLayout과 NavigationView 설정
        drawerLayout = findViewById(R.id.main_layout_drawer)
        navigationView = findViewById(R.id.naviView)
        navigationView.setNavigationItemSelectedListener(this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 처음 시작할 때 홈 프래그먼트 표시
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, FragmentHome())
                .commit()
        }
        bottomNavigationView.selectedItemId = R.id.btn_home

        // 하단 네비바 클릭 이벤트
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.btn_home -> selectedFragment = FragmentHome()
                R.id.btn_dashboard -> selectedFragment = FragmentBoard()
                R.id.btn_notifications -> selectedFragment = FragmentRec()
                R.id.btn_Fal -> selectedFragment = FragmentFal()
                R.id.btn_mypage -> selectedFragment = FragmentMypage()
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, selectedFragment)
                    .commit()
            }
            true
        }
        //새로 추가
    }
    // 툴바에 메뉴 추가
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // 툴바 메뉴 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tool_menu -> {
                drawerLayout.openDrawer(GravityCompat.END)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // 사이드 메뉴 버튼 클릭 이벤트
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.side_mypage_button -> Toast.makeText(applicationContext, "마이페이지", Toast.LENGTH_SHORT).show()
            R.id.side_board_button -> Toast.makeText(applicationContext, "게시판", Toast.LENGTH_SHORT).show()
            R.id.side_reivew_button -> Toast.makeText(applicationContext, "내 리뷰", Toast.LENGTH_SHORT).show()
            R.id.side_write_button -> {
                val intent = Intent(this, ActivityWriting::class.java)
                startActivity(intent)
            }
            R.id.side_login_button -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawers()
        return false
    }

    // LOGIN
    inner class subMainActivity : AppCompatActivity() {

        private lateinit var googleSignInClient: GoogleSignInClient

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)

            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this@subMainActivity, gso)

            findViewById<View>(R.id.google_sign_in_button).setOnClickListener {
                signIn()
            }
        }

        private fun signIn() {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }

        private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
            Log.d(TAG, "firebaseAuthWithGoogle:" + acct?.id)
            val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
            Firebase.auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")
                        val user = Firebase.auth.currentUser
                        // Update UI with the signed-in user's information
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        // Update UI to show sign-in failed
                    }
                }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleActivity"
    }
}