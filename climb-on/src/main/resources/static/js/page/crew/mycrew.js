const test = [28, 30, 32]; // 리스트 저장
// crew posts 에서 image가 포함되어 있는 애들을 어떻게 골라오지
// 포함된 애들만 리스트에 저장해서 여기다가 출력

test.forEach(function(a){
    console.log(a)
    const test1 = document.createElement('p'); // 해당 태그 ...에 저장 클래스 명에도 저장이 되나요
    test1.innerHTML = a;
    document.querySelector('#test').appendChild(test1);
});

const memberTabBtn = document.getElementById("member-tab");
memberTabBtn.addEventListener('click', async function() {
})

// 가져온 이벤트 활동 페이지에 뿌려주는 로직
function populateEventList(eventData) {
    const eventListContainer = document.getElementById('crew-activity-event-list');
    eventListContainer.innerHTML = '';

    eventData.forEach(event => {
        const eventItem = document.createElement('div');
        eventItem.className = 'crew-activity-event-item';
        eventItem.innerHTML = `
                                       <div class="crew-event-info">
                            <span class="crew-event-status-tag">예정</span>
                            <div class="crew-event-details">
                                <p class="crew-event-date">${new Date(event.start).toLocaleDateString()} · ${new Date(event.start).toLocaleTimeString([], {
            hour: '2-digit',
            minute: '2-digit'
        })}</p>
                                <p class="crew-event-location">서울숲클라이밍 종로점</p>
                            </div>
                          </div>
                          <div class="crew-event-center">
                            <p class="crew-event-title">${event.title}</p>
                          </div>
                          <div class="crew-event-right">
                            <button class="participate-btn">
                              <i class="fa-solid fa-child-reaching"></i><br/>
                              <span> 참여 + </span> <!-- 참여 눌렀을 때 링크테이블에 추가-->
                            </button>
                          </div>

        `;
        eventListContainer.appendChild(eventItem);
    })

}