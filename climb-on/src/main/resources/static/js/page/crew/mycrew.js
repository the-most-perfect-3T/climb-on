// document.querySelectorAll('.tab-item').forEach(tab => {
//     tab.addEventListener('click', function () {
//         // Remove active class from all tabs
//         document.querySelectorAll('.tab-item').forEach(item => item.classList.remove('active'));
//         // Add active class to clicked tab
//         this.classList.add('active');
//
//         // Hide all tab panels
//         document.querySelectorAll('.tab-panel').forEach(panel => panel.classList.remove('active'));
//         // Show the corresponding panel
//         const tabId = this.getAttribute('data-tab');
//         document.getElementById(tabId).classList.add('active');
//     });
// });

const pants = [28, 30, 32]; // 리스트 저장
// crew posts 에서 image가 포함되어 있는 애들을 어떻게 골라오지
// 포함된 애들만 리스트에 저장해서 여기다가 출력

pants.forEach(function(a){ // pants 라는 변수에 array 갯수만큼 안에 코드를 실행
    console.log(a) // a는 array 에 들어있는 변수를 추출
    const test1 = document.createElement('p'); // 해당 태그 ...에 저장 클래스 명에도 저장이 되나요
    test1.innerHTML = a;
    document.querySelector('#test').appendChild(test1);
});