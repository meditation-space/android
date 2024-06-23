import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.meditation_space.ActivityCollector
import com.example.meditation_space.WarningDialog

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ActivityCollector.addActivity(this)
    }

    override fun onStart() {
        super.onStart()
        var count = ActivityCollector.getActivityCount()
        ActivityCollector.setActivityCount(count + 1)
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCollector.getIsViolation() == true) {
            val warningDialog = WarningDialog(
                this,
                "收集失败",
                "由于切换界面或将程序切至后台而导致雨水收集失败。",
                {}
            )
            warningDialog.show()
            ActivityCollector.setIsViolation(false)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("BaseActivity","onPause")
    }
    override fun onStop() {
        super.onStop()
        var count = ActivityCollector.getActivityCount()
        ActivityCollector.setActivityCount(count - 1)
        //专注训练时，判断是否进入后台
        if (count - 1 == 0 && ActivityCollector.getIsCollectRainDrop()) {
            Log.d("BaseActivity", "该应用已经进入后台！！")
            ActivityCollector.setIsViolation(true)
            val intent = Intent("com.example.wyr_timegarden.NUBERDOWNCLOSE")
            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

}