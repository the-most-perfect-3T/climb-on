let viewMode = 'list'; // 기본값을 리스트 뷰로 설정

//  URL에 포함해 페이지를 이동합니다.
function navigateWithViewMode(event, page){
    event.preventDefault(); // 기본 동작 방지
    const urlParams = new URLSearchParams(window.location.search);

    const currentCategory = urlParams.get('category') || '전체'; // 카테고리가 없으면 기본값 "전체"로 설정
    const currentViewMode = urlParams.get('viewMode') || 'list'; // 뷰모드 기본값을 리스트로 설정
    urlParams.set('page', page); //페이지 번호 설정
    urlParams.set('category', currentCategory); //카테고리 설정
    urlParams.set('viewMode', currentViewMode); //뷰모드 설정
    // 선택된 뷰 모드를 URL에 추가하여 페이지 이동
    window.location.href = `/community?${urlParams.toString()}`;
}

// 페이지가 로드될 때 URL에서 정렬 상태와 뷰 모드 상태를 추출하여 버튼 텍스트 업데이트
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);

    // LocalStorage에서 저장된 뷰모드 가져오기 //URL에서 현재 카테고리를 추출
    const storedViewMode = localStorage.getItem('viewMode');
    const currentSort = urlParams.get('sort') || 'latest'; // 기본값은 'latest'로 설정
    const currentCategory = urlParams.get('category') || '전체'; // 기본값은 '전체'
    // const currentViewMode = urlParams.get('viewMode') ||'list'; // URL, 기본값 'list' 순으로 설정
    viewMode = storedViewMode || urlParams.get('viewMode') || 'list'; // 저장된 값, URL, 기본값 순으로 설정

    updateSortButtonText(currentSort);
    setViewMode(viewMode); // 페이지 로드 시 현재 뷰 설정 (초기 뷰설정)
    loadPost();

    // 버튼 활성화 처리
    const buttons = document.querySelectorAll('.category-buttons button');
    buttons.forEach(button => {
        if (button.textContent.trim() === currentCategory) {
            button.classList.add('active');
        } else {
            button.classList.remove('active'); // 다른 버튼의 active 제거
        }
    });
    // if (!urlParams.get('category')){
    //     urlParams.set('category', '전체');
    //     window.location.search = urlParams.toString();
    // }  // 바로 커뮤니티 눌렀을 때 http://localhost:8080/community?category=전체&viewMode=list 로 이동 // 했으나 뷰리스트 고정 안되는건 계속되므로 삭제

});

function setActiveCategory(button, category){
    const buttons = document.querySelectorAll('.category-buttons button');
    buttons.forEach(btn => btn.classList.remove('active'));
    button.classList.add('active');

    // URL에 카테고리 반영
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('category', category);

    // 현재 상태를 유지하며 페이지 리로드
    window.location.search = urlParams.toString();
}

// 정렬 버튼 텍스트를 업데이트하는 함수
function updateSortButtonText(order) {
    const sortButton = document.querySelector('#sortButton .sort-text');
    switch (order) {
        case 'latest':
            sortButton.textContent = '최신순';
            break;
        case 'comments':
            sortButton.textContent = '댓글순';
            break;
        case 'views':
            sortButton.textContent = '조회순';
            break;
        case 'hearts':
            sortButton.textContent = '좋아요순';
            break;
        default:
            sortButton.textContent = '최신순';
    }
}

// 정렬 버튼 클릭 시 호출되는 함수
function sortPosts(order) {
    updateSortButtonText(order); // 버튼 텍스트 업데이트

    // 정렬 조건을 URL에 추가하여 서버에 요청
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('sort', order);

    // JavaScript에서 정렬 및 기타 URL 조작 시에도 category와 searchKeyword를 유지
    const category = urlParams.get('category') || '전체';
    const searchKeyword = urlParams.get('searchKeyword') || '';
    const viewMode = urlParams.get('viewMode') || 'list';
    urlParams.set('category', category);
    urlParams.set('searchKeyword', searchKeyword);
    urlParams.set('viewMode', viewMode);
    window.location.search = urlParams.toString();
}


// 뷰 전환 스크립트 // 뷰 모드를 설정하고 URL 파라미터에 반영하는 함수 (URL 갱신하여 고정)

