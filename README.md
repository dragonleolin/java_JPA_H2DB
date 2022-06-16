# Spring Simple Lab
***

## 使用技術


1. **[Spring Core](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html)**
2. **[Spring Web MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)**
3. **[Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/2.1.3.RELEASE/reference/html/)**
4. **[Spring AOP](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#aop)**
5. **[Spring Cache Caffeine](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html#boot-features-caching-provider-caffeine)**
6. Java 8
	+ **[Lambda](https://magiclen.org/java-8-lambda/)**
	+ **[Optional API](http://blog.tonycube.com/2015/10/java-java8-4-optional.html)**
	+ **[Stream API](https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/index.html)**
	+ **[CompletableFuture](https://popcornylu.gitbooks.io/java_multithread/content/async/cfuture.html)**
7. **[Spring Boot 2](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/)**
8. **[Spring Transaction](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/data-access.html)**
9. **[BCrypt](https://ithelp.ithome.com.tw/articles/10196477)**
10. **[Spring Security](https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/reference/htmlsingle/)**
11. **[JWT(JSON Web Token)](https://yami.io/jwt/)**
12. **[ECDSA](https://hk.saowen.com/a/34562609077f8b590a14b571c1cfa9412bf4277eda6f027d979a1e21bafd8e36)**
13. **[Spring Schedule](https://www.baeldung.com/spring-scheduled-tasks)**
14. **[Cron Expressions](https://www.baeldung.com/cron-expressions)**

***
## RESTful API 架構


![](https://i.imgur.com/fw4MI8D.png)
***

## 開發環境設定

+ 安裝 JAVA jdk 8
+ 安裝 Git
***

## 專案結構

### Package

+ **aop：** aop 相關實作
+ **auth：** 身份驗證相關實作
+ **bo：** 業務邏輯物件
+ **config：** Spring 相關配置
+ **constant：** 常數定義
+ **controller：** Controller 定義
+ **dao：** 資料存取物件
+ **dto：** 資料傳輸物件
+ **error：** 自定義例外
+ **factory：** 工廠物件
+ **po：** 持久物件（資料庫資料 mapping）
+ **scheduler：** 排程相關實作
+ **service：** 業務邏輯實作
+ **util：** 共用元件實作
+ **validator：** 資料檢核相關實作

***

## 快速啟動

### Step 1：從 Bitbucket 將專案 clone 下來

```shell
$ git clone <url>
```

### Step 2：使用 Eclipse 開啟專案

![](https://i.imgur.com/wSvgTAP.png)

![](https://i.imgur.com/kiamXLK.png)

### Step 3：Import 為 Gradle 專案

![](https://i.imgur.com/snu7y2a.png)

![](https://i.imgur.com/i01cKop.png)

### Step 4：執行 com.example.demo.DemoApplication.java

![](https://i.imgur.com/77yfS2A.png)

### Step 5：應用程式啟動後會自動啟動內嵌的 H2 資料庫

### Step 6：自動根據 com.example.demo.po 裡定義之物件內容初始化資料庫的資料表

### Step 7：自動根據 src/main/resources/data-h2.sql 裡定義之 sql 來建立初始資料

### Step 8：http://localhost:8080/h2 開啟 DB 控制台

![](https://i.imgur.com/96H5TVZ.png)

***

## 身份驗證機制

### 預設 http://localhost:8080/user 相關 API 皆需經過驗證才能使用。

### Step 1：透過 API http://localhost:8080/auth/login 進行登入，成功將回傳 Token

+ 預設帳號：admin
+ 預設密碼：admin

>**NOTE：** 密碼需先經過 RSA 加密，可透過 API http://localhost:8080/demo 進行加密

![](https://i.imgur.com/awaPFUL.png)

![](https://i.imgur.com/VU3YiLV.png)

### Step 2：使用登入取得的 Token 帶入 Http request header「Authorization」即可呼叫 user 相關 API

![](https://i.imgur.com/nWirdYX.png)


***

## 開始前動作


### Step 1：從 master 分支切出自己的開發分支

```shell
$ git checkout -b <your name> master
```

### Step 2：開始開發
***
## LAB 目標
***

### 1. 按照以下 API 規格進行開發

>**NOTE：** 不需要快取

#### 1-1. API：查詢所有書籍

##### 基本資訊

|                   | Content         |
| ----------------- | --------------- |
| **URL**           | /book           |
| **HTTP Method**   | GET             | 
| **Desc**          | 查詢所有的 book   |

##### 回覆（Response）訊息格式

| Column              | Type      | Remarks             |
| ------------------- | -------   | ------------------- |
| Repeating Structure |         |                     |
| name                | String    | 書名                 |
| author              | String    | 作者                 |
| publicationDate     | String    | 出版日期 yyyy-mm-dd   |

#### 1-2. API：以書名查詢書籍

##### 基本資訊

|                   | Content           |
| ----------------- | ----------------- |
| **URL**           | /book/{name}      |
| **HTTP Method**   | GET               | 
| **Desc**          | 以 name 撈取 book  |

##### 回覆（Response）訊息格式

| Column            | Type      | Remarks             |
| ----------------- | -------   | ------------------- |
| name              | String    | 書名                 |
| author            | String    | 作者                 |
| publicationDate   | String    | 出版日期 yyyy-mm-dd   |

#### 1-3. API：新增書籍

##### 基本資訊

|                   | Content                                                       |
| ----------------- | ------------------------------------------------------------- |
| **URL**           | /book                                                         |
| **HTTP Method**   | POST                                                          | 
| **Desc**          | 1. 將請求（Request）的書籍資料寫入 Table「BOOK」2. 回傳已新增的書籍資料 |

##### 請求（Request）訊息格式

| Column            | Type      | Remarks            |
| ----------------- | -------   | -------------------|
| name              | String    | 書名                |
| author            | String    | 作者                |
| publicationDate   | String    | 出版日期 yyyymmdd   |

##### 回覆（Response）訊息格式

| Column            | Type      | Remarks             |
| ----------------- | -------   | ------------------- |
| name              | String    | 書名                 |
| author            | String    | 作者                 |
| publicationDate   | String    | 出版日期 yyyy-mm-dd   |

#### 1-4. API：刪除書籍

##### 基本資訊

|                   | Content           |
| ----------------- | ----------------- |
| **URL**           | /book/{name}      |
| **HTTP Method**   | DELETE            | 
| **Desc**          | 以 name 來刪除書籍  |

#### 1-5. API：更新書籍

##### 基本資訊

|                   | Content                                                                                           |
| ----------------- | -----------------------------------------------------------------------------------------------   |
| **URL**           | /book                                                                                             |
| **HTTP Method**   | PUT                                                                                               | 
| **Desc**          | 1. 將請求（Request）的書籍資料更新對應書名的書籍資料 2. 若請求欄位為 null，則不更新此欄位 3. 回傳已更新的書籍資料   |

##### 請求（Request）訊息格式

| Column            | Type      | Remarks            |
| ----------------- | -------   | -------------------|
| name              | String    | 書名                |
| author            | String    | 作者                |
| publicationDate   | String    | 出版日期 yyyymmdd   |

##### 回覆（Response）訊息格式

| Column            | Type      | Remarks             |
| ----------------- | -------   | ------------------- |
| name              | String    | 書名                 |
| author            | String    | 作者                 |
| publicationDate   | String    | 出版日期 yyyy-mm-dd   |

***
### 2. 針對資源 Book 新增 Cache

#### 2-1. Cache 時間為 1 分鐘
#### 2-2. 查詢時若有Cache則回傳Cache，若沒Cache則重新查詢後更新Cache並回傳
#### 2-3. 變更時更新Cache
#### 2-4. 刪除時清除Cache
***

### 3. Transaction 控制

#### 3-1. 設計一API：同時新增使用者、書籍，並以 Transaction 控制若有任何例外發生，則 rollback
#### 3-2. 設計一API：同時更新使用者、書籍，並以 Transaction 控制若有任何例外發生也不影響已經執行的 DB 操作
***

### 4. AOP (Aspect-Oriented Programming)

>**NOTE：** 使用 AOP 的方式實作

#### 4-1. 在進到 Controller method 前印出「{method 名稱} 開始執行」、輸入的參數
#### 4-2. 在 Controller method 之後印出「{method 名稱} 執行結束」
#### 4-3. 印出執行 Controller method 所花費的時間
***

### 5. Security

>**NOTE：** 5-1 使用 Spring Security 實作

#### 5-1. 將 Book API 限制為角色「USER」才能存取
#### 5-2. 實作一個方法將一個字串做Base64編碼
```java
String stringToBase64String(String inputString)
```
#### 5-3. 實作一個方法將byte[]做Base64編碼
```java
String bytesToBase64String(byte[] bytes)
```
#### 5-4. 實作一個方法將Base64字串做Base64解碼，並印出byte[]的長度
```java
void decodeBase64String(String base64String)
```
#### 5-5. 調查並分享目前企業應用上常用的加解密有哪些種類、哪些演算法？
***

### 6. Scheduler

>**NOTE：** 使用 Spring Schedule 的方式實作

#### 6-1. 新增一排程每10秒鐘統計一次當前DB裡有多少本書，並在console log印出
***

### 7. Request Validate

>**NOTE：** 遵循專案架構並使用專案中的 @Validator 實作 request 檢核

#### 7-1. 除 API「更新書籍」外，其餘 API request 參數皆為必填
#### 7-2. 使用 @Validator 給定正規表示式來檢核 Book API request 中所有書名「只能是英數字」、「長度最大為 Table 定義之最大長度」
#### 7-3. 使用 @Validator 給定正規表示式來檢核 Book API request 中所有作者「只能是英文字」、「長度最大為 Table 定義之最大長度」
#### 7-4. 使用 @Validator 給定參數 validator 來檢核 Book API request 中所有出版日期「符合規格中定義之格式」
***

### 8. 修改 Book API：查詢所有書籍

#### 8-1. 修改為可帶「order」參數，值必須為「name」、「author」、「date」其中之一，非必填，例如：/book?order=name
#### 8-2. 根據參數 order 決定回傳的 book list 以哪個欄位進行排序
+ name：書名以「長->短」排序
+ author：作者以英文字母「A -> Z」排序
+ date：出版日期「新 -> 舊」排序

***

### 9. Lambda

#### 9-1. 說明 Lambda 的概念、使用方法
+ 優點
+ 使用條件
+ 舉例

***

### 10. Stream

#### 10-1. 將 Book API：查詢所有書籍，對集合的操作改為使用 Stream的方式

***

### 11. Git

#### 11-1. 使用 rebase 到最新的Master commit 
#### 11-2. 使用 cherry pick git_Sample 分支上的commit 32e27e4
#### 11-3. 使用 no fast-forward merge 將 git_Sample 合併到自己的Branch 並解決合併衝突
#### 11-4. 比較 fast-forward merge 跟 no fast-forward merge 的差異
#### 11-5. 將 完成的分支 push 上 bitbucket 

![](https://i.imgur.com/5QWaTy0.png)

***