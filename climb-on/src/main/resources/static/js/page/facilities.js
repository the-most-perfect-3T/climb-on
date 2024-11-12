
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
}
