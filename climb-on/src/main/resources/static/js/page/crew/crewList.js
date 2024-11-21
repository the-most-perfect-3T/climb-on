

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 크루등록 모달~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
// 크루 사진 수정
const buttonForPicInput = document.getElementById('imgInsert');
const crewPicInput = document.getElementById('crewPicInput');

// 버튼 클릭시 숨겨진 input(type:file, display: none) 작동
buttonForPicInput.addEventListener("click",function (){
    crewPicInput.click();   // 가상의 클릭
});

// 유저가 이미지 파일 선택해서 "change" 발생시 해당 파일 읽어와서 base64(이미지 파일을 upload 없이 display 해주는 방식)로 만들어 img.src에 넣어줌
crewPicInput.addEventListener("change", function (){
    const file = crewPicInput.files[0];
    const reader = new FileReader();
    reader.onload = () => {
        const base64URL = reader.result;
        const img = document.getElementById('crewPic');
        img.setAttribute('src', base64URL);
    };
    reader.readAsDataURL(file);
});

let crewNameInput = document.getElementById('crewNameInput');
const crewNameBtn = document.getElementById('crewNameBtn');

crewNameInput.addEventListener('keyup', function (){
    let errorMessage = document.getElementById('crewName-error');
    let value = crewNameInput.value;
    if(value.length < 10){
        errorMessage.textContent = "";
    }else{
        errorMessage.textContent = "한글 1~10자 이내"
    }
});

crewNameBtn.addEventListener('click', function (){
    const crewName = crewNameInput.value;
    fetch(`/crew/checkCrewName?crewName=${crewName}`)
});

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







