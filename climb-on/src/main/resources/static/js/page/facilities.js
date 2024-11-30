/*let Reviews22;

window.onload = async function() {


}*/
//검색*/

/*document.addEventListener('DOMContentLoaded', async () => {
    // DOMContentLoaded 이벤트가 발생하면, getFacilityDetails로부터 시설 정보를 받아옴
    const facility = await getFacilityDetails();  // 서버에서 시설 정보 받기
    await showFacilityDetails(facility);  // 받은 시설 정보를 보여줌

});*/

function showSuggestions() {
    const name = document.getElementById('codeInput').value;
    const suggestionsDiv = document.getElementById('suggestions');

    suggestionsDiv.style.display = 'block';
    if (name) {
        fetch(`/facilities/suggestions?code=${encodeURIComponent(name)}`) // 패치요청 responseEntity 로 jason 받음
            .then(response => {                         // encodeURIComponent 안전하게하는것
                if (!response.ok) {
                    throw new Error('서버 오류: ' + response.status);
                }
                return response.json();
            })
            .then(data => {

                suggestionsDiv.textContent = ''; // 이전 추천 결과 초기화
                if (data && data.length > 0) {  // data가 존재하고, 그 길이가 0보다 클 경우
                    data.forEach(facilities => {

                        const item = document.createElement('div');
                        item.className = 'suggestion-item';
                        item.textContent = facilities.facilityName; // 메뉴 코드 표시
                        item.onclick = () => selectSuggestion(facilities.facilityName);// 클릭 시 선택

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

    }
    else {
        suggestionsDiv.textContent = '';
        suggestionsDiv.style.display = 'none'; // 입력이 없을 경우 숨김
    }
}

function selectSuggestion(facilities) {
    document.getElementById('codeInput').value = facilities; // 입력창에 선택한 코드 삽입
    document.getElementById('suggestions').style.display = 'none'; // 추천 결과 숨김
}

// DOM이 로드된 후에 이벤트 리스너를 등록

document.addEventListener("DOMContentLoaded", function() {
    // 'searchForm'에 submit 이벤트 리스너 추가
    const searchForm = document.getElementById('searchForm');
    searchForm.addEventListener('submit', handleSubmit);
    const favoriteButton = document.getElementById('favorite-btn-${facility.id}');




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

    // 지도 로딩 중 애니메이션 표시
    const loadingSpinner = document.getElementById('loadingSpinner');
    loadingSpinner.style.display = 'block'; // 로딩 스피너 보이기


    // 지도와 마커 생성
    const latitude = facilities[0]?.latitude || selectedPosition.lat;
    const longitude = facilities[0]?.longitude || selectedPosition.lng;

    const container = document.getElementById('map');
    const options = {
        center: new kakao.maps.LatLng(latitude, longitude),
        level: 8
    };
    map = new kakao.maps.Map(container, options);


    showOffcanvas();


    // 시설마다 마커 추가
    // facilities는 loadKakaoApi 호출 시 전달해야 함
    facilities.forEach(facility => {
        const markerPosition = new kakao.maps.LatLng(facility.latitude, facility.longitude);

        const facilityMarker = new kakao.maps.Marker({
            position: markerPosition,  // 마커 위치 설정
            map: map  // 지도에 마커 추가
        });

        // 마커 클릭 시 해당 시설 정보 표시

        kakao.maps.event.addListener(facilityMarker, 'click', function () {

            var moveLatLon = new kakao.maps.LatLng(facility.latitude,facility.longitude);
            map.panTo(moveLatLon);  // 지도 중심 이동

            // 로컬 서버의 이미지를 마커로 설정
            var markerImage = new kakao.maps.MarkerImage(
                '/images/logo.svg', // 상대 경로로 로컬 이미지 지정
                new kakao.maps.Size(50, 50),  // 마커 크기 (50x50)
                { offset: new kakao.maps.Point(25, 50) } // 마커의 기준점 (중앙 하단)
            );
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
            // 시설 정보와 관련된 로직을 추가적으로 넣을 수 있음
            // 예를 들어, 시설에 대한 세부사항을 보여주는 함수 호출
            facilityDetailsContainer.classList.remove('visible');
            facilityDetailsContainer.style.visibility = 'hidden';
            showFacility(facility);


            setTimeout(() => {
                facilityDetailsContainer.classList.add('visible');  // visible 클래스를 추가하여 애니메이션 효과 시작
            }, 500); // 50ms 정도 대기 후 애니메이션 시작



        });
        kakao.maps.event.addListener(map, 'click', function () {

            facilityDetailsContainer.classList.remove('visible');
            facilityDetailsContainer.style.visibility = 'hidden';

        });


     //
    });

/*    // 초기 마커 설정
    marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(selectedPosition.lat, selectedPosition.lng),
        map: map
    });*/

   // 지도 클릭 이벤트
  /*  kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
        const latlng = mouseEvent.latLng;

        // 마커 위치 변경
        marker.setPosition(latlng);
        selectedPosition = {lat: latlng.getLat(), lng: latlng.getLng()};
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

    // 로컬 서버의 이미지를 마커로 설정
    var markerImage = new kakao.maps.MarkerImage(
        '/images/logo.svg', // 상대 경로로 로컬 이미지 지정
        new kakao.maps.Size(50, 50),  // 마커 크기 (50x50)
        { offset: new kakao.maps.Point(25, 50) } // 마커의 기준점 (중앙 하단)
    );
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
    setTimeout(function() {
        showFacilityDetails(facility)

    }, 300);
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
    document.getElementById("img-wrap0").addEventListener("click", function () {
        const button = this;

        // active 클래스 추가
        button.classList.add("active");
    })
    // sessionStorage에 코드 값 저장
    sessionStorage.setItem('searchCode', searchCode);
    // 폼 제출
    document.getElementById('searchForm').submit();
}
let currentfacility = null;

async function checkFavorite(id) {
    let isFavorite = await getIsFavorite(id);

    return isFavorite;
}
async function reviewcheckFavorite(id) {
    let isFavorite = await reviewgetIsFavorite(id);

    return isFavorite;
}

let globalDettails;
 //리뷰 상태저장
const detailsContainer = document.getElementById('facilityDetailsContainer');


const detailsContainerContainer = document.getElementById('facility-detail333');

async function showFacilityDetails(facility) {

    // facility 객체의 값을 조건에 맞게 출력
    if (facility != null) {
        setTimeout(() => {
            facilityDetailsContainer.classList.add('visible');
        }, 50); // 50ms 정도 대기 후 애니메이션 시작
    }
    let isFavorite;

    isFavorite = await checkFavorite(facility.id);

    // facilityDetailsHTML 내에 로딩 스피너와 세부 정보를 함께 포함
    const facilityDetailsHTML = `
        <div class="facility-details">

            <div class="facility-details-top">
                <img id="facilityImg" loading="lazy" class="facility-banner-content" src=""/></br>
                <div class="nameAndStart">
                    <h3>${facility.facilityName || '정보 없음'}</h3>
                    <div class="favorite-btn" id="favorite-btn-${facility.id}" onClick="toggleFavorite(${facility.id},${isFavorite})">
                        ${isFavorite ? '<i class="fa-bookmark fa-solid"></i>' : '<i class="fa fa-bookmark-o"></i>'}
                    </div>
                </div>
            </div>
            <h4><strong>기본정보</strong></h4>
            <p>주소: ${facility.address || '정보 없음'}</p>
            <p>전화번호: ${facility.contact || '정보 없음'}</p>
            <p>운영시간: ${facility.openingTime || '정보 없음'}</p>
            <p>시설 유형: ${facility.facilityType || '정보 없음'}</p>
        </div>
    `;

    // facilityDetailsContainer에 세부 정보를 삽입
    globalDettails = facilityDetailsHTML;
    detailsContainer.innerHTML = facilityDetailsHTML;
    // loadReviews와 loadImage가 완료된 후
    await loadReviews(facility.id);
    await loadImage(facility.id);




    currentfacility = facility;
}

function renderStars(averageRating) {
    let starsHtml = '';

    for (let i = 1; i <= 5; i++) {
        if (averageRating >= i) {
            starsHtml += '<span class="filled"><i class="fa fa-star" aria-hidden="true"></i></span>';
        } else if (averageRating >= i - 0.5) {
            starsHtml += '<span class="half-filled"><i class="fa fa-star-half-o" aria-hidden="true"></i></span>';
        } else {
            starsHtml += ''; // 별을 표시하지 않음
        }
    }

    return starsHtml;
}




function loadReviews(facilityId) {
    // detailsContainer의 기존 HTML을 지우지 않고, 새로운 데이터를 추가하는 방식으로 수정


    // 1. 기존 리뷰 아이템 삭제
    const existingReviews = detailsContainer.querySelectorAll('.Review-item');
    existingReviews.forEach(review => {
        review.remove();  // 각 리뷰 아이템 삭제
    });

    const reviewSummary = detailsContainer.querySelector('.item2');
    if (reviewSummary) {
        reviewSummary.remove();  // reviewSummary 삭제
    }

    fetch(`/Review/Reviews?code=${facilityId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 오류: ' + response.status);
            }

            return response.json();

        })

        .then(async data => {
            // 데이터를 받아와서 리뷰가 존재하는 경우에만 처리



            if (1) {
                // 기존 내용 갱신 (기존 html이 있으면 그 내용 추가)
                let Reviews22 = await getUserId();

                const reviewSummary = `
                <div class="facilityReviews" id="facilityReviews">
                <div class="item2">
                  <div class="item4">
                    <span class="review-sum">리뷰 : <i style="color: #FF7F27">${data.length}</i></span>
                  </div>    
                   
                  <div class="reveiw-star-sum">
                    <span class="review-avg"> ${data[0] && data[0].averageRating !== undefined ? (Math.floor(data[0].averageRating * 10) / 10) : 0}/5</span>
                            <div id="stars-container"></div> 
                             <p style="display:  ${Reviews22 === undefined || Reviews22 === 0 ? 'none' : 'block'}" class="review-guide">리뷰를 남기면 나의 체크인목록에 표시됩니다.<br\>
                        다른 클라이머들을 위해 리뷰를 남겨주세요</p>
                        <p style="display:  ${Reviews22 === undefined || Reviews22 === 0 ? 'block' : 'none'}" class="review-guide">리뷰를 남기시려면 로그인을 해주세요.<br\></p>
                  </div>
                  <div class="item3">
                    <i type="button" class="btn-fmodal" data-bs-toggle="modal" data-bs-target="#exampleModal"
                    style="display:  ${Reviews22 === undefined || Reviews22 === 0 ? 'none' : 'block'}" >작성</i>
                  </div>
                    <br>
                </div>
                </div>
            `;

                const item2 = document.createElement('div');
                item2.innerHTML = reviewSummary;
                // 2. 새로운 내용 추가
                detailsContainer.appendChild(item2);


                if (data && data.length > 0)
                {
                // 별점 렌더링
                document.getElementById('stars-container').innerHTML = renderStars(data[0].averageRating);

                // 각 리뷰 내용 동적으로 생성하여 추가
                for (const Reviews of data) {

                    let Reviews2 = await getReview(Reviews.id);
                    const isFavorite = await reviewcheckFavorite(Reviews.id || defaultId);

                    const item = document.createElement('div');
                    const reviewDate = new Date(Reviews.createdAt);
                    const timeText = timeAgo(reviewDate);
                    item.className = 'Review-item';
                    // 리뷰 내용 구성
                    item.innerHTML = `
                    <div class="review-detail" id="review-detail">
                        <div class="review-detail-nickname">
                        
                            <p class="userModalOpen" data-id="${Reviews.reviewerId}">${Reviews.userNickname}</p>
                            <span class="review-time">${timeText}</span>   
                                <div class="review-actions" id="review-actions" style="display: ${Reviews2.user ? 'block' : 'none'};">
                                    <button class="menu-buttonreview" ><i class="fa-solid fa-bars"></i></button>
                                    <div class="dropdown-menureview">
                                        <button class="edit-review-btn" onclick="editReview(${Reviews.id})">수정</button>    
                                        <button class="delete-btn" onclick="confirmDelete(${Reviews.id})">삭제</button>
                                    </div>
                                </div>  
                                
                                
                        </div>
                        
                        <div class="time-rating">
      
                            <div class="review-rating">
                                <div class="reviewstars-container" id="reviewstars-${Reviews.id}"></div>
                            </div>
                        </div>
                        <p class="review-text">${Reviews.comment || '댓글이 없습니다'}</p>
                        <div class="review-text-fav">
                            <div className="reviewfavorite-btn" id="reviewfavorite-btn-${Reviews.id}" onClick="reviewtoggleFavorite(${Reviews.id},${isFavorite})">
                                ${isFavorite ?? false ?'<i class="fa-heart fa-solid"></i>':'<i class="fa-heart fa-regular"></i>'}
                            </div>
                            
                        </div>          
                    </div>
                `;
                    detailsContainer.appendChild(item); // 새로 추가된 리뷰 항목을 detailsContainer에 추가

                    const starContainer = document.getElementById(`reviewstars-${Reviews.id}`);
                    starContainer.innerHTML = renderStars(Reviews.rating); // 해당 리뷰의 별을 표시

                }
                    userMondalOpen();

            }
        }
        })
        .catch(error => {
            console.error('Error:', error);
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



async function reviewtoggleFavorite(id,isFavorite) {
    // 즐겨찾기 목록에서 해당 시설이 이미 존재하는지 확인

    try {
        const response = await reviewupdateFavoriteOnServer(id, !isFavorite); // 즐겨찾기 상태를 반전시켜서 서버에 보냄


        if (response.ok) {
            console.log('서버에 즐겨찾기 상태가 업데이트되었습니다.');
            const updatedFavoriteStatus = await reviewcheckFavorite(id);
            await reviewshowButton(id, updatedFavoriteStatus) // 반전된 값 반환

        } else {

            console.error('서버에 즐겨찾기 상태 업데이트 실패');
        }
    } catch (error) {
        console.error('서버와의 통신 중 오류 발생:', error);
    }


}


async function toggleFavorite(id,isFavorite) {
    // 즐겨찾기 목록에서 해당 시설이 이미 존재하는지 확인

    try {
        const response = await updateFavoriteOnServer(id, !isFavorite); // 즐겨찾기 상태를 반전시켜서 서버에 보냄


        if (response.ok) {
            console.log('서버에 즐겨찾기 상태가 업데이트되었습니다.');
            const updatedFavoriteStatus = await checkFavorite(id);
            await showButton(id, updatedFavoriteStatus) // 반전된 값 반환

        } else {

            console.error('서버에 즐겨찾기 상태 업데이트 실패');
        }
    } catch (error) {
        console.error('서버와의 통신 중 오류 발생:', error);
    }


}

async function showButton(id, isFavorite ) {

    const button = document.getElementById(`favorite-btn-${id}`);
    if (button) {
        button.innerHTML = isFavorite ? '<i class="fa-bookmark fa-solid"></i>':'<i class="fa fa-bookmark-o"></i>' ;

        // 버튼의 클릭 이벤트 업데이트 (옵션)
        button.setAttribute('onclick', `toggleFavorite(${id}, ${isFavorite})`);
    }

}

async function reviewshowButton(id, isFavorite ) {

    const button = document.getElementById(`reviewfavorite-btn-${id}`);
    if (button) {
        button.innerHTML = isFavorite ? '<i class="fa-solid fa-heart"></i>':'<i class="fa-heart fa-regular"></i>';

        // 버튼의 클릭 이벤트 업데이트 (옵션)
        button.setAttribute('onclick', `reviewtoggleFavorite(${id}, ${isFavorite})`);
    }

}
async function reviewupdateFavoriteOnServer(id, addFavorite) {
    const url = '/Review/update-favorite';
    const data = {
        facilityId: id,
        isFavorite: addFavorite,  // 즐겨찾기 추가 여부
    };

    const response = await fetch(url, {
        method: 'POST',  // POST 메소드로 요청
        headers: {
            'Content-Type': 'application/json',  // JSON 형식으로 전송
        },
        body: JSON.stringify(data),  // 데이터를 JSON 형식으로 변환하여 전송
    });

    return response;
}



async function updateFavoriteOnServer(id, addFavorite) {
    const url = '/facilities/update-favorite';
    const data = {
        facilityId: id,
        isFavorite: addFavorite,  // 즐겨찾기 추가 여부
    };

    const response = await fetch(url, {
        method: 'POST',  // POST 메소드로 요청
        headers: {
            'Content-Type': 'application/json',  // JSON 형식으로 전송
        },
        body: JSON.stringify(data),  // 데이터를 JSON 형식으로 변환하여 전송
    });

    return response;
}
async function reviewgetIsFavorite(id) {

    const validId = id || 0; // 기본값을 0으로 설정 (기본값을 다른 값으로 설정할 수도 있습니다)


    try {
        const response = await fetch(`/Review/getIsFavorite?id=${validId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error('서버 오류: ' + response.status);
        }

        const data = await response.json();

        if (data !== null && data !== undefined) {
            return data;  // 데이터를 반환
        } else {
            console.log("즐겨찾기 정보가 없습니다.");
            return null;  // 데이터가 없으면 null 반환
        }
    } catch (error) {
        console.error('오류 발생:', error);
        return 0;  // 오류 발생시 null 반환
    }
}





async function getIsFavorite(id) {
    try {

        const validId = id || 0; // 기본값을 0으로 설정 (기본값을 다른 값으로 설정할 수도 있습니다)
        const response = await fetch(`/facilities/getIsFavorite?id=${validId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error('서버 오류: ' + response.status);
        }

        const data = await response.json();

        if (data !== null && data !== undefined) {
            return data;  // 데이터를 반환
        } else {
            console.log("즐겨찾기 정보가 없습니다.");
            return null;  // 데이터가 없으면 null 반환
        }
    } catch (error) {
        console.error('오류 발생:', error);
        return null;  // 오류 발생시 null 반환
    }
}





async function loadImage(facilityId){


    const url = `/facilityImg/getImage?facilityId=${facilityId}`;



    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',  // JSON 형식으로 전송
        },

    });

    const imagePath = await response.json();// 서버에서 경로를 텍스트로 받음

    // 이미지 경로를 사용해 이미지를 HTML에 표시
    const imgElement = document.getElementById('facilityImg');

if(Array.isArray(imagePath) && imagePath.length) {

    imgElement.src = imagePath[0].filePath;

}

else
    imgElement.src = "/images/default.jpg";

}







document.addEventListener("DOMContentLoaded", function() {



    const stars = document.querySelectorAll(".rating .star"); // 별 요소들
    const resetBtn = document.getElementById("modal-close"); // '닫기' 버튼


    let rating =0;
    // 별 클릭 시 평점 설정
    stars.forEach(star => {
        star.addEventListener("click", function() {
            rating = parseInt(this.getAttribute("data-value"));
            updateStars(rating);
        });

        // 마우스 오버 시 동적 별 채우기
        star.addEventListener("mouseover", function() {
            const value = parseInt(this.getAttribute("data-value"));
            updateStars(value);
        });

        // 마우스 아웃 시 현재 평점 상태 유지
        star.addEventListener("mouseout", function() {
            updateStars(rating);
        });
    });

    const modalElement = document.getElementById("exampleModal");

// 모달 닫힘 이벤트에 리스너 추가
    modalElement.addEventListener("hidden.bs.modal", function () {
        // 입력 필드 초기화
        document.getElementById("comment").value = "";
        document.getElementById("ratingValue").value = "";
        document.getElementById("facilityId").value = "";
        document.getElementById('exampleModalLabel').innerHTML = "리뷰 작성";  //닫힐때 초기화
        resetStars();

    });

    // 별 초기화 함수
    function resetStars() {
        stars.forEach(star => {
            star.querySelector("i").classList.remove("fa-star");
            star.querySelector("i").classList.add("fa-star");
            star.querySelector("i").style.color = "gray"; // 기본 회색으로 되돌리기
        });
        rating = 0; // 초기 평점으로 리셋
    }

    // 별 상태 업데이트 함수
    function updateStars(rating) {
        stars.forEach(star => {
            const value = parseInt(star.getAttribute("data-value"));
            if (value <= rating) {
                star.querySelector("i").classList.remove("fa-star");
                star.querySelector("i").classList.add("fa-star");
                star.querySelector("i").style.color = "#f79256";
            } else {
                star.querySelector("i").classList.remove("fa-star");
                star.querySelector("i").classList.add("fa-star");
                star.querySelector("i").style.color = "gray"; // 빈 별은 회색
            }
        });
    }

    const reviewForm = document.getElementById("reviewinsertForm");



    reviewForm.addEventListener("submit", async function (event) {
        // 평점이 선택되지 않았으면 경고

        event.preventDefault();

        const allGray = Array.from(document.querySelectorAll('#rating .star i')).every(icon => {
            return icon.style.color === 'gray'; // 모든 아이콘이 회색인지 확인

        });

        if (allGray) {

            alert("평점을 선택해 주세요!");
            return;
        }

        rating = Number(rating); // 다시 숫자로 변환
        if(rating === 0 || rating===""){
            rating = document.getElementById("ratingValue").value;

            if (rating === 0 || rating ==="") {
                alert("평점을 선택해 주세요!");
                return;
            }





        }else {
            document.getElementById("ratingValue").value = rating;
// 모든 별 요소를 선택


            if (rating === 0 || rating ==="") {


                alert("평점을 선택해 주세요!");
                return;
            }
        }

        // 리뷰 내용이 비어있으면 경고
        const comment = document.getElementById("comment").value;
        if (comment.trim() === "") {
            alert("리뷰 내용을 작성해 주세요!");

            event.preventDefault();  // 폼 제출을 막음
            return;
        }
        // 이 부분을 통해 시설 ID를 가져오는 로직을 작성하세요.
        let id = document.getElementById('reviewId').value;
        document.getElementById('facilityId').value = currentfacility.id;



        const url = '/Review/reviewInsert';
        const data = {
            rating: rating,
            comment: comment,
            facilityId: currentfacility.id,
            id : id

        };


        await fetch(url, {
            method: 'POST',  // POST 메소드로 요청
            headers: {
                'Content-Type': 'application/json',  // JSON 형식으로 전송
            },
            body: JSON.stringify(data),  // 데이터를 JSON 형식으로 변환하여 전송

        });

        document.getElementById('reviewId').value = null;
        document.getElementById('exampleModalLabel').innerHTML = "리뷰 작성";  //폼보내고 초기화
        await loadReviews(currentfacility.id);




    });
});

async function getReview(id) {

    const response = await fetch(`/Review/getReview?id=${id}`);
    const review = await response.json();  // 리뷰 데이터를 JSON으로 파싱




/*

    const reviewActions = document.querySelector(`#review-actions-${id}`);



    if (reviewActions) {
        const editButton = reviewActions.querySelector('.edit-review-btn');
        const deleteButton = reviewActions.querySelector('.delete-btn');


        console.log("왜보이냐고" + review.user)
        if (review.user) {
            // '수정' 버튼과 '삭제' 버튼을 숨깁니다.
            if (editButton) editButton.style.display = 'none';
            if (deleteButton) deleteButton.style.display = 'none';
        } else {
            // '수정' 버튼과 '삭제' 버튼을 보이게 합니다.
            if (editButton) editButton.style.display = 'inline-block';
            if (deleteButton) deleteButton.style.display = 'inline-block';
        }
*/


    return review;
}
async function deleteReview(id){
    const url = '/Review/reviewDelete';
    const data = {
        id : id
    };

    await fetch(url, {
        method: 'POST',  // POST 메소드로 요청
        headers: {
            'Content-Type': 'application/json',  // JSON 형식으로 전송
        },
        body: JSON.stringify(data),  // 데이터를 JSON 형식으로 변환하여 전송
    });


    await loadReviews(currentfacility.id);
}

async function editReview(id){
    // fetch로 데이터를 불러옵니다.
    try {

        let review = await getReview(id);


       loadReviewData(review);  // 리뷰 데이터를 모달에 로드
        const exampleModal = new bootstrap.Modal(document.getElementById('exampleModal'));
        exampleModal.show();
    } catch (error) {
        console.error('리뷰 데이터를 불러오는 데 실패했습니다:', error);
        alert('리뷰 데이터를 불러오는 데 실패했습니다.');
    }
}

// 리뷰 데이터를 모달에 로드하는 함수
function loadReviewData(review) {


    // review.rating 값을 숨겨진 input에 설정
    document.getElementById('ratingValue').setAttribute('value', review.rating);
    document.getElementById('exampleModalLabel').innerHTML = "리뷰 수정"; //함수들어오면 수정


// review.comment 값을 textarea에 설정
    document.getElementById('comment').value = review.comment;

    document.getElementById("reviewId").value = review.id;


// 평점에 맞는 별 표시
    document.querySelectorAll('#rating .star').forEach(star => {
        const starValue = star.getAttribute('data-value'); // 각 별의 data-value 속성 값 가져오기
        const icon = star.querySelector("i"); // <i> 태그 찾기

        if (icon) { // <i> 태그가 존재하는지 확인
            if (starValue <= review.rating) { // 별의 값이 리뷰 평점보다 작거나 같으면
                icon.classList.remove("fa-star"); // 빈 별을 제거
                icon.classList.add("fa-star"); // 채워진 별 추가
                icon.style.color = "#f79256"; // 채워진 별은 색상 변경
            } else { // 별의 값이 리뷰 평점보다 크면
                icon.classList.remove("fa-star"); // 채워진 별을 제거
                icon.classList.add("fa-star"); // 빈 별 추가
                icon.style.color = "gray"; // 빈 별은 회색
            }
        }
    });
}

async function getUserId(){
    const response = await fetch(`/facilities/userId`);
    const Id = await response.json();  // 리뷰 데이터를 JSON으로 파싱

    return Id;

}

/*
// 첫 페이지 렌더링
if (Array.isArray(data)) await renderData(currentPage);
if(data.length > itemsPerPage) {
    const paginationElement = document.querySelector("#review .pagination");
    renderPagination(paginationElement, data.length, itemsPerPage, currentPage, (newPage) => {
        currentPage = newPage;
        renderData(currentPage);
        renderPagination(paginationElement, data.length, itemsPerPage, currentPage, arguments.callee);
    });
}


// 삭제 후 다시 렌더링
await renderData(currentPage);
if (data.length >= itemsPerPage) {
    const paginationElement = document.querySelector("#review .pagination");
    renderPagination(paginationElement, data.length, itemsPerPage, currentPage, (newPage) => {
        currentPage = newPage;
        renderData(currentPage);
        renderPagination(paginationElement, data.length, itemsPerPage, currentPage, arguments.callee);
    });
} else {
    document.querySelector("#review .pagination").textContent = "";
}


function renderPagination(paginationElement, totalItems, itemsPerPage, currentPage, onPageChange) {
    paginationElement.textContent = ""; // 기존 페이지네이션 초기화

    const totalPages = Math.ceil(totalItems / itemsPerPage);

    // 이전 버튼
    const prevButton = document.createElement("li");
    prevButton.className = `prev ${currentPage === 1 ? "disabled" : ""}`;
    prevButton.innerHTML = `<a href="#"><i class="fa-solid fa-circle-chevron-left"></i></a>`;
    prevButton.querySelector("a").addEventListener("click", (e) => {
        e.preventDefault();
        if (currentPage > 1) onPageChange(currentPage - 1);
    });
    paginationElement.appendChild(prevButton);

    // 페이지 번호 버튼
    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement("li");
        pageButton.className = `num ${i === currentPage ? "current" : ""}`;
        pageButton.innerHTML = `<a href="#">${i}</a>`;
        pageButton.querySelector("a").addEventListener("click", (e) => {
            e.preventDefault();
            onPageChange(i);
        });
        paginationElement.appendChild(pageButton);
    }

    // 다음 버튼
    const nextButton = document.createElement("li");
    nextButton.className = `next ${currentPage === totalPages ? "disabled" : ""}`;
    nextButton.innerHTML = `<a href="#"><i class="fa-solid fa-circle-chevron-right"></i></a>`;
    nextButton.querySelector("a").addEventListener("click", (e) => {
        e.preventDefault();
        if (currentPage < totalPages) onPageChange(currentPage + 1);
    });
    paginationElement.appendChild(nextButton);
}*/
// 삭제 확인 함수
function confirmDelete(reviewId) {
    // 사용자가 "확인"을 클릭한 경우에만 삭제를 실행
    if (confirm('정말 삭제하시겠습니까?')) {
        deleteReview(reviewId);  // 삭제 함수 호출
    }
}
document.addEventListener('DOMContentLoaded', () => {
    // 모든 드롭다운 버튼을 가져옵니다.
    const menuButtons = document.querySelectorAll('.menu-buttonreview');

    menuButtons.forEach((button) => {
        button.addEventListener('click', (event) => {
            // 현재 버튼의 바로 다음 요소(드롭다운)를 찾습니다.
            const dropdown = button.nextElementSibling;

            if (dropdown) {
                dropdown.classList.toggle('open'); // 'open' 클래스 추가/제거
            } else {
                console.error('Dropdown not found for button:', button);
            }
        });
    });

    // 외부 클릭 시 드롭다운 닫기
    document.addEventListener('click', (event) => {
        const openDropdowns = document.querySelectorAll('.dropdown-menureview.open');
        openDropdowns.forEach((dropdown) => {
            if (
                !dropdown.contains(event.target) && // 드롭다운 내부 클릭이 아닌 경우
                !dropdown.previousElementSibling.contains(event.target) // 버튼 클릭이 아닌 경우
            ) {
                dropdown.classList.remove('open');
            }
        });
    });
});

// 스켈레톤 로딩 표시
function showSkeletonLoader() {
    const skeleton = document.createElement('div');
    skeleton.className = 'skeleton-loader';
    skeleton.innerHTML = '<div class="skeleton-item"></div>'.repeat(3); // 반복되는 스켈레톤 항목
    detailsContainer.appendChild(skeleton);
}

// 스켈레톤 로딩 제거
function hideSkeletonLoader() {
    const skeleton = detailsContainer.querySelector('.skeleton-loader');
    if (skeleton) skeleton.remove();
}





// DOM이 완전히 로드된 후 실행
document.addEventListener("DOMContentLoaded", function () {
    // 기존 이미지에 대해 처리
    const images = document.querySelectorAll('img');
    images.forEach(img => {
        if (img.complete) {
            // 이미지가 이미 로드된 경우
            handleImageLoad({ target: img });
        } else {
            // 새로 로드되는 경우
            img.addEventListener('load', handleImageLoad);
        }

        // 로드 실패 처리
        img.addEventListener('error', () => {
            console.error(`Failed to load image: ${img.src}`);
        });
    });

    // 동적으로 추가된 이미지에 대한 처리
    const observer = new MutationObserver((mutations) => {
        mutations.forEach(mutation => {
            mutation.addedNodes.forEach(node => {
                if (node.tagName === 'IMG') {
                    if (node.complete) {
                        handleImageLoad({ target: node });
                    } else {
                        node.addEventListener('load', handleImageLoad);
                    }

                    // 로드 실패 처리
                    node.addEventListener('error', () => {
                        console.error(`Failed to load image: ${node.src}`);
                    });
                }
            });
        });
    });

    observer.observe(document.body, { childList: true, subtree: true });
});

// 이미지 로드 핸들러
function handleImageLoad(event) {
    const image = event.target; // 이벤트의 대상이 되는 이미지


    // 부모 요소에서 skeleton-item을 찾고 숨김
    const skeleton = image.parentElement.querySelector('.skeleton-item');
    if (skeleton) {
        skeleton.style.display = 'none';
    }

    // 이미지 표시

    image.style.display = 'block';
}





// offcanvas를 보이게 하는 함수
function showOffcanvas() {

    const offcanvas = document.querySelector('.content .offcanvas');

    // 로딩이 완료되었을 때

    offcanvas.style.visibility = 'visible';  // 보이게 설정
    offcanvas.style.transform = 'translateX(0)';  // 왼쪽으로 슬라이드

    setTimeout(() => {
        loadingSpinner.style.display = 'none'; // 애니메이션 숨기기
    }, 500);  // 애니메이션이 끝날 때까지 약간의 시간 지연
}

// MutationObserver 설정
const observer = new MutationObserver((mutations) => {
    mutations.forEach(mutation => {
        mutation.addedNodes.forEach(node => {
            // 맵이 로드된 후 추가되는 요소를 감지
            if (node.nodeType === 1 && node.matches('#map')) {
                // #map 요소가 DOM에 추가되었을 때 offcanvas를 표시
                showOffcanvas();
            }
        });
    });
});

// body나 특정 영역에서 새로운 요소가 추가되는지 감지
observer.observe(document.body, { childList: true, subtree: true });




function userMondalOpen() {
    const userModalOpen = document.querySelectorAll(".userModalOpen");
    if(userModalOpen){
        userModalOpen.forEach(function(el, i){



            if(el.textContent === "익명"){
                el.style.cursor = "default";
            }
            el.addEventListener("click", function(e){


                const userId = parseInt(e.target.getAttribute("data-id"));


                if (isNaN(userId)) {
                    console.error("Invalid userId:", userId);
                    return; // 유효하지 않은 경우 요청 중단
                }


                fetch(`/user/${userId}`) // 서버의 요청 URL
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`Error: ${response.status}`);
                        }
                        return response.json(); // JSON 데이터를 기대
                    })
                    .then(data => {

                        // 서버에서 받은 데이터로 모달 내용 채우기
                        const userViewModal = document.getElementById("userViewModal");

                        userViewModal.querySelector(".top .img-wrap img").setAttribute("src", data.user.profilePic);
                        userViewModal.querySelector(".top .nickname").textContent = data.user.nickname;
                        userViewModal.querySelector(".top .one-liner").textContent = data.user.oneLiner != null ? data.user.oneLiner : "한줄 소개가 없습니다.";
                        userViewModal.querySelector(".middle .crew .cont").textContent = data.crewName != null ? data.crewName : "가입한 크루가 없습니다.";
                        userViewModal.querySelector(".middle .home .cont").textContent = data.homeName != null ? data.homeName : "등록한 홈짐이 없습니다.";

                        // 모달 띄우기
                        const modal = new bootstrap.Modal(document.getElementById('userViewModal'));
                        modal.show();
                    })
                    .catch(error => {
                        console.error("에러 발생:", error);
                    });
            });
        });

    }
}

