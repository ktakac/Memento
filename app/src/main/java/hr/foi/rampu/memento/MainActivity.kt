package hr.foi.rampu.memento

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.wearable.Wearable
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.rampu.memento.adapters.MainPagerAdapter
import hr.foi.rampu.memento.database.TasksDatabase
import hr.foi.rampu.memento.fragments.CompletedFragment
import hr.foi.rampu.memento.fragments.NewsFragment
import hr.foi.rampu.memento.fragments.PendingFragment
import hr.foi.rampu.memento.helpers.MockDataLoader
import hr.foi.rampu.memento.helpers.TaskDeletionServiceHelper
import hr.foi.rampu.memento.sync.WearableSynchronizer

class MainActivity : AppCompatActivity() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var navDrawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    private val taskDeletionServiceHelper by lazy { TaskDeletionServiceHelper(applicationContext) }
    private val dataClient by lazy { Wearable.getDataClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabs)
        viewPager2 = findViewById(R.id.viewpager)
        navDrawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)

        val mainPagerAdapter = MainPagerAdapter(supportFragmentManager, lifecycle)

        mainPagerAdapter.addFragment(
            MainPagerAdapter.FragmentItem(
                R.string.tasks_pending, R.drawable.baseline_assignment_late_24, PendingFragment::class
            )
        )

        mainPagerAdapter.addFragment(
            MainPagerAdapter.FragmentItem(
                R.string.tasks_completed, R.drawable.baseline_assignment_turned_in_24, CompletedFragment::class
            )
        )

        mainPagerAdapter.addFragment(
            MainPagerAdapter.FragmentItem(
                R.string.news, R.drawable.baseline_wysiwyg_24, NewsFragment::class
            )
        )
        viewPager2.adapter = mainPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(mainPagerAdapter.fragmentItems[position].titleRes)
            tab.setIcon(mainPagerAdapter.fragmentItems[position].iconRes)
        }.attach()

        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.title) {
                getString(R.string.tasks_pending) -> viewPager2.setCurrentItem(0, true)
                getString(R.string.tasks_completed) -> viewPager2.setCurrentItem(1, true)
                getString(R.string.news) -> viewPager2.setCurrentItem(2, true)
            }
            navDrawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true

        }


        navView.menu.setGroupDividerEnabled(true)
        var newNavMenuIndex = 0

        mainPagerAdapter.fragmentItems.withIndex().forEach { (index, fragmentItem) ->
            newNavMenuIndex++
            navView.menu
                .add(0, index, index, fragmentItem.titleRes)
                .setIcon(fragmentItem.iconRes)
                .setCheckable(true)
                .setChecked((index == 0))
                .setOnMenuItemClickListener {
                    viewPager2.setCurrentItem(index, true)
                    navDrawerLayout.closeDrawers()
                    return@setOnMenuItemClickListener true
                }
        }
        newNavMenuIndex++

        navView.menu.add(
            newNavMenuIndex,
            0,
            newNavMenuIndex,
            getString(R.string.sync_wear_os)
            )
            .setIcon(R.drawable.baseline_watch_24)
            .setOnMenuItemClickListener {
                WearableSynchronizer.sendTasks(
                    TasksDatabase
                        .getInstance()
                        .getTasksDao()
                        .getAllTasks(false),
                    dataClient
                )
                return@setOnMenuItemClickListener true
            }




        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position : Int) {
                navView.menu.getItem(position).isChecked = true
            }
        })

        TasksDatabase.buildInstance(applicationContext)
        MockDataLoader.loadMockData()

        val channel = NotificationChannel("task-timer", "Task Timer Channel",
            NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        activateTaskDeletionService()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun activateTaskDeletionService() {
        taskDeletionServiceHelper.activateTaskDeletionService {  deletedTaskId ->
            supportFragmentManager.setFragmentResult(
                "task_deleted",
                        bundleOf("task_id" to deletedTaskId)
            )
        }
        /*
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, TaskDeletionService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 2 * 60 * 1000,
            2 * 60 * 1000,
            pendingIntent
        )*/
    }

    override fun onDestroy() {
        taskDeletionServiceHelper.deactivateTaskDeletionService()
        super.onDestroy()
    }
}