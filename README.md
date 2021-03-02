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
- 세부기능  
  - 회원 관리  
    - 회원가입  
    - 로그인
  - 친구 관리  
    - 친구 검색  
    - 친구 찾기, 추가  
    - 1:1 채팅하기  
  - 채팅 관리  
    - 실시간 채팅  
    - 채팅방 검색, 추가    
    - 채팅방 미리보기  
    - 채팅방 참여 인원 표시  
    - 채팅방 나가기
  - 계정 관리  
    - 이름, email 변경  
  - 알림 관리  
    - 실시간 메세지 도착 알림  
    - Notification 클릭 시 해당 채팅방 이동  
    
## 개발 환경
### 사용 프레임워크  
> Android Studio(4.1.1), node.js(12.16.1), AWS Amplify, Firebase Cloud Messaging  

### 사용 라이브러리
> Navigation Jetpack(2.3.2), Dagger2-Hilt(2.28-alpha), Retrofit2(2.9.0), Moshi(1.11.0), rxJava(3.0.9)
> Socket.io(1.0.0), Amplify(1.16.3), Glide(4.11.0), Firebase-bom(26.5.0), WorkManager(2.5.0)
> Node.js(Express), Node.js(mysql), Node.js(socket.io)

