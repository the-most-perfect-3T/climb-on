/* 비즈니스 전환신청 모달 */
// 시설 검색
const userApplyModalChoice = document.getElementById("userApplyModalChoice");
const inputSearch = userApplyModalChoice.querySelector("#facilityName");
inputSearch.addEventListener("input", function(){
    const wrapName = userApplyModalChoice.querySelector("#searchFacilities");
    showSuggestionsModal(wrapName);
});

function showSuggestionsModal(wrapName){
    const input = wrapName.querySelector('input[type="search"]');
    const inputValue = input.value;
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
                        item.onclick = () => selectSuggestion(facilities.facilityName, wrapName);
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
function selectSuggestion(facilities, wrapName) {

    const inputSearch = wrapName.querySelector('input[type="search"]');
    const btnSearch = wrapName.querySelector(".btn-search");
    const suggestionsDiv = wrapName.querySelector('.suggestions');

    wrapName.classList.add("active");
    btnSearch.style.display = 'none';
    inputSearch.value = facilities;
    suggestionsDiv.style.display = 'none';
}

const btnSearch = userApplyModalChoice.querySelector("#btnSearch");

btnSearch.addEventListener("click", function(){
    const wrapName =  userApplyModalChoice.querySelector("#searchFacilities");
    onClickFirstResult(wrapName);
});

// 검색버튼 클릭 시 첫번째 검색결과 선택 / 입력한게 없으면 알럿창
function onClickFirstResult(wrapName){

    const suggestionsDiv = wrapName.querySelector('.suggestions');
    const suggestionItem = suggestionsDiv.querySelectorAll(".suggestion-item");
    if(suggestionItem.length > 0){
        selectSuggestion(suggestionItem[0].textContent, wrapName);
    }

    if(inputSearch.value === ""){
        alert("검색할 업체명을 입력해주세요.");
    }
}


// 파일첨부 버튼 input file 대신 클릭
const btnFileUpload = userApplyModalChoice.querySelector("#userApplyModalChoice .btn-fileupload");
const businessFile = userApplyModalChoice.querySelector('#businessFile');

btnFileUpload.addEventListener("click", function(e){
    businessFile.click();
});


const attachFile = userApplyModalChoice.querySelector(".attachFile");
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



// 비즈니스 전환신청 - form 유효성 검증
function onSubmitHandlerApply (event){

    event.preventDefault();

    const wrapName = userApplyModalChoice.querySelector("#searchFacilities");
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



/* 홈짐 모달 */
const facilityModal = document.getElementById("facilityModal");
const list = facilityModal.querySelector("ul");
const btnMore = facilityModal.querySelector(".btn-more");

const inputSearch2 = facilityModal.querySelector("#facilityHomeName");
inputSearch2.addEventListener("input", function(){
    const wrapName = facilityModal.querySelector("#searchHomeFacilities");
    showList(wrapName);

});

// 모달 열렸을 때 전체 리스트 보여지기
facilityModal.addEventListener("show.bs.modal", function(){
    addList();
    btnMore.style.display = "none";
});

function addList(){


    // 전체목록 불러오는 api
    fetch("/facilities/selectList")
    .then(response => {
        if (!response.ok) {
            throw new Error('서버 오류: ' + response.status);
        }
        return response.json();
    })
    .then(data => {
        console.log(data);

        if (data && data.length > 0) {
            data.forEach(facilities => {

                const listItem = createListItem(facilities);
                list.appendChild(listItem);

            });

        } else {
            console.log('등록된 시설이 없습니다.');
        }

    })
    .catch(error => {
        console.error('AJAX 오류:', error);
    });
}

// 모달 닫혔을 때 input value  없애기
facilityModal.addEventListener("hidden.bs.modal", deleteInput);

function deleteInput(){
    const wrapName = facilityModal.querySelector("#searchHomeFacilities");
    const input = wrapName.querySelector('input[type="search"]');
    input.value = "";

    const noResultLi = list.querySelector("li.no-result");
    if(noResultLi){
        noResultLi.remove();
    }else {
        list.textContent = "";
    }
}


function showList(wrapName){
    const input = wrapName.querySelector('input[type="search"]');
    const inputValue = input.value;

    const btnSearch = wrapName.querySelector(".btn-search");
    btnSearch.style.display = 'block';
    wrapName.classList.remove("active");

    if (inputValue) {
        fetch(`/facilities/suggestions?code=${encodeURIComponent(inputValue)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('서버 오류: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                list.textContent = '';

                if (data && data.length > 0) {
                    const allData = data;
                    const defaultLength = 4;
                    let displayedItems = 0;

                    // 맨처음 4개만 출력
                    for(let i = 0; i < defaultLength; i++){
                        const listItem = createListItem(allData[i]);
                        list.appendChild(listItem);
                        displayedItems++; // 이미 추가한 항목
                    }

                    // 데이터가 4개 이상 시 &&  모두 표시되지 않았으면
                    if (allData.length > defaultLength && displayedItems < allData.length) {
                        btnMore.style.display = "block";
                    }


                    btnMore.addEventListener("click", function(){
                        const remainingItems = allData.length - displayedItems; // 데이터 중 남은 개수
                        const itemsToAdd = Math.min(defaultLength, remainingItems); // 추가할 개수 (기본 개수와 남은 개수 비교해서 작은걸로 추가)

                        for(let i = 0; i < itemsToAdd; i++){
                            const listItem = createListItem(allData[displayedItems + i]); // 보여진거 다음꺼부터 추가
                            list.appendChild(listItem);
                        }
                        // 보여진 개수 업데이트
                        displayedItems += itemsToAdd;


                        if(displayedItems >= allData.length){
                            this.style.display = "none";
                        }

                        // 스크롤 이동
                        list.scrollTop = list.scrollHeight;
                    });
                    
                    // 리스트를 선택하도록
                    selectHomeName();

                } else {

                    // '검색 결과 없습니다.' 보여주기
                    noResultListItem();

                    // 더보기 버튼 지우기
                    btnMore.style.display = "none";
                }

            })
            .catch(error => {
                console.error('AJAX 오류:', error);
                //list.style.display = 'none'; // 오류 발생 시 숨김
            });
    } else {

        const noResultLi = list.querySelector("li.no-result");
        if(noResultLi){
            noResultLi.remove();
        }else {
            list.textContent = "";
            btnMore.style.display = "none";
        }
        addList();

    }
}

// 검색결과가 없습니다 생성함수
function noResultListItem(){
    const listItem = document.createElement('li');
    listItem.classList.add("no-result");
    listItem.textContent = "검색 결과가 없습니다.";
    list.appendChild(listItem);
}

// 리스트 아이템 생성 함수
function createListItem(data) {
    const li = document.createElement('li');
    li.classList.add('d-flex', 'align-items-center', 'justify-content-between', 'border-bottom');

    const leftDiv = document.createElement('div');
    leftDiv.classList.add('left');
    const nameP = document.createElement('p');
    nameP.classList.add('name');
    nameP.textContent = data.facilityName;
    const addressP = document.createElement('p');
    addressP.classList.add('address');
    addressP.textContent = data.address;
    leftDiv.appendChild(nameP);
    leftDiv.appendChild(addressP);

    const rightDiv = document.createElement('div');
    rightDiv.classList.add('right');
    const imgWrap = document.createElement('div');
    imgWrap.classList.add('img-wrap');
    const img = document.createElement('img');
    /*img.src = data.imgSrc;*/
    imgWrap.appendChild(img);
    rightDiv.appendChild(imgWrap);

    li.appendChild(leftDiv);
    li.appendChild(rightDiv);

    return li;
}

facilityModal.addEventListener("shown.bs.modal", function(){
    selectHomeName();

});

function selectHomeName(){
    const listItems = list.querySelectorAll("li:not(.no-result)");

    listItems.forEach(function(el, i){

        el.addEventListener("click", function handleClick(){
            console.log("click 요청날림");
            const p = el.querySelector(".name");

            // 홈짐에 등록하는 요청 날리기
            fetch("/user/registFacility", {
                method: "post",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({facilityName: p.textContent})

            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('서버 오류: ' + response.status);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log(data);

                    const homeName = document.getElementById("homeName");
                    homeName.textContent = p.textContent;

                    alert(data.message);

                    const modalInstance = bootstrap.Modal.getInstance(facilityModal) || new bootstrap.Modal(facilityModal);
                    modalInstance.hide();

                })
                .catch(error => {
                    console.error('AJAX 오류:', error);
                });
            el.removeEventListener("click", handleClick);
        }, {once : true});


    });
}
