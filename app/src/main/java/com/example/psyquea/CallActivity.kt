package com.example.psyquea

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.psyquea.informacion.JavascriptInterface
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_call.*
import java.util.*


class CallActivity : AppCompatActivity() {

    var username = ""
    var friendsUsername = ""

    var isPeerConnected = false

    var database = FirebaseDatabase.getInstance()
    var myref = database.getReference("VideoCall")

    var isAudio = true
    var isVideo = true

    var uniqueId = ""

    var userID = ""
    var friendID = ""

    //var firebaseRef = Firebase.database.getReference()
    val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
    val requestCode = 1

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        if(!isPermissionGranted()){
            askPermissions()
        }

        username = intent.getStringExtra("username")!!
        userID = intent.getStringExtra("userID")!!

        friendsUsername = intent.getStringExtra("friendUser")!!
        friendID = intent.getStringExtra("friendID")!!

        Toast.makeText(this, username, Toast.LENGTH_LONG)
        Toast.makeText(this, friendsUsername, Toast.LENGTH_LONG)

        toggleAudioBtn.setOnClickListener {
            isAudio = !isAudio
            callJavascriptFunction("javascript:toggleAudio(\"${isAudio}\")")
            toggleAudioBtn.setImageResource(if (isAudio) R.drawable.ic_mic else R.drawable.ic_mic_off)

        }
        toggleVideoBtn.setOnClickListener {
            isVideo = !isVideo
            callJavascriptFunction("javascript:toggleVideo(\"${isVideo}\")")
            toggleVideoBtn.setImageResource(if (isVideo) R.drawable.ic_videochat else R.drawable.ic_videocam_off)
        }

        cerrarBtn.setOnClickListener {
            finish()
        }

        webView.webChromeClient = object: WebChromeClient(){
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPermissionRequest(request: PermissionRequest?) {
                //super.onPermissionRequest(request)
                if (request != null) {
                    request.grant(request.resources)
                }
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.addJavascriptInterface(JavascriptInterface(this), "Android")

        val filePath = "file:android_asset/Call.html"
        webView.loadUrl(filePath)

        webView.webViewClient = object: WebViewClient(){
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView?, url: String?) {
                callJavascriptFunction("javascript:init(\"${userID}\")")
                switchControls()
                callJavascriptFunction("javascript:startCall(\"${friendID}\")")
            }
        }
    }

    private fun askPermissions() {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    private fun isPermissionGranted(): Boolean {

        permissions.forEach {
            if(ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED )
                return false
        }
        return true
    }

    private fun sendCallRequest() {
        if(!isPeerConnected){
            Toast.makeText(this, "No estas conectado a internet :(", Toast.LENGTH_LONG)
            return
        }

        myref.child(friendsUsername).child("incoming").setValue(username)
        myref.child(friendsUsername).child("isAvaible").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) { }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value.toString() == "true"){
                    listenForConnId()
                }
            }

        })
    }

    private fun listenForConnId() {
        myref.child(friendsUsername).child("connId").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) { }

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value == null){
                    return
                }

                switchControls()
                callJavascriptFunction("javascript:startCall(\"${snapshot.value}\")")
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setupWebView() {
        webView.webChromeClient = object: WebChromeClient(){
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPermissionRequest(request: PermissionRequest?) {
                //super.onPermissionRequest(request)
                if (request != null) {
                    request.grant(request.resources)
                }
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.addJavascriptInterface(JavascriptInterface(this), "Android")

        loadVideoCall()
    }

    private fun loadVideoCall() {
        val filePath = "file:android_asset/Call.html"
        webView.loadUrl(filePath)

        webView.webViewClient = object: WebViewClient(){
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView?, url: String?) {
                initializePeer()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun initializePeer() {

        uniqueId = getUniqueID()

        callJavascriptFunction("javascript:init(\"${uniqueId}\")")
        myref.child(username).child("incoming").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) { }

            override fun onDataChange(snapshot: DataSnapshot) {

                onCallRequest(snapshot.value as? String)

            }

        })
    }

    private fun onCallRequest(caller: String?) {

        if (caller == null) return
        callLayout.visibility = View.VISIBLE
        incomingCallTxt.text = "$caller esta llamando..."

        acceptBtn.setOnClickListener {
            myref.child(username).child("connId").setValue(uniqueId)
            myref.child(username).child("isAvaible").setValue(true)

            callLayout.visibility = View.GONE
            switchControls()
        }

        rejectBtn.setOnClickListener {
            myref.child(username).child("incoming").setValue(null)
            callLayout.visibility = View.GONE
        }

    }

    private fun switchControls() {
        inputLayout.visibility = View.GONE
        callControlLayout.visibility = View.VISIBLE

    }

    private fun getUniqueID():String{
        return UUID.randomUUID().toString()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun callJavascriptFunction(functionString: String){
        webView.post{ webView.evaluateJavascript(functionString, null)}

    }

    public fun onPeerConnected() {
        isPeerConnected = true
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {

        myref.child(username).setValue(null)
        webView.loadUrl("about:blank")

        super.onDestroy()
    }


}