package c.m.jeparalanguage.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import c.m.jeparalanguage.R
import c.m.jeparalanguage.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
