# 레트로핏을 이용한 커피 키오스크

### 스테이지어스 서버를 이용한 커피키오스크

### 레트로핏을 이용해서 스테이지어스 서버의 데이터를 가져옵니다 

### 코딩하면서 유의 한것 
  - 가져온 데이터(Json)를 변환후 사용하는 방식 
  - 데이터가 달라지더라도 돌아갈 수 있도록 만드는 것이 중요 
  - 서비스의 노티피케이션을 이용해 장바구니 상태 출력 
  - 노티를 누르면 액티비티가 새로 열리는 문제 발생 
  -> 패딩 인텐트(노티 눌렀을때 이동할 곳 선택 / 눌릴때 까지 기다린다는 뜻 )
    - 패딩인텐트를 버튼마다 다르게끔 설정 
    - 런치모드를 통해서 액티비티가 중복으로 되지 않도록 설정함으로써 해결 
  
### 메뉴를 오브젝트로 설정 
  object- 오브젝트는 클래스와 비슷하지만 다름 
        - 클래스는 증복으로 만들경우 새롭게 만들지만 
        - 오브젝트의 경우 무조건 1개만 있게됨 
        - 메뉴의 경우 달라질 수 없기 때문에 오브젝트로 설정 

### 장바구니를 클래스화 
  - 장바구니를 클래스화 해서 액티비티간 통신에서 데이터를 클래스로 주고받음 
  - 클래스로 주고받으면 이름을 인덱스로 관리 안해도 되서 용이 
  - 클래스를 액티비티 또는 프라그먼트 간 주고 받기 위해서는 Serializable 을 설정해줘야함
  - 클래스 내의 데이터 클래스의 경우도 마찬가지고 Serializable를 설정해줘야만 한다 
  - intent로 보내줄때는 putExtra - getSerializable 같은 방식 사용 
  - 받는 곳에서는 val cart = getSerializable("name") as CartClass 
  - 같은 방식으로 as 를 활용해야함 
  - as는 카트클래스로 취급하는데 아닐경우 성립안한다는 뜻 
  - as를 써줘야만 cartClass에 있는 함수등을 사용 할 수 있음 
  
### 레트로핏 
  - 기본적으로 레트로핏은 비동기 함수이다 
  *비동기함수란)
  - 리턴값을 기다리지 않아도 되는 함수 
  - 즉 순서대로 가는것이 아님 
  - 비동기 함수가 있는 이유 
  - 서버로부터 데이터를 받아오는 경우 데이터를 받아오는 코드는 최상단에 위치
  - 비동기적 처리를 하지 않고 동기적 처리를 하면 / 서버로부터 데이터를 받아오는 코드의 실행이 완전히 끝나야 코드를 처리하게 됨 
  - 같은 문제가 생김
  - 예시로 있는것이 콜백함수 

#### *빌드 그래들 앱에 sync하자
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
   
  EX)
  ```kotlin
  @POST("/order")
    fun postOrder(
        @Body body:HashMap<String,Any>
    ):Call<InputHistory>
}
data class InputHistory(
    var message:String ,
    var success:Boolean
)
```

#### POST - 데이터를 올리겠다는 뜻 
  - 포스트 옆의 "/order"은 이름이라고 보면 된다 (백엔드의)
  - 포스트 할때는 바디를 사용한다 
  - 해쉬맵의 경우 HashMap<타입,타입>으로 쓰이며 왼쪽이 Key 오른쪽이 Value이다 
  - 아래 데이터 클래스가 보내고 받아오는 것이다 
  
  

```kotlin
@GET("/category/menu")
    fun getAccountMenu(
        @Query("lang") lang:String,
        @Query("category_name") category_name:String
    ):Call<MenuData>
    
data class MenuData(
    var message: String,
    var success: Boolean,
    var data: JsonArray
)
```

#### GET - 데이터를 가져오는 것 
  - "/category/menu"- 이름 
  - get에서는 쿼리를 통해 가져옴 
  - 쿼리 옆 괄호는 가져올 데이터의 Key
  - 아래 데이터 클래스는 가져오는 타입임 
  - 
 
```kotlin
lateinit var retrofit: Retrofit
lateinit var retrofitHttp: RetrofitService
fun initRetrofit() {
        retrofit= RetrofitClient.initRetrofit()
        retrofitHttp=retrofit!!.create(RetrofitService::class.java)
    }
```

- 레트로핏을 사용하기전 레트로핏을 정의해주는 함수와 전역변수 (문법) - 즉 외우자 

### Json 사용법 

```kotlin
data class MenuListData(
        var menu_name: String,
        var menu_price: Int,
        var menu_image: String
    )
    
val gson = Gson()
val menu = gson.fromJson(response.body()!!.data[index], MenuListData::class.java)
```

  - 위는 레트로핏을 통해 받은 데이터를 반복문을 통해 모든 데이터를 gSon으로 바꿔주는 것이다 
  - 데이터 클래스에 있는 변수명이 Key가 된다 

```kotlin
data class MenuListData(
        var menu_name: String,
        var menu_price: Int,
        var menu_image: String
    )
    
data class reciptData(
        val name:String,
        val count:Int,
        val sum_price:Int
    )
    
val gson= GsonBuilder()
            .setPrettyPrinting()
            .create()
            
val recipt=reciptData('데이터')
val myAccountData=gson.toJson(recipt)
```

  - Gson 객체 선언 후 데이터 클래스 이용 
  - 데이터 클래스의 변수명이 Key 
  - 데이터 클래스안에 데이터를 넣고 
  - to Json 함수를 통해서 바꿔준다 


### diaglo 사용 법

```kotlin
//커스텀 다이얼로그 - 컨센트레이트 안먹음

// 다이얼로그 프라그먼트 (Dialog.kt)
class dialog() : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val bundle = arguments
            val message = bundle!!.getString("text")
            val builder = AlertDialog.Builder(it)
            val view=inflater.inflate(R.layout.dialog,null)
            builder.setView(inflater.inflate(R.layout.dialog, null))
            builder.apply {
                setTitle("주문기록")
                setMessage(message)
                setNegativeButton("확인",
                    DialogInterface.OnClickListener { _, _ ->
                        Log.d("qwe","123")
                    }
                )
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

// 액티비티 (MainActivity.kt)
fun showDialog(text:String) {
        var newFragment= dialog()
        val bundle = Bundle()
        bundle.putString("text",text)
        newFragment.arguments = bundle
        val fragmentManager=supportFragmentManager
        newFragment.show(fragmentManager, "dialog")
    }
```
  - 위 클래스는 다이얼로그를 만드는 클래스임 
  - 다이얼로그도 프라그먼트와 마찬가지로 이뤄짐 
  - 다이얼로그와 주고 받을 때에는 번들사용 
  - show 함수를 통해 다이얼로그창 띄움 
  - *커스텀 다이얼로그에 대한 공부가 좀더 필요 + 다이얼로그 버튼 클릭 부분에 대한 부분도 공부가 좀더 필요해보임


### ASSETS (외부저장소) 사용법 
- 위 파일에는 없지만 아웃백 키오스크를 만들때 json 파일을 가져오면서 사용 

#### assets 파일 생성 - app 에 생성하면 됨 

  - assets 파일에 불러올 파일들 저장하면 된다 
  - 드래그후 리팩터 해줘도 됨 

#### assets 파일 오픈

```kotlin
val jsonString = assets.open("data.json").reader().readText()
```
- json 파일을 열때에는 위와같이 해주면 jsonString을 받아올 수 있다 

```kotlin
picture=mutableListOf<Bitmap>()
val image = assets.open("image.png")
var bitmap: Bitmap = BitmapFactory.decodeStream(image)
picture.add(bitmap)
```
- image 파일을 열려면 비트맵을 활용해야함 
- 비트맵을 통해 디코드 하는 과정이 필요 
- 위 과정은 문법이므로 외우도록 / pixmap 과의 차이점 생각하기 
- *pixmap - 픽셀로 이루어진 맵 

### 서비스(service)
  - 서비스는 쓰레드와 비슷한 개념 

#### 서비스를 만들때 쓰는 함수 (MainActivity.kt)
  - 서비스를 연결하기 위해서 만들어 놓는 함수라고 보면 된다 
```kotlin 
var connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as MyService.MyBinder
            myService = binder.getService()
            isService = true
        }
        override fun onServiceDisconnected(className: ComponentName?) {
            isService = false
        }
    }
    
```

#### (MyService.kt) - 서비스가 연결될경우 실행되는 함수 
```kotlin
override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
```

#### 바인드 서비스 
  - 한번만 연결하면 되는 함수 
  - 한번 연결하면 지속적으로 연결이 되있어서 증복으로 실행해도 실행되지 않는 특징이 있다 
  - 서비스간 데이터를 주고 받을때 사용 
  
  ##### 바인드 서비스 연결 함수 
    - putExtra 함수를 이용해서 바인드 서비스에 정보 전달 가능 
    
   #### (MainActivity.kt)