function setViewMode(mode) {
    viewMode = mode || 'list'; // 기본값 설정
    localStorage.setItem('viewMode', viewMode); // LocalStorage에 뷰모드 상태 저장
    renderPosts();

    // URL에 viewMode 파라미터를 추가하여 현재 모드를 반영
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('viewMode', viewMode);
    //history : 브라우저의 히스토리 API를 활용하여 브라우저의 탐색 기록을 조작할 수 있는 객체
    history.replaceState(null, '', `${location.pathname}?${urlParams.toString()}`); // 이걸 사용하면 브라우저 기록을 변경하거나 새로운 상태를 추가하지 않고, 현재 페이지의 URL을 수정하는데 사용된다! (뷰모드에 많이 쓰인다.)
    //첫 번째 파라미터 : 상태 객체(null) : 현재 상태와 연결할 데이터를 저장할 수 있다. 현재사용x
    //두 번째 파라미터:제목(''): 업데이트된 URL과 연결된 페이지의 제목입니다. 이 예시에서는 빈 문자열로 설정되어 무시된다.
    //세 번째 파라미터:URL(~): 브라우저의 URL표시줄에 나타날 새로운 URL이다. 페이지를 새로고침하지 않고 URL만 변경된다. cf) location.pathname:현재 페이지의 경로(쿼리 문자열 제외)., urlParams.toString(): 수정된 쿼리 문자열
    // 브라우저의 히스토리 기록을 변경하지 않고, 현재 페이지의 URL을 업데이트한다.
    // 버튼 스타일 업데이트 (선택된 버튼에 active 클래스 추가)
    document.getElementById('cardViewButton').classList.toggle('active', mode === 'card');
    document.getElementById('listViewButton').classList.toggle('active', mode === 'list');
}

function renderPosts() { // 뷰 모드에 따라 표시되는 목록 전환
    const listView = document.querySelector('.list-view');
    const cardView = document.querySelector('.card-container');

    // 뷰 모드에 따라 표시되는 목록 전환
    if (viewMode === 'card') {
        listView.style.display = 'none';
        cardView.style.display = 'flex';
    } else {
        listView.style.display = 'table';
        cardView.style.display = 'none';
    }
}

document.getElementById("writePostBtn").addEventListener("click", () => {
    writePost();
})

async function writePost(){
    try {
        const response = await fetch('/crew/checkHasCrew');
        if(!response.ok){
            throw new Error("서버에러");
        }
        const message = await response.text();
        if(message.includes("소속된 크루X")){
            alert("소속된 크루가 있어야만 게시글 작성이 가능합니다.");
        }
    } catch (error){
        console.error(error);
        alert(error);
    }
    window.location.href = "/crew/writepost";
}

async function loadPost(){
    try{
        const res = await fetch('/events/allevent');
        if(!res.ok){
            throw new Error("크루 일정을 가져오지 못했습니다." + res);
        }
        const contentType = res.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
            const data = await res.json();
            console.log(data);
            populateAllCrewEvents(data);
        } else {
            throw new Error("Received non-JSON response.");
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// 가져온 이벤트 활동 페이지에 뿌려주는 로직
function populateAllCrewEvents(eventData) {
    const eventListContainer = document.getElementById('crew-activity-event-list');
    eventListContainer.innerHTML = '';

    if(eventData.length > 0)
    {
        for(let i =0; i < eventData.length; i++) {
            if(i === 3) break;
            console.log("이벤트 시작: " + i);
            const eventItem = document.createElement('div');
            eventItem.className = 'crew-activity-event-item';
            eventItem.innerHTML = `
               <div class="crew-event-info">
                   <span class="crew-event-status-tag">예정</span> <!--start 시점이랑 오늘 date 비교해서 예정, 진행중으로 보여지게 만들어야 됨-->
                   <div class="crew-event-details">
                       <p class="crew-event-date">${new Date(eventData[i].start).toLocaleDateString()} · ${new Date(eventData[i].start).toLocaleTimeString([], {
                           hour: '2-digit', minute: '2-digit'
                       })}</p>
                   </div>
               </div>
               <div class="crew-event-center">
                   <p class="crew-event-title">${eventData[i].title}</p>
               </div>
               <div class="crew-event-right">
                   <p class="crew-event-location">서울숲클라이밍 종로점</p> <!--eventData[i].location 추가해서 여기-->
                   <p class="crew-event-crewname">크루명</p>
                   <div class="crew-event-image-placeholder"></div>
               </div>`;
            eventListContainer.appendChild(eventItem);
        }
    }
}