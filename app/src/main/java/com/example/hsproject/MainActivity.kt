package com.example.hsproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.hsproject.adapters.MainViewPagerAdapter
import com.example.hsproject.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var mPageAdapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        //뷰페이저2는 페이지와 카테고리가 같이 이동 X 그래서 이벤트 달아줘야 함
        binding.bottomNav.setOnItemSelectedListener {
            //it에는 menu->bottom_nav_menu.xml의 아이템이 들어있다
            when (it.itemId) {//아이템 번호에 따라서 화면을 설정한다
                R.id.myAppointment -> {
                    binding.mainViewPager.currentItem = 0
                    //메인/약속/세팅의 액션바는 여기서 조절해주기
                    //근데 그러면 +버튼이 늦게 생성되기때문에 꼭 물어보기!! ㅠㅠ
                    titleTxt.text = "약속 지킴이"
                    addIcon.visibility = View.VISIBLE
                    addIcon.setOnClickListener {
                        val myIntent = Intent(mContext, AddAppointmentActivity::class.java)
                        startActivity(myIntent)
                    }
                }
                R.id.inbitedAppointment -> {
                    binding.mainViewPager.currentItem = 1
                    addIcon.visibility = View.GONE
                }
                R.id.setting -> {
                    binding.mainViewPager.currentItem = 2
                    addIcon.visibility = View.GONE
                }
                //네이게이션을 누르면 화면이 이동! 단 화면이동시 네비는 이동 x
            }
            return@setOnItemSelectedListener true
        }
        //페이지가 바뀌면 응답을 담는다
        binding.mainViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                //그 응답을 이용해 페이지 변경
            //추상메서드가 없어 내가 직접 오버라이딩 해줘야 한다
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNav.selectedItemId = when(position){
                        0 -> R.id.myAppointment
                        1 -> R.id.inbitedAppointment
                        else -> R.id.setting
                    }
                }

        })
        /*여기서 액션 바 클릭 이벤트 만들어주기 동일*/
    }

    override fun setValues() {
        //액션바 타이틀 이름 변경
        addIcon.visibility = View.VISIBLE

        mPageAdapter = MainViewPagerAdapter(this) //여기에 어답타 붙이고
        binding.mainViewPager.adapter = mPageAdapter //연결하면 여기에 프레그먼트들이 붙음
    }


}
