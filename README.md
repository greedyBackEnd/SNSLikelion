
# 멋사SNS V1🚀
테킷 백엔드 스쿨 5기 프로젝트 2
<br>
<br>

## 🛠 프로젝트 환경
- Java 17
- Spring Boot 3.1.2
- SQLite
<br>
<br>

## 🎯 프로젝트 목표
- 이 프로젝트를 통해 유저는 여러 이미지를 포함한 피드를 작성할 수 있으며, 유저 간의 팔로우 및 친구 관계를 맺어 활발한 SNS를 이용할 수 있는 것을 목표로합니다.
<br>
<br>

## 💡 기능 요약
- 유저는 이미지를 포함한 피드를 작성하고 관리할 수 있습니다.
- 유저는 다른 유저의 피드에 좋아요를 남기고, 댓글을 작성할 수 있습니다.
- 유저는 다른 유저를 팔로우할 수 있으며, 팔로우 중인 유저의 피드를 별도로 조회할 수 있습니다.
- 유저는 다른 유저와 친구 관계를 맺을 수 있으며, 친구 관계 유저의 피드를 별도로 조회할 수 있습니다.
<br>
<br>

## 🛠 ERD
![ERD](https://github.com/likelion-backend-5th/Project_2_KimDoyoung/blob/main/docs/ERD.png)   


## 📘 기능 요약

1. **유저 User**
    - 회원가입 POST /auth/signup : 유저는 회원가입을 할 수 있습니다. 이 때 아이디와 비밀번호는 필수 요소입니다. ( +전화번호, 이메일, 주소)
    - 로그인 POST /auth/login : 아이디와 비밀번호 입력 시, 토큰 정보를 반환받을 수 있습니다.
    - 로그아웃 GET /auth/logout
    - 프로필 조회 GET /users/profile (토큰) : 현재 로그인 된 유저의 프로필을 조회합니다.
    - 프로필 수정 PUT /users/profile (토큰) : 유저의 정보를 수정합니다. (비밀번호, 전화번호, 이메일, 주소)
    - 프로필 이미지 업로드 POST /users/profile (토큰) : 유저의 프로필 이미지를 업로드 및 수정합니다.
    - 팔로우 POST /users/follow/{userId} (토큰) : 다른 유저를 팔로우합니다.
    - 언팔로우 POST /uesr/unfollow/{userId} (토큰) : 다른 유저와의 팔로우를 끊을 수 있습니다.
    - 팔로우 목록 조회 GET /users/followers (토큰) : 현재 팔로우 중인 유저의 목록을 조회합니다.
    - 친구 요청 POST /users/friend/request/{userId} (토큰) : 다른 유저에게 친구 요청을 보냅니다.
    - 친구 요청 수락 POST /users/friend/accept/{requestId} (토큰) : 요청된 친구 요청을 수락합니다.
    - 친구 요청 거절 POST /users/friend/reject/{requestId} (토큰) : 요청된 친구 요청을 거절합니다.
    - 친구 요청 목록 조회 GET /users/friend-request (토큰) : 요청된 친구 요청 목록을 조회합니다.
    - ※ 팔로우는 일방향으로 진행될 수 있으며, 친구 관계는 요청이 수락되는 순간 서로 친구 관계가 맺어집니다.


<br>

2. **피드 Feed**
    - 피드 작성 POST /feeds (토큰): 피드를 작성합니다. 여러 이미지를 첨부할 수 있으며, 첨부된 이미지가 없을 경우 기본 이미지(static/images/defaultImg/defaultFeedImg.jpg)가 등록됩니다. 첨부된 이미지가 있을 경우 다음과 같은 경로에 이미지가 저장됩니다. (static/images/feedImg/{username}/{feedId}/{yyyy_MM_dd_mm_ss_UUID_filename.png}
    - 피드 임시 저장 POST /feeds/draft (토큰) : 피드를 임시 저장합니다. 임시 저장된 피드 또한 마찬가지로 기본 이미지 또는 첨부된 이미지를 가질 수 있습니다. 임시 저장된 피드는 다른 유저에게 조회되지 않습니다.
    - 피드 상세 조회 GET /feeds/{feedId} : 해당 피드를 조회합니다. 다음과 같은 내용을 포함합니다. (작성자, 제목, 내용, List<이미지>, List<댓글>, 좋아요 수)
    - 특정 유저 피드 목록 조회 GET /feeds/user/{username} : 해당 유저의 피드 목록을 조회합니다. 모든 피드 목록 조회할 때, 해당 피드의 사진이 여러 장일 경우, 가장 먼저 첨부된 대표 이미지로 표시됩니다.
    - 임시 저장된 피드 목록 조회 GET /feeds/draft (토큰) : 유저의 임시 저장된 피드의 목록을 조회합니다.
    - 팔로잉 유저 피드 목록 조회 GET /feeds/followed (토큰) : 현재 팔로우 중인 유저들의 피드의 목록을 조회합니다. 작성한 사용자와 무관하게 작성된 순서의 역순으로 조회됩니다.
    - 친구 피드 목록 조회 GET /feeds/friends (토큰) : 현재 친구 관계에 있는 유저들의 피드의 목록을 조회합니다. 마찬가지로 작성한 사용자와 무관하게 작성된 순서의 역순으로 조회됩니다.
    - 피드 수정 PUT /feeds/{feedId} (토큰) : 해당 피드의 제목 및 내용을 수정합니다. 기존 피드의 이미지가 기본 이미지일 경우, 새롭게 첨부된 이미지로 대체됩니다.
    - 피드 삭제 DEL /feeds/{feedId} (토큰) : 해당 피드를 삭제합니다.
<br>


3. **댓글 Comment**
    - 댓글 작성 POST /feeds/{feedId}/comments (토큰) : 피드에 댓글을 작성합니다. 해당 댓글은 피드 상세 조회에서 확인할 수 있습니다.
    - 댓글 수정 PUT /feeds/{feedId}/comments/{commentId} (토큰) : 해당 댓글 내용을 수정합니다.
    - 댓글 삭제 DEL /feeds/{feedId}/comments/{commentId} (토큰) : 해당 댓글을 삭제합니다.
<br>

4. **피드 좋아요 likes**
    - 피드 좋아요 POST /feeds/{feedId}/like (토큰) : 해당 피드에 좋아요를 추가할 수 있습니다. 처음 요청 시, 해당 피드의 좋아요 수가 올라갑니다. 같은 요청을 한 번 더 요청하게되면, 해당 좋아요가 취소됩니다. 이러한 좋아요 수는 피드 상세 조회 시 확인할 수 있습니다.
<br>

## ※ 주의 사항
- 모든 요청 경로의 앞에는 다음과 같은 URL이 붙습니다. -> **/api/v1/~**
- 전반적인 실행을 위해 우선 더미 데이터 등록이 필요합니다.
<br>
<br>

### Postman JSON
[postman_collection_json](https://github.com/likelion-backend-5th/Project_2_KimDoyoung/blob/main/docs/sns_likelion.postman_collection.json)   

<br>
<br>

