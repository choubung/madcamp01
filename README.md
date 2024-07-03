# Dear.Prof
교수님께 고민없이 메일을 보내보세요!
<br/>
<br/>
<br/>

## Outline
<img width="30%" src="https://github.com/choubung/madcamp01/assets/112858914/fc497f0d-6999-4dae-b70e-6060d69310bb"/>

**Dear.Prof**는 교수님의 연락처를 저장할 수 있는 연락처탭, 사진을 업로드할 수 있는 갤러리탭, 교수님께 보낼 메일 양식을 용건에 맞게 제공하는 메일 양식탭으로 구성된 앱입니다.
<br/>
<br/>
<br/>

## Team
**곽지원** https://github.com/Kwak-Jiwon

**이수연** https://github.com/choubung
<br/>
<br/>
<br/>

## Tech Stack
**Front-end** : Java

**IDE** : Android Studio
<br/>
<br/>
<br/>

## About
**Intro**
- 앱을 처음 시작할 때 로고가 짧게 떠오릅니다.
- `Bottom Navigation Bar` 를 통해 각각의 탭으로 이동할 수 있습니다.
<img width="30%" src="https://github.com/choubung/madcamp01/assets/112858914/fcc08697-3599-4044-b799-9c8d2eb7e2d7"/>
<br/>
<br/>

**연락처탭**
- 교수님 연락처 목록을 `RecyclerView` 로 제공합니다.
- 연락처를 저장할 DB를 만들기 위해 `room` 라이브러리를 사용했습니다.
- 검색 기능
    - 이름으로 연락처를 검색할 수 있습니다.
<img width="30%" src="https://github.com/choubung/madcamp01/assets/112858914/6a0b4b94-8a95-45c7-81f8-06190fb2f97b"/>
<br/>
<br/>

- 연락처 추가 기능
    - `Floating Action Button` 을 누르면 연락처를 추가할 수 있습니다.
<img width="30%" src="https://github.com/choubung/madcamp01/assets/112858914/9fec9218-25a0-4b18-a2ae-fc9e7baa46d5"/>
<br/>
<br/>

- 삭제 기능
    - 연락처 아이템을 길게 누르면 떠오르는 `Dialog` 로 삭제 여부를 선택할 수 있습니다.
<img width="30%" src="https://github.com/choubung/madcamp01/assets/112858914/35392605-5f9a-4476-9a5f-f2d1816ed4bd"/>
<br/>
<br/>

- 상세정보창
    - 전화/메시지/메일 버튼을 누르면 해당 앱으로 연결됩니다.
<img width="30%" src="https://github.com/choubung/madcamp01/assets/112858914/9fec9218-25a0-4b18-a2ae-fc9e7baa46d5"/>
<br/>
<br/>


**갤러리탭**
- `Floating action button`에서 “사진 가져오기”, “사진 촬영하기”를 선택할 수 있습니다.
- “사진 가져오기” 선택 시, 갤러리의 사진을 `RecyclerView` 에 나타냅니다.
- “사진 촬영하기” 선택 시, 카메라를 통해 사진을 촬영하고, 이미지를 `RecyclerView`에 나타냅니다.
- 각 사진 클릭 시, 원본 사진을 볼 수 있습니다.
<img width="30%" src="https://github.com/choubung/madcamp01/assets/112858914/e4eef713-5323-4d37-a416-62f433b7c01d"/>
<br/>
<br/>

**메일 양식탭**
- `spinner` 로 용건을 선택하면 그에 맞는 메일 양식을 제공합니다.
- “받는 이”에 이름을 입력하면 연락처DB에서 메일 주소를 가져옵니다.
- 본인의 상황에 맞게 메일 내용을 수정할 수 있습니다.
- 하단의 `Floating Action Button` 을 누르면 메일앱으로 작성 내용이 연동됩니다.
<img width="30%" src="https://github.com/choubung/madcamp01/assets/112858914/9ce68e95-d82a-432d-96a3-5d9ed25a72d9"/>
<br/>
<br/>
<br/>

## Beta
**apk link**<br/>
https://drive.google.com/file/d/1_KWqvd4Z5wV9mBZXbxZrktVsm2dOXr-O/view?usp=drive_link
