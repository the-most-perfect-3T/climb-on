const btnModify = document.querySelector(".section-mypage #btn-modify");
const profileCont = document.querySelector(".section-mypage .profi le-cont");
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


// 프로필수정-계정삭제 checkbox 클릭시 disabled 해제
const inputDeleteAgree = document.getElementById("agreeCheck");
const btnWithdrawal = document.querySelector(".section-mypage #profile .modify-cont .btn-withdrawal");
inputDeleteAgree.addEventListener("change", function(){
   console.log(this);
   const is_checked = this.checked;
   if(is_checked){
       btnWithdrawal.removeAttribute("disabled");
   }else {
        btnWithdrawal.setAttribute("disabled", true);
   }
});