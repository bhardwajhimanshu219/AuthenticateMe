package com.example.eventorganiser

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TransactionTooLargeException
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var toolbar:Toolbar
    lateinit var allEvents:AllEvents
    lateinit var goToEvents: GoToEvents
    lateinit var hostEvents: HostEvents
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        allEvents = AllEvents()
        goToEvents = GoToEvents()
        hostEvents = HostEvents()
        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabLayout)
        setSupportActionBar(toolbar)
        tabLayout.setupWithViewPager(viewPager)
        val fragmentAdapter = MyPagerAdapter(supportFragmentManager, 0)
        fragmentAdapter.addFragment(allEvents, "All Events")
        fragmentAdapter.addFragment(goToEvents, "Going To Events")
        fragmentAdapter.addFragment(hostEvents, "Host My Event")
        viewPager.adapter = fragmentAdapter
    }
}

class MyPagerAdapter(supportFragmentManager: FragmentManager, i: Int) : FragmentPagerAdapter(supportFragmentManager, i) {
    private var listFragment : ArrayList<Fragment> = ArrayList()
    private var listFragmentString : ArrayList<String> = ArrayList()

     fun addFragment(fm: Fragment, name: String){
        listFragment.add(fm)
         listFragmentString.add(name)
    }
    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listFragmentString[position]
    }
}





