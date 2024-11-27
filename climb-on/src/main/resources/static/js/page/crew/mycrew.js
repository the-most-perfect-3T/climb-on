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

const test = [28, 30, 32]; // 리스트 저장
// crew posts 에서 image가 포함되어 있는 애들을 어떻게 골라오지
// 포함된 애들만 리스트에 저장해서 여기다가 출력

test.forEach(function(a){ // pants 라는 변수에 array 갯수만큼 안에 코드를 실행
    console.log(a) // a는 array 에 들어있는 변수를 추출
    const test1 = document.createElement('p'); // 해당 태그 ...에 저장 클래스 명에도 저장이 되나요
    test1.innerHTML = a;
    document.querySelector('#test').appendChild(test1);
});


const memberTabBtn = document.getElementById("member-tab");
memberTabBtn.addEventListener('click', async function() {
    try {
        const response = await fetch("/mycrew/member");
        if (!response.ok) {
            throw new Error("서버 오류: " + response.status);
        }
        const data = await response.json();
        const memberListContainer = document.getElementById("memberList-container");
        memberListContainer.innerHTML = "";

        data.forEach((member) => {
            const memberTr = document.createElement('tr');
            memberTr.classList.add('border-bottom');
            memberTr.innerHTML = `
            <td class="d-flex flex-column gap-2" >
              <div class="position-relative align-items-center" style="width: 60px; height: 60px;">
                <div class="img-wrap d-flex align-items-center justify-content-center" style="width: 100%; height: 100%">
                  <img src="${member.profilePic}" alt="/images/logo.svg" class="w-100">
                </div>
              </div>          
            </td>
            <td>
              <p class="mb-1 fw-bold">${member.nickname}</p>
            </td>   
            <td>
              <div class="badge rounded-pill" style="display: ${member.role == 'CAPTAIN'? "" : "none" }">크루장</div>
            </td>                     
            `
            memberListContainer.appendChild(memberTr);
        });
    } catch (error) {
        console.error("AJAX 오류:", error);
    }
})

const albumTabBtn = document.getElementById("album-tab");
albumTabBtn.addEventListener('click', async function() {
    try {
        const response = await fetch("/mycrew/album");
        if (!response.ok) {
            throw new Error("서버 오류: " + response.status);
        }
        const data = await response.json();
        const gridContainer = document.querySelector('.grid-container');
        gridContainer.innerHTML = "";

        data.forEach((img) => {
            const gridItem = document.createElement('div');
            gridItem.classList.add('grid-item');
            gridItem.innerHTML = `
            <img src="${img}" alt="/images/logo.svg">          
            `
            gridContainer.appendChild(gridItem);
        })
    } catch (error) {
        console.error("AJAX 오류:", error);
    }
});