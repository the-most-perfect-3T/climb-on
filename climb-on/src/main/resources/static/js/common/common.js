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
document.addEventListener('DOMContentLoaded', () => {
    const $html = document.querySelector('html');
    const isDarkMode = localStorage.getItem('isDarkMode') === 'true';
    $html.setAttribute('data-bs-theme', isDarkMode ? 'dark' : 'light');
});
function darkMode() {
    const $html = document.querySelector('html');
    const darkModeToggle = document.getElementById('btnToggle');

    // 초기 설정
    const isDarkMode = localStorage.getItem('isDarkMode') === 'true';
    $html.setAttribute('data-bs-theme', isDarkMode ? 'dark' : 'light');

    // 이벤트 리스너 추가
    if (darkModeToggle) {
        darkModeToggle.addEventListener('click', () => {
            const isDarkMode = localStorage.getItem('isDarkMode') === 'true';
            const newMode = !isDarkMode;
            localStorage.setItem('isDarkMode', newMode);
            $html.setAttribute('data-bs-theme', newMode ? 'dark' : 'light');
        });
    }

    // MutationObserver로 속성 유지
    const observer = new MutationObserver(() => {
        const isDarkMode = localStorage.getItem('isDarkMode') === 'true';
        const currentTheme = $html.getAttribute('data-bs-theme');
        const expectedTheme = isDarkMode ? 'dark' : 'light';
        if (currentTheme !== expectedTheme) {
            $html.setAttribute('data-bs-theme', expectedTheme);
        }
    });

    observer.observe($html, { attributes: true, attributeFilter: ['data-bs-theme'] });
}
darkMode();

/*function darkMode() {

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
darkMode();*/


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