```kotlin
fun serviceBind()
    {
        intentService= Intent(this, MyService::class.java)
        intentService.putExtra("DATA",cartClass)
        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
    }

    fun serviceUnBind()
    {
        if (isService) {
            unbindService(connection)
            isService = false
        }
    }
```
#### (MyService.kt)
  - bindServiceReturn 함수의 경우 임의로 지정한 함수로서 데이터를 주고받기 위한 함수 

```kotlin
inner class MyBinder : Binder() {
        fun getService(): MyService = this@MyService
    }
    override fun onBind(intent: Intent): IBinder {
        if (intent.getSerializableExtra("DATA")as CartClass? !=null){
            cartClass=intent.getSerializableExtra("DATA")as CartClass
            totalCal=cartClass.totalCal().toString()
        }

        return iBinder
    }
    fun bindServiceReturn(): CartClass {
        return cartClass
    }
```

#### 노티피케이션 - 포그라운드 서비스 
  
  - 포그라운드 서비스는 서비스 중에서도 눈 앞으로 보이는 visible 부분을 바탕으로 함 
  - 포그라운드는 시작시 onStartCommand 로 값을 보낼 수 있음


##### (MainActivity.kt) -포그라운드 서비스 시작 
```kotlin
ContextCompat.startForegroundService(this, intentService)
```

##### (MyService.kt)  - 포그라운드 서비스 종료 
```kotlin 
private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }
    
```

##### (MyService.kt)  - 노티피케이션 채널 설정 
            - 안드로이드 오레오(8.0) 26 이상 부터는 노티피케이션의 채널을 설정해야만 함 
            - 아래 함수는 채널을 설정하는 함수 
```kotlin
private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            Companion.CHANNEL_ID,
            "MyApp notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "AppApp Tests"

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            notificationChannel)
    }
```

#### 노티피케이션 만드는 함수 
  - 여기서 포인트가 padingIntent를 통해 노티 알람을 눌렀을때 돌아가는 activity를 설정하는 것이다 
  - padingIntent는 위에서 설명했으니 생략 
  - 채널을 먼저 만들고 연결해줘야 함을 유의하자 

```kotlin
fun Notification() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var intentMainLanding:Intent
        if (type=="main") {
            intentMainLanding = Intent(this, MainActivity::class.java)
        }
        else{
            intentMainLanding = Intent(this, CartActivity::class.java)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intentMainLanding, 0)
        createNotificationChannel()
        if (notiManager == null) {
            notiManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setContentTitle(
            StringBuilder(resources.getString(R.string.app_name)+" 총 금액은 : ${totalCal}")
        )
            .setTicker(
                StringBuilder(("실행중"))
            )
            .setSmallIcon(R.mipmap.outback_logo)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
        noti = builder.build()
        startForeground(10, noti)
    }
}
```


#### splash - 로딩화면 
  - splash의 경우 나중에 서비스로 구현하는 것이 좋음
  - 현재의 방식은 무조건 2 초간 띄우는 방식인데 비효율적이고 로딩화면의 의미가 퇴색됨 
  - DURATION 이 시간을 의미 (이시간동안 기다렸다가 실행한다 라는 뜻)
  - intent 를 통해 다음 페이지를 설정 

##### (StartAcitivity.kt)
```kotlin 
class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_page_activity)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, DURATION)
    }
    companion object {
        private const val DURATION : Long = 2000
    }
    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
```

#### 뒤로가기 버튼 
  - 위 앱은 액티비티를 쌓지 않는 방식이기에 뒤로가기 버튼을 막는다 
  - 핸들러를 이용해서 만들었다 
 
  - 뒤로가기 버튼을 누르면 토스트로 "한번더 누르시면 종료합니다" 를 출력 
  - 뒤로가기를 누른뒤 1.5초 내에 다시 클릭하면 종료 되도록 설정했다 
  - 함수는 다음과 같다 
  - 땜빵의 느낌이 강하지만 해결한 모습
  
   * 핸들러의 경우 작은 쓰레드의 개념으로 생각해도 괜찮다 
   - (핸들러란, worker thread 에서 main thread 로 메시지를 전달해주는 역할을 하는 클래스이다.)

```kotlin
//    <뒤로가기 설정
    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }
    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }
//    뒤로가기 설정 >
```

