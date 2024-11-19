/* 프로필탭에서 회원정보수정으로 이동 */
const btnModify = document.querySelector(".section-mypage #btn-modify");
const profileCont = document.querySelector(".section-mypage .profile-cont");
const modifyCont = document.querySelector(".section-mypage .modify-cont");
const btnProfileTab = document.querySelector(".section-mypage #profile-tab");

btnModify.addEventListener("click", function(){
    modifyCont.classList.add("active");
    profileCont.classList.remove("active");
});

btnProfileTab.addEventListener("click", function(){
    modifyCont.classList.remove("active");
    profileCont.classList.add("active");
});


// form 유효성 검증
const onSubmitHandlerForMypage = () => {
    let isError = false;
    let errorMessages = document.querySelectorAll(".error-message");
    let inputs = document.querySelectorAll("input");

    // 에러메시지가 있으면 에러상태
    for (const message of errorMessages) {
        if (message.textContent.trim() !== "") {
            isError = true;
            break;
        }
    }

    // 에러 상태이면
    if (isError) {
        alert("입력값에 문제가 있습니다. 다시 확인해주세요.");
        inputs.forEach(input => {
            input.value = '';
        });
        errorMessages.forEach(message => {
            message.textContent = "";
        });
        return false;
    }

    // 에러 X 중복확인 안했을 때
    let isCheck = false;
    let messageIndex = 0;
    let successMessages = document.querySelectorAll(".success-message");
    const nicknameExisting = document.getElementById("nicknameExisting") !== null ? document.getElementById("nicknameExisting") : null;
    const nickname = document.getElementById("nickname").value;

    // 기존값이 있으면
    if(nicknameExisting !== null){
        if(nicknameExisting.value !== nickname) {
            // 기존과 동일하지 않으면
            for (const message of successMessages) {
                if (message.textContent.trim() === "") {
                    // 성공 메시지가 없으면 중복확인 x
                    isCheck = true;
                    break;
                }
                messageIndex++;
            }

            // 중복확인 안했으면
            if (isCheck) {
                alert("닉네임 중복확인을 해주세요.");

                return false;
            }
        }else {
            // 기존과 동일하면
            return true;
        }
    }

    return true;
}


// 회원정보수정 - 프로필이미지 input file 대신 클릭
const btnModifyProfile = document.querySelector(".section-mypage #profile .modify-cont .btn-modify");
const profileFile = document.getElementById('profilePic');
const btnDeleteProfile = document.querySelector(".section-mypage #profile .modify-cont .btn-delete");

const profileImg = document.getElementById('profileImg');
const profileImgSrc = profileImg.getAttribute("src");

if (profileImgSrc.includes("/img/profile")) {
    btnDeleteProfile.removeAttribute("disabled");
} else {
    btnDeleteProfile.setAttribute("disabled", true);
}

btnModifyProfile.addEventListener("click", function(e){
    profileFile.click();
});

profileFile.addEventListener('change', function () {
    if (this.files.length > 0) {
        btnDeleteProfile.removeAttribute("disabled");
    } else {
        btnDeleteProfile.setAttribute("disabled", true);
    }

    if (this.files.length > 0) {
        document.getElementById('profileForm').submit();
    }
});


// 회원정보수정 - 비즈니스계정전환 checkbox 클릭시 disabled 해제
const inputDeleteAgree1 = document.getElementById("agreeCheck1");
const btnApply = document.querySelector(".section-mypage #profile .modify-cont .btn-apply");
inputDeleteAgree1.addEventListener("change", function(){
    const is_checked = this.checked;
    if(is_checked){
        btnApply.removeAttribute("disabled");
    }else {
        btnApply.setAttribute("disabled", true);
    }
});


// 회원정보수정 - 계정삭제 checkbox 클릭시 disabled 해제
const inputDeleteAgree = document.getElementById("agreeCheck");
const btnWithdrawal = document.querySelector(".section-mypage #profile .modify-cont .btn-withdrawal");
inputDeleteAgree.addEventListener("change", function(){
   const is_checked = this.checked;
   if(is_checked){
       btnWithdrawal.removeAttribute("disabled");
   }else {
        btnWithdrawal.setAttribute("disabled", true);
   }
});


// 회원정보수정 - 비즈니스 계정 파일첨부 버튼 input file 대신 클릭
const btnFileUpload = document.querySelector("#userApplyModalChoice .btn-fileupload");
const businessFile = document.getElementById('businessFile');

btnFileUpload.addEventListener("click", function(e){
    businessFile.click();
});

businessFile.addEventListener('change', function () {
    if (this.files.length > 0) {

        // 이미지인지 확인


        // 이미지이면
        //파일이름 추가

        /*document.getElementById("fileAttach").submit();*/
      /*  /!*document.getElementById('businessForm').submit();*!/
        fetch("/mypage/applyBusiness", {
            method: "POST",
            body: JSON.stringify({
                businessFile: businessFile
            })
        })
            .then(response => response.json())
            .then(data => console.log(data));*/
    }
});




//검색
let formName = document.getElementById("searchFacilities"); // formName 만 바꿔서 재활용

function showSuggestions2() {
    const inputSearch = formName.querySelector('input[type="search"]');
    const inputValue = inputSearch.value;
    const suggestionsDiv = formName.querySelector('.suggestions');
    const btnSearch = formName.querySelector(".btn-search");
    btnSearch.style.display = 'block';
    formName.classList.remove("active");

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
    const inputSearch = formName.querySelector('input[type="search"]');
    const btnSearch = formName.querySelector(".btn-search");
    const suggestionsDiv = formName.querySelector('.suggestions');

    formName.classList.add("active");
    btnSearch.style.display = 'none';
    inputSearch.value = facilities;
    suggestionsDiv.style.display = 'none';
}
const inputSearch = formName.querySelector('input[type="search"]');
const btnSearch = formName.querySelector(".btn-search");

// 엔터 누르면 검색버튼 클릭
inputSearch.addEventListener("keyup", function(event){
    if(event.key === 'Enter'){
        btnSearch.click();
    }
});

// 검색버튼 클릭 시 맨 첫번쨰 검색결과 선택
btnSearch.addEventListener("click", function(){
    const suggestionsDiv = formName.querySelector('.suggestions');
    const suggestionItem = suggestionsDiv.querySelectorAll(".suggestion-item");
    if(suggestionItem.length > 0){
        selectSuggestion(suggestionItem[0].textContent);
    }
});



