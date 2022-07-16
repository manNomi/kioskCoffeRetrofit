# 레트로핏을 이용한 커피 키오스크

### 스테이지어스 서버를 이용한 커피키오스크

### 레트로핏을 이용해서 스테이지어스 서버의 데이터를 가져옵니다 

### 가져온 데이터(Json)를 변환후 사용하는 방식 

### 데이터가 달라지더라도 돌아갈 수 있도록 만드는 것이 중요 


### 서비스의 노티피케이션을 이용해 장바구니 상태 출력 

### 노티를 누르면 액티비티가 새로 열리는 문제 발생 
-> 패딩 인텐트(노티 눌렀을때 이동할 곳 선택)- 눌릴때 까지 기다린다는 뜻 
  패딩인텐트를 버튼마다 다르게끔 설정 
  런치모드를 통해서 액티비티가 중복으로 되지 않도록 설정함으로써 해결 
  
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





  
