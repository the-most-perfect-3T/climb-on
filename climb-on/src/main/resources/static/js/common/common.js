/* 공통 영역 js */

// Header scroll
window.addEventListener("scroll", function() {
    const header = document.querySelector(".header");
    header.style.left = 0 - window.scrollX + 'px';
});

// Header Nav - 1depth hover 시 2depth 보여주기
const depth1 = document.querySelectorAll('.depth1>li');

depth1.forEach(function(el) {
    el.addEventListener("mouseenter", function() {
        const depth2 = this.querySelector(".depth2");
        if (depth2) depth2.classList.add("active");
    });

    el.addEventListener("mouseleave", function() {
        const depth2 = this.querySelector(".depth2");
        if (depth2) depth2.classList.remove("active");
    });
});



// 다크모드
function darkMode() {

    const $html = document.querySelector('html');
    let isDarkMode = localStorage.getItem('isDarkMode') === 'true';

    // Bootstrap의 다크 모드 클래스를 추가하거나 제거하는 로직
    $html.setAttribute('data-bs-theme', isDarkMode ? 'dark' : 'light');

    // 다크 모드 토글 버튼 이벤트 처리
    const darkModeToggle = document.getElementById('btnToggle');
    darkModeToggle.addEventListener('click', () => {
        isDarkMode = !isDarkMode;
        localStorage.setItem('isDarkMode', isDarkMode);
        $html.setAttribute('data-bs-theme', isDarkMode ? 'dark' : 'light');
    });
}
darkMode();


// Header Nav active
const currentUrl = window.location.href;
const $header = document.querySelector("header");
const depth1Li = $header.querySelectorAll(".depth1 > li");
const depth2Li = $header.querySelectorAll(".depth2 > li");

function removeActive(el){
    el.forEach(function(el){
        el.classList.remove("active");
    });
}

if(currentUrl.includes("facilities")){
    removeActive(depth1Li);
    depth1Li[0].classList.add("active");
}else if(currentUrl.includes("community")){
    removeActive(depth1Li);
    depth1Li[1].classList.add("active");
}else if(currentUrl.includes("crew")){
    removeActive(depth1Li);
    depth1Li[2].classList.add("active");
    if(currentUrl.includes("home")){
        removeActive(depth2Li);
        depth2Li[0].classList.add("active");
    }else {
        removeActive(depth2Li);
        depth2Li[1].classList.add("active");
    }
}


/* 타유저 프로필 모달 */
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
            console.log("userId", userId);

            fetch(`/user/${userId}`) // 서버의 요청 URL
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`Error: ${response.status}`);
                    }
                    return response.json(); // JSON 데이터를 기대
                })
                .then(data => {
                    console.log("data", data);
                    // 서버에서 받은 데이터로 모달 내용 채우기
                    const userViewModal = document.getElementById("userViewModal");
                    console.log(userViewModal.querySelector(".top .img-wrap img"));
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

