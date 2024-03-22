package FCM

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.deliboard.MainActivity
import com.example.deliboard.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessaging


class MessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FirebaseMessagingService"
    }

    // 이 디바이스에 새 토큰을 생성할때 호출
    // 토큰은 앱이 재설치되거나 데이터가 날아가면 다시 생성된다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(TAG, "NewToken: $token")

        // 토큰을 SharedPreferences에 저장
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()
        Log.i(TAG, "토큰 저장됨")
    }

    // 메세지를 받을때 호출
    // Notification 을 구현해준다.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "Message: ${remoteMessage.data}" )
        Log.d(TAG, "MsgNotification: ${remoteMessage.notification}" )

        if (remoteMessage.data.isEmpty()) {
            Log.e(TAG, "empty message")
        } else {
            sendNotification(remoteMessage, applicationContext)
        }
    }

    // notification 만드는 함수
    private fun sendNotification(remoteMessage: RemoteMessage, context: Context) {
        val channelId = "cId"
        val channelName = "cName"

        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        val intent = Intent(context, MainActivity::class.java) // 알림을 클릭할 때 실행할 Activity 지정
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context, uniId, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)
//        FLAG_CANCEL_CURRENT → 이전에 생성한 PendingIntent 취소 후 새로 생성
//        FLAG_NO_CREATE → 이미 생성된 PendingIntent 가 있다면 재사용 (없으면 Null 리턴)
//        FLAG_ONE_SHOT → 해당 PendingIntent를 일회성으로 사용
//        FLAG_UPDATE_CURRENT → 이미 생성된 PendingIntent 가 있다면, Extra Data 만 업데이트

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.search) // 알림 아이콘 설정
            .setContentTitle(remoteMessage.notification?.title) // 알림 제목 설정
            .setContentText(remoteMessage.notification?.body) // 알림 내용 설정
            .setAutoCancel(true) // 알림을 사용자가 터치하면 자동으로 알림이 사라지도록 설정
            .setSound(soundUri) // 알림 소리 설정
            .setContentIntent(pendingIntent)

        /**
        "notificationBuilder" 알림 생성시 여러가지 옵션을 이용해 커스텀 가능.
        setSmallIcon : 작은 아이콘 (필수)
        setContentTitle : 제목 (필수)
        setContentText : 내용 (필수)
        setColor : 알림내 앱 이름 색
        setWhen : 받은 시간 커스텀 ( 기본 시스템에서 제공합니다 )
        setShowWhen : 알림 수신 시간 ( default 값은 true, false시 숨길 수 있습니다 )
        setOnlyAlertOnce : 알림 1회 수신 ( 동일 아이디의 알림을 처음 받았을때만 알린다, 상태바에 알림이 잔존하면 무음 )
        setContentTitle : 제목
        setContentText : 내용
        setFullScreenIntent : 긴급 알림 ( 자세한 설명은 아래에서 설명합니다 )
        setTimeoutAfter : 알림 자동 사라지기 ( 지정한 시간 후 수신된 알림이 사라집니다 )
        setContentIntent : 알림 클릭시 이벤트 ( 지정하지 않으면 클릭했을때 아무 반응이 없고 setAutoCancel 또한 작동하지 않는다 )
        setLargeIcon : 큰 아이콘 ( mipmap 에 있는 아이콘이 아닌 drawable 폴더에 있는 아이콘을 사용해야 합니다. )
        setAutoCancel : 알림 클릭시 삭제 여부 ( true = 클릭시 삭제 , false = 클릭시 미삭제 )
        setPriority : 알림의 중요도를 설정 ( 중요도에 따라 head up 알림으로 설정할 수 있는데 자세한 내용은 밑에서 설명하겠습니다. )
        setVisibility : 잠금 화면내 알림 노출 여부
        Notification.VISIBILITY_PRIVATE : 알림의 기본 정보만 노출 (제목, 타이틀 등등)
        Notification.VISIBILITY_PUBLIC : 알림의 모든 정보 노출
        Notification.VISIBILITY_SECRET : 알림의 모든 정보 비노출
         */

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(uniId, notificationBuilder.build())
    }


    // 기기의 토큰을 가져오는 함수
    fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and handle the token as needed
            Log.d(TAG, "Token : $token")
        }
    }
}