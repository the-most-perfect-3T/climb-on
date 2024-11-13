// 이메일 유효성 검사
const emailValidCheck = event => {
    const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    let userId = document.getElementById("userId");
    let errorText = findNextTag(userId, "p");

    if(pattern.test(event.target.value)){
        errorText.textContent = " ";
    }else {
        errorText.textContent = "이메일 형식이 아닙니다.";
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


