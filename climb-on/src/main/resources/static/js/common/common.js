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
        if (depth2) {
            depth2.style.height = '64px';
            depth2.style.opacity = '1';
        }
    });

    el.addEventListener("mouseleave", function() {
        const depth2 = this.querySelector(".depth2");
        if (depth2) {
            depth2.style.height = '0';
            depth2.style.opacity = '0';
        }
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
function darkMode(){


    const $html = document.querySelector('html');
    const $btnToggle = document.querySelector("#btnToggle");
    // 현재 모드를 가져옴
    let mode = $html.getAttribute('data-bs-theme');


    // 현재 모드와 반대되는 모드로 설정
    if( mode == 'dark' ){
        $html.setAttribute("data-bs-theme", "light");

        /*$btnToggle.textContent = "다크모드";*/
    }else{
        $html.setAttribute("data-bs-theme", "dark");
        /*$btnToggle.textContent = "라이트모드";*/
    }


}



