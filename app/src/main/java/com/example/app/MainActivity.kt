package com.example.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // 광고 뷰
    lateinit var mAdView: AdView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var loginMenuItem: MenuItem
    private lateinit var logoutMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Firebase Auth 초기화
        auth = Firebase.auth

        // Google Sign-In 초기화
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

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
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, FragmentHome()).commit()
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
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, selectedFragment).commit()
            }
            true
        }
        loginMenuItem = navigationView.menu.findItem(R.id.side_login_button)
        logoutMenuItem = navigationView.menu.findItem(R.id.side_logout_button)

        // 로그인 상태에 따라 로그인 및 로그아웃 버튼 활성화/비활성화 설정
        updateNavigationMenu()

        // 사이드 바 상단 텍스트와 이미지 변경
        val headerView = navigationView.getHeaderView(0)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.user_email)
        val userImageView = headerView.findViewById<ImageView>(R.id.user_image)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userEmailTextView.text = currentUser.email ?: "로그인해주세요"
            // 이미지 색상 변경
            userImageView.setColorFilter(Color.parseColor("#FF3700B3")) // 로그인되었을 때 이미지 색상 변경
        } else {
            userEmailTextView.text = "로그인해주세요"
            // 로그인되지 않았을 때 이미지 색상 및 이미지 설정
            userImageView.setColorFilter(Color.parseColor("#FF000000")) // 원래 색상으로 변경
            userImageView.setImageResource(R.drawable.myp02) // 기본 이미지 설정
        }
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
            R.id.side_mypage_button -> {
                val fragment = FragmentMypage()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, fragment).commit()
                Toast.makeText(applicationContext, "마이페이지", Toast.LENGTH_SHORT).show()
            }

            R.id.side_board_button -> {
                val fragment = FragmentBoard()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, fragment).commit()
                Toast.makeText(applicationContext, "게시판", Toast.LENGTH_SHORT).show()
            }
            R.id.side_reivew_button -> {
                val fragment = FragmentFal()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, fragment).commit()
                Toast.makeText(applicationContext, "찜목록", Toast.LENGTH_SHORT).show()
            }
            R.id.side_write_button -> {
                val intent = Intent(this, BoardActivity::class.java)
                startActivity(intent)
            }
            R.id.side_login_button -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.side_logout_button -> {
                logout()
            }
        }
        drawerLayout.closeDrawers()
        return false
    }



    private fun logout() {
        val logoutMenuItem = navigationView.menu.findItem(R.id.side_logout_button)
        logoutMenuItem.isEnabled = false  // 로그아웃 버튼 비활성화
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티를 종료하여 뒤로 가기 버튼을 눌렀을 때 다시 메인 액티비티로 돌아가지 않도록 합니다.
        }
    }

    private fun updateNavigationMenu() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // 로그인 상태일 때
            loginMenuItem.isVisible = false
            logoutMenuItem.isVisible = true
        } else {
            // 로그아웃 상태일 때
            loginMenuItem.isVisible = true
            logoutMenuItem.isVisible = false
        }
    }
}
