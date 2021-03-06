package nl.tcilegnar.mybaseapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import nl.tcilegnar.mybaseapp.models.PermissionInfo
import nl.tcilegnar.mybaseapp.util.Network
import nl.tcilegnar.mybaseapp.util.PermissionRequester

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initFabClick()

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initFabClick() {
        fab.setOnClickListener { view ->
            PermissionRequester().requestPermissionsAndCallbackIfAllGranted(
                this, object : PermissionRequester.PermissionRequestListener {
                    override fun allPermissionsGranted() {
                        val currentWifiName = Network().currentWifiNetworkName
                        val hasConnection = Network().hasConnection()
                        Snackbar.make(
                            view, "Current network: $currentWifiName. Connection: $hasConnection", Snackbar.LENGTH_LONG
                        ).setAction("Action", null).show()
                    }

                    override fun notAllPermissionsGranted() {
                        Snackbar.make(
                            view, "notAllPermissionsGranted", Snackbar.LENGTH_LONG
                        ).setAction("Action", null).show()
                    }

                    override fun anyPermissionDeniedAndDoNotAskAgain() {
                        Snackbar.make(
                            view, "anyPermissionDeniedAndDoNotAskAgain", Snackbar.LENGTH_LONG
                        ).setAction("Action", null).show()
                    }
                }, PermissionInfo.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
