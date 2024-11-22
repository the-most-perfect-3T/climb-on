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

// Header Nav active
/*depth1.forEach(function(el) {
    el.addEventListener("click", function(){
       depth1.forEach(item=>item.classList.remove('active'));
       this.classList.add("active");
    });
});*/


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


