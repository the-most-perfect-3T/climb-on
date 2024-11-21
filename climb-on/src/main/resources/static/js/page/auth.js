// 이메일 유효성 검사
const emailValidCheck = event => {
    const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    let userId = document.getElementById("userId");
    let errorMessage = findNextTag(userId, "p");

    if(pattern.test(event.target.value)){
        errorMessage.textContent = " ";
    }else {
        errorMessage.textContent = "이메일 형식이 아닙니다.";
    }

    // 지울 때  - 성공메시지 지움
    /*const eventKey = ["Backspace", "Delete", "ArrowUp", "ArrowDown", "ArrowLeft", "ArrowRight"];
    const userIdError = document.getElementById("userId-error");
    if (eventKey.includes(event.key) || userIdError.textContent !== "") {
        let targetElement = document.getElementById("userId-success") !== null ? document.getElementById("userId-success") : null;
        if (targetElement) {
            targetElement.textContent = ''; // textContent를 비움
        }
        console.log(event.key, targetElement?.textContent);
    }*/
    let targetElement = document.getElementById("userId-success") !== null ? document.getElementById("userId-success") : null;
    if (targetElement) {
        targetElement.textContent = ''; // textContent를 비움
    }
}

// 다음 요소 찾기
function findNextTag(element, tagName) {
    let nextElement = element.nextElementSibling;

    while (nextElement) {
        if (nextElement.tagName === tagName.toUpperCase()) {
            return nextElement;
        }
        nextElement = nextElement.nextElementSibling;
    }

    return null;
}

// 비밀번호 유효성 검사
const passwordValidCheck = event => {
    let passwordInput = document.getElementById("password");
    let errorMessage = findNextTag(passwordInput, "p");
    let password = passwordInput.value;

    if (password.length < 6) {
        errorMessage.textContent = "비밀번호는 6자 이상이어야 합니다.";
    } else if (password.length > 20) {
        errorMessage.textContent = "비밀번호는 20자를 초과할 수 없습니다.";
    } else {
        const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,20}$/;
        if (!passwordPattern.test(password)) {
            errorMessage.textContent = "비밀번호에는 영문, 숫자, 특수문자가 포함되어야 합니다.";
        } else {
            errorMessage.textContent = "";
        }
    }

}

// 비밀번호 확인
const passwordCompareCheck = event => {
    let passwordInput = document.getElementById("password");
    let passwordInput2 = document.getElementById("passwordCheck");
    let password = passwordInput.value;
    let passwordChk = passwordInput2.value;
    let errorMessage = findNextTag(passwordInput2, "p");

    if(passwordChk !== password){
        errorMessage.textContent = "비밀번호가 일치하지 않습니다.";
    }else {
        errorMessage.textContent = "";
    }

    password = null;
    passwordChk = null;
}

// 닉네임 유효성 검사
const nicknameValidCheck = event => {
    let nicknameInput = document.getElementById("nickname");
    let nickname = nicknameInput.value;
    let errorMessage = findNextTag(nicknameInput, "p");

    const namePattern = /^[가-힣A-Za-z0-9]{1,20}$/;

    if(namePattern.test(nickname)){
        errorMessage.textContent = "";
    }else {
        errorMessage.textContent = "한글 1~10자, 영문 2~20자 이내, 특수문자 불가";
    }

    // 지울 때  - 성공메시지 지움
    let targetElement = document.getElementById("nickname-success") !== null ? document.getElementById("nickname-success") : null;
    if (targetElement) {
        targetElement.textContent = '';
    }
}



// form 유효성 검증
const onSubmitHandler = () => {

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
    let checkIndex = "";
    for(const message of successMessages){
        if (message.textContent.trim() === "") {
            // 성공 메시지가 없으면 중복확인 x
            isCheck = true;
            break;
        }
        messageIndex++;
    }

    // 중복확인 안했으면
    if(isCheck){
        if(messageIndex === 0){
            checkIndex = "아이디";
        }else {
            checkIndex = "닉네임";
        }

        alert(checkIndex + " 중복확인을 해주세요.");

        return false;
    }

    return true;

}

// 아이디 중복확인 요청보내기
const checkId = () => {
    const userId = document.getElementById("userId").value;
    fetch(`/auth/checkUserId?userId=${userId}`)
        .then(response => response.text())
        .then(message => {
            document.getElementById("userId-success").innerText = message.includes("사용 가능") ? message : "";
            message.includes("중복") ? alert(message) : "";
            message.includes("유효") ? alert(message) : "";
        })
        .catch(error => console.error("Error:", error));
}

// 닉네임 중복확인 요청보내기
const checkName = () => {

    const nicknameExisting = document.getElementById("nicknameExisting") !== null ? document.getElementById("nicknameExisting") : null;
    const nickname = document.getElementById("nickname").value;
    if(nicknameExisting !== null){
        // 기존값(히든)이 있으면
        if(nicknameExisting.value !== nickname){
            // 기존값(히든)과 닉네임 일치하지 않을 때
            fetch(`/auth/checkName?nickname=${nickname}`)
                .then(response => response.text())
                .then(message => {
                    document.getElementById("nickname-success").innerText = message.includes("사용 가능") ? message : "";
                    message.includes("중복") ? alert(message) : "";
                    message.includes("유효") ? alert(message) : "";
                })
                .catch(error => console.error("Error:", error));
        }
    }else {
        // 기존값(히든)이 없을 때
        fetch(`/auth/checkName?nickname=${nickname}`)
            .then(response => response.text())
            .then(message => {
                document.getElementById("nickname-success").innerText = message.includes("사용 가능") ? message : "";
                message.includes("중복") ? alert(message) : "";
                message.includes("유효") ? alert(message) : "";
            })
            .catch(error => console.error("Error:", error));
    }

}