// 시설 검색
let wrapName = document.getElementById("searchFacilities"); // wrapName 만 바꿔서 재활용

function showSuggestions2() {
    const inputSearch = wrapName.querySelector('input[type="search"]');
    const inputValue = inputSearch.value;
    const suggestionsDiv = wrapName.querySelector('.suggestions');
    const btnSearch = wrapName.querySelector(".btn-search");
    btnSearch.style.display = 'block';
    wrapName.classList.remove("active");

    if (inputValue) {
        fetch(`/facilities/suggestions?code=${encodeURIComponent(inputValue)}`) // 패치요청 responseEntity 로 jason 받음
            .then(response => {                         // encodeURIComponent 안전하게하는것
                if (!response.ok) {
                    throw new Error('서버 오류: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                suggestionsDiv.textContent = '';

                if (data && data.length > 0) {
                    data.forEach(facilities => {
                        const item = document.createElement('div');
                        item.className = 'suggestion-item';
                        item.textContent = facilities.facilityName; // 메뉴 코드 표시
                        item.onclick = () => selectSuggestion(facilities.facilityName);
                        /*console.log(facilities.facilityName);*/
                        suggestionsDiv.appendChild(item);
                    });
                    suggestionsDiv.style.display = 'block'; // 추천 결과 표시
                } else {
                    /*suggestionsDiv.style.display = 'none'; // 추천 결과 숨김*/
                    const item = document.createElement('div');
                    item.className = 'no-result';
                    item.textContent = "검색 결과가 없습니다.";
                    suggestionsDiv.appendChild(item);
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

// 추천검색어 선택
function selectSuggestion(facilities) {
    const inputSearch = wrapName.querySelector('input[type="search"]');
    const btnSearch = wrapName.querySelector(".btn-search");
    const suggestionsDiv = wrapName.querySelector('.suggestions');

    wrapName.classList.add("active");
    btnSearch.style.display = 'none';
    inputSearch.value = facilities;
    suggestionsDiv.style.display = 'none';
}
const inputSearch = wrapName.querySelector('input[type="search"]');
const btnSearch = wrapName.querySelector(".btn-search");


// 엔터 누르면 검색버튼 클릭
inputSearch.addEventListener("keyup", function(event){
    if(event.key === 'Enter'){
        btnSearch.click();
    }
});


// 검색버튼 클릭 시 첫번째 검색결과 선택 / 입력한게 없으면 알럿창
btnSearch.addEventListener("click", function(){
    const suggestionsDiv = wrapName.querySelector('.suggestions');
    const suggestionItem = suggestionsDiv.querySelectorAll(".suggestion-item");
    if(suggestionItem.length > 0){
        selectSuggestion(suggestionItem[0].textContent);
    }

    if(inputSearch.value === ""){
        alert("검색할 업체명을 입력해주세요.");
    }
});



// 파일첨부 버튼 input file 대신 클릭
const btnFileUpload = document.querySelector("#userApplyModalChoice .btn-fileupload");
const businessFile = document.getElementById('businessFile');

btnFileUpload.addEventListener("click", function(e){
    businessFile.click();
});


const attachFile = document.querySelector(".attachFile");
const btnDelete = attachFile.querySelector("button");

// 사업자등록증 파일 선택 시
businessFile.addEventListener('change', function () {

    if (this.files.length > 0) {
        // 이미지인지 확인
        console.log("첨부한 이미지 이름 : " + this.files[0].name);
        const originalName = this.files[0].name;
        const ext = originalName.substring(originalName.lastIndexOf(".")).slice(1);
        const allowedType = ["jpeg", "jpg", "gif", "png"];

        if(allowedType.includes(ext.toLowerCase())){
            attachFile.classList.add("active");
            let fileName = attachFile.querySelector(".fileName");
            fileName.textContent = originalName;
            btnFileUpload.setAttribute("disabled", true);
        }else {
            businessFile.value = "";
            return alert("지원하지 않는 형식입니다. 다시 업로드해주세요.");
        }
    }
});

btnDelete.addEventListener("click", function(){
    const attachFile = document.querySelector(".attachFile");
    attachFile.classList.remove("active");
    btnFileUpload.removeAttribute("disabled");
    businessFile.value = "";
});



// form 유효성 검증
function onSubmitHandlerApply (event){

    event.preventDefault();

    if(wrapName.classList.contains("active") && attachFile.classList.contains("active")){
        // 시설명 입력 & 파일 선택
        console.log("다입력됐음", event.target);
        event.target.submit();

    }else if(!wrapName.classList.contains("active")){
        alert("업체명을 입력해주세요.");
    }else if(!attachFile.classList.contains("active")){
        alert("사업자등록증을 첨부해주세요.");
    }


}