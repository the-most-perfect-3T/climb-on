
/*window.onload = function() {
    const savedCode = sessionStorage.getItem('searchCode');  // sessionStorage에서 'searchCode' 가져오기
    if (savedCode) {
        document.getElementById('codeInput').value = savedCode;  // 입력 필드에 값 넣기
    }
}*/
//검색
function showSuggestions() {
    const name = document.getElementById('codeInput').value;
    const suggestionsDiv = document.getElementById('suggestions');

    if (name) {
        fetch(`/facilities/suggestions?code=${encodeURIComponent(name)}`) // 패치요청 responseEntity 로 jason 받음
            .then(response => {                         // encodeURIComponent 안전하게하는것
                if (!response.ok) {
                    throw new Error('서버 오류: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                console.log(data); // 데이터 구조 확인
                suggestionsDiv.textContent = ''; // 이전 추천 결과 초기화
                if (data && data.length > 0) {  // data가 존재하고, 그 길이가 0보다 클 경우
                    data.forEach(facilities => {
                        console.log(facilities.facilityName + "데이터가있음?"); // 각 메뉴 확인
                        const item = document.createElement('div');
                        item.className = 'suggestion-item';
                        item.textContent = facilities.facilityName; // 메뉴 코드 표시
                        item.onclick = () => selectSuggestion(facilities.facilityName);// 클릭 시 선택
                        console.log(facilities.facilityName);
                        suggestionsDiv.appendChild(item);
                    });
                    suggestionsDiv.style.display = 'block'; // 추천 결과 표시
                } else {
                    suggestionsDiv.style.display = 'none'; // 추천 결과 숨김
                }
            })
            .catch(error => {
                console.error('AJAX 오류:', error);
                suggestionsDiv.style.display = 'none'; // 오류 발생 시 숨김
            });
    } else {
        suggestionsDiv.textContent = '';
        suggestionsDiv.style.display = 'none'; // 입력이 없을 경우 숨김
    }
}

function selectSuggestion(facilities) {
    console.log(facilities);
    document.getElementById('codeInput').value = facilities; // 입력창에 선택한 코드 삽입
    document.getElementById('suggestions').style.display = 'none'; // 추천 결과 숨김
}

// DOM이 로드된 후에 이벤트 리스너를 등록
document.addEventListener("DOMContentLoaded", function() {
    // 'searchForm'에 submit 이벤트 리스너 추가
    const searchForm = document.getElementById('searchForm');
    searchForm.addEventListener('submit', handleSubmit);

});

// 폼 제출 처리
function handleSubmit(event) {
    event.preventDefault();  // 기본 폼 제출을 막음

    // 입력 필드에서 코드 값을 가져옴
    const searchCode = document.getElementById('codeInput').value;

    // sessionStorage에 코드 값 저장
    sessionStorage.setItem('searchCode', searchCode);

    // 폼을 제출해서 서버로 데이터를 전송
    const form = document.getElementById('searchForm');
    form.submit();  // 이때, 'searchCode'는 이미 sessionStorage에 저장되었으므로 폼 제출 시 자동으로 서버로 전달됨


}
function handleCategorySubmit(categoryId) {
    // 선택된 카테고리 ID를 폼에 추가하여 서버로 전송
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/facilities/categoryselect';

    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'categoryId';
    input.value = categoryId;
    form.appendChild(input);

    // sessionStorage에서 'searchCode' 값을 가져오기
    const facilityName = sessionStorage.getItem('searchCode');
    console.log(facilityName)
    // 'searchCode'가 존재하면, 이를 'code'라는 이름으로 폼에 추가
    if (facilityName) {
        const codeInput = document.createElement('input');
        codeInput.type = 'hidden';
        codeInput.name = 'facilityName';  // 서버에서 'searchCode'라는 이름으로 받기
        codeInput.value = facilityName;
        form.appendChild(codeInput);
    }
    document.body.appendChild(form);
    form.submit();
//세션 클리어
   // sessionStorage.removeItem('searchCode');
}

let map = null;
let marker = null; // 전역 marker
let selectedPosition = {lat: 37.4988635, lng: 127.0266457}; // 초기 위치

// 카카오 지도 API 로드 함수
function loadKakaoMap(facilities) {
    const {kakao} = window;

    // 지도와 마커 생성
    const latitude = facilities[0]?.latitude || selectedPosition.lat;
    const longitude = facilities[0]?.longitude || selectedPosition.lng;

    const container = document.getElementById('map');
    const options = {
        center: new kakao.maps.LatLng(latitude, longitude),
        level: 3
    };
    map = new kakao.maps.Map(container, options);

    // 시설마다 마커 추가
    // facilities는 loadKakaoApi 호출 시 전달해야 함
    facilities.forEach(facility => {
        const markerPosition = new kakao.maps.LatLng(facility.latitude, facility.longitude);
        console.log(facility)
        const facilityMarker = new kakao.maps.Marker({
            position: markerPosition,  // 마커 위치 설정
            map: map  // 지도에 마커 추가
        });

        // 마커 클릭 시 해당 시설 정보 표시

        kakao.maps.event.addListener(facilityMarker, 'click', function () {



            // 시설 정보가 보이는지 여부를 체크

            console.log(markerPosition.getLat())
            // 시설 정보와 관련된 로직을 추가적으로 넣을 수 있음
            // 예를 들어, 시설에 대한 세부사항을 보여주는 함수 호출
            showFacility(facility);


        });
    });

/*    // 초기 마커 설정
    marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(selectedPosition.lat, selectedPosition.lng),
        map: map
    });*/

  /*  // 지도 클릭 이벤트
    kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
        const latlng = mouseEvent.latLng;

      /!*  // 마커 위치 변경
        marker.setPosition(latlng);
        selectedPosition = {lat: latlng.getLat(), lng: latlng.getLng()};*!/
        showFacility
        // 선택된 좌표를 콘솔에 출력
        console.log(`Selected Location: ${selectedPosition.lat}, ${selectedPosition.lng}`);
    });*/
}
//해당위치로 지도이동
let currentMarker = null;  // 이전 마커를 추적하기 위한 변수

function showFacility(facility) {
    // 텍스트클릭시해당 위치로 지도 이동

    var moveLatLon = new kakao.maps.LatLng(facility.latitude,facility.longitude);
    map.panTo(moveLatLon);  // 지도 중심 이동
    console.log("왜안돼썅")
    // 로컬 서버의 이미지를 마커로 설정
    var markerImage = new kakao.maps.MarkerImage(
        '/images/logo.svg', // 상대 경로로 로컬 이미지 지정
        new kakao.maps.Size(50, 50),  // 마커 크기 (50x50)
        { offset: new kakao.maps.Point(25, 50) } // 마커의 기준점 (중앙 하단)
    );
    console.log(facility.latitude,facility.longitude)
    // 기존 마커가 있으면 제거
    if (currentMarker) {
        currentMarker.setMap(null); // 기존 마커 삭제
    }

    // 새로운 마커 생성
    var marker = new kakao.maps.Marker({
        position: moveLatLon,  // 마커 위치 설정
        image: markerImage      // 커스텀 이미지 설정
    });

    // 마커 지도에 표시
    marker.setMap(map);

    currentMarker = marker;

    showFacilityDetails(facility)

}
// 카카오 지도 API 스크립트 로드
function loadKakaoApi(facilities) {
    const script = document.createElement('script');
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=5787c5f817571b9179d48c038390a65f&autoload=false`;
    script.async = true;

    script.onload = () => {
        window.kakao.maps.load(() => loadKakaoMap(facilities));
    };

    script.onerror = () => {
        console.error('Kakao Maps API 스크립트를 로드하는 중 오류가 발생했습니다.');
    };

    document.head.appendChild(script);
}

// 모달 닫기 버튼 처리
function closeModal() {
    const mapModal = document.getElementById('map-modal');
    if (mapModal) {
        mapModal.style.display = 'none'; // 모달 숨기기
    }
}

window.onload = function () {

    articleMessage()

};

//초기화 버튼
function resetForm() {
    // 검색 입력 필드를 0으로 설정
    // 입력 필드에서 코드 값을 가져옴
    const searchCode = document.getElementById('codeInput').value;

    // sessionStorage에 코드 값 저장
    sessionStorage.setItem('searchCode', searchCode);
    // 폼 제출
    document.getElementById('searchForm').submit();
}
let currentfacility = null;
function showFacilityDetails(facility){
    // facility 객체의 값을 조건에 맞게 출력
    if(facility != null){
        facilityDetailsContainer.style.display = 'block';
    }

    console.log("클릭두번되나요")


    const facilityDetailsHTML = `
 <div class="facility-details">
        <h3>시설명: ${facility.facilityName || '정보 없음'}</h3>
        <p><strong>주소:</strong> ${facility.address || '정보 없음'}</p>
        <p><strong>전화번호:</strong> ${facility.contact || '정보 없음'}</p>
        <p><strong>운영시간:</strong> ${facility.openingTime || '정보 없음'}</p>
        <p><strong>시설 유형:</strong> ${facility.facilityType || '정보 없음'}</p>
        <p><strong>위도:</strong> ${facility.latitude ? facility.latitude : '정보 없음'}</p>
        <p><strong>경도:</strong> ${facility.longitude ? facility.longitude : '정보 없음'}</p>
        </div>
    `;
    // facilityDetailsContainer에 세부 정보를 삽입
    const detailsContainer = document.getElementById('facilityDetailsContainer');
    detailsContainer.innerHTML = facilityDetailsHTML;

    fetch(`/Review/Reviews?code=${encodeURIComponent(facility.id)}`) // 패치요청 responseEntity 로 jason 받음
        .then(response => {                         // encodeURIComponent 안전하게하는것
            if (!response.ok) {
                throw new Error('서버 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log(data); // 데이터 구조 확인

            if (data && data.length > 0) {  // data가 존재하고, 그 길이가 0보다 클 경우
                data.forEach(Reviews => {
                    console.log(Reviews.comment + "데이터가있음?"); // 각 메뉴 확인
                    const item = document.createElement('div');
                    const reviewDate = new Date("2024-08-01T12:00:00Z"); // 예시 날짜
                    const timeText = timeAgo(reviewDate);
                    item.className = 'Review-item';
                    // Using innerHTML to insert the review comment
                    item.innerHTML = `<p>${Reviews.userNickname}</p>
                                        <span>${timeText}</span>
                                        <p>${Reviews.rating}</p>
                                        <p>${Reviews.likeCount}</p>
                                        <p>${Reviews.comment || '댓글이 없습니다'}</p>`;
                    // Append the review item div to the details container

                    detailsContainer.appendChild(item);
                });

                currentfacility = facility;
            }


        });

}

// 주어진 날짜(예: 리뷰 작성일)와 현재 시간의 차이를 계산하는 함수
function timeAgo(date) {
    const now = new Date();
    const diffInSeconds = Math.floor((now - date) / 1000);

    const minutes = Math.floor(diffInSeconds / 60);
    const hours = Math.floor(diffInSeconds / 3600);
    const days = Math.floor(diffInSeconds / (3600 * 24));
    const months = Math.floor(diffInSeconds / (3600 * 24 * 30)); // 대략적으로 한 달을 30일로 계산
    const years = Math.floor(diffInSeconds / (3600 * 24 * 365)); // 대략적으로 1년을 365일로 계산

    if (years > 0) {
        return `${years}년 전`;
    } else if (months > 0) {
        return `${months}개월 전`;
    } else if (days > 0) {
        return `${days}일 전`;
    } else if (hours > 0) {
        return `${hours}시간 전`;
    } else if (minutes > 0) {
        return `${minutes}분 전`;
    } else {
        return `방금 전`;
    }
}

// 사용 예시

