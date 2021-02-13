package com.example.psyquea.informacion

import android.webkit.JavascriptInterface
import com.example.psyquea.CallActivity

class JavascriptInterface (val callActivity: CallActivity){

    @JavascriptInterface
    public fun onPeerConnected(){
        callActivity.onPeerConnected()
    }

}