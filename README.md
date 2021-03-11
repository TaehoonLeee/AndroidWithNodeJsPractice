# 채팅 어플리케이션
> 직접 nodejs express를 이용해 서버를 개설해 데이터베이스를 관리, 여러 라이브러리를 통해 안드로이드 채팅 어플리케이션 제작하는 1인 토이 프로젝트  
- AWS Amplify를 이용한 인증된 회원관리  
- Socket.io를 통한 실시간 채팅  
- http 통신을 통한 database관리  
- fcm을 이용한 메세지 도착 알림  

## 목차  
- [개발 환경](#개발-환경)  
  - [사용 프레임워크](#사용-프레임워크)  
  - [사용 라이브러리](#사용-라이브러리)  
- [세부기능](#세부-기능)  
  - [회원 관리](#회원-관리)  
    - [회원가입](#회원가입)  
    - [로그인](#로그인)
  - [친구 관리](#친구-관리)  
    - [친구 검색](#친구-검색)  
    - [친구 찾기, 추가](#친구-찾기\,-추가)  
    - [1:1 채팅하기](#1\:1-채팅하기)  
  - [채팅 관리](#채팅-관리)  
    - [실시간 채팅](#실시간-채팅)  
    - 채팅방 검색, 추가    
    - 채팅방 미리보기  
    - 채팅방 참여 인원 표시  
    - 채팅방 나가기
  - 계정 관리  
    - 이름, email 변경  
  - [알림 관리](#알림-관리)  
    - [실시간 메세지 도착 알림](#실시간-메세지-도착-알림)  
    - [Notification 클릭 시 해당 채팅방 이동](#Notification-클릭-시-해당-채팅방-이동)  
    
## 개발 환경
### 사용 프레임워크  
> Android Studio(4.1.1), node.js(12.16.1), AWS Amplify, Firebase Cloud Messaging  

### 사용 라이브러리
> Navigation Jetpack(2.3.2), Dagger2-Hilt(2.28-alpha), Retrofit2(2.9.0), Moshi(1.11.0), rxJava(3.0.9)
> Socket.io(1.0.0), Amplify(1.16.3), Glide(4.11.0), Firebase-bom(26.5.0), WorkManager(2.5.0)
> Node.js(Express), Node.js(mysql), Node.js(socket.io)  
  
## 세부 기능  
### 회원 관리  
#### 회원가입  
![스크린샷 2021-03-11 오후 11 48 42](https://user-images.githubusercontent.com/48707020/110806580-8cf58180-82c5-11eb-8cc2-59f678f317de.png)  
- AWS Amplify Authentication을 통하여 메일을 통해 인증을 거쳐 회원가입을 한다.  
#### 로그인  
- AWS Amplify Authentication을 통해 아이디와 비밀번호를 확인한 후 로그인을 한다.  

### 친구 관리  
#### 친구 검색  
![search friends](https://user-images.githubusercontent.com/48707020/110806642-9aab0700-82c5-11eb-8fc3-3b0f1acadf6f.gif)  
  
#### 친구 찾기, 추가  
![add friend](https://user-images.githubusercontent.com/48707020/110806622-954dbc80-82c5-11eb-9bc3-ed7ebc8d8de7.gif)  
- 정규식을 이용하여 검색한 이름과 관련 있는 친구들의 목록을 나열한 후 추가하고 싶은 친구를 추가할 수 있다.  

#### 1:1 채팅하기
![private chat](https://user-images.githubusercontent.com/48707020/110807692-a64afd80-82c6-11eb-90a1-194b188e2b90.gif)  
  
### 채팅 관리
#### 실시간 채팅  
![realtime chat](https://user-images.githubusercontent.com/48707020/110809248-10b06d80-82c8-11eb-834e-7dd94fd65825.gif)  
- socket.io 통신을 이용한 실시간 채팅과 multer, cloudinary를 이용한 이미지 주고 받기  

### 알림 관리
#### 실시간 메세지 도착 알림
![noti](https://user-images.githubusercontent.com/48707020/110811887-76055e00-82ca-11eb-9f88-09689c379b0b.gif)  
- FCM을 통하여 실시간 메세지 도착 알림 구현  

#### Notification 클릭 시 해당 채팅방 이동  
![click noti](https://user-images.githubusercontent.com/48707020/110811362-fa0b1600-82c9-11eb-8c19-fd81b9b8275d.gif)
