package nl.tcilegnar.mybaseapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.tcilegnar.mybaseapp.broadcastreceivers.BroadcastReceiverRequestPermissions.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val extras = Bundle()
        extras.putInt(REQUEST_CODE, requestCode)
        extras.putStringArray(PERMISSIONS, permissions)
        extras.putIntArray(GRANT_RESULTS, grantResults)

        val myIntent = Intent()
        myIntent.putExtras(extras)
        myIntent.action = ACTION_REQUEST_PERMISSION
        sendBroadcast(myIntent)
    }
}
