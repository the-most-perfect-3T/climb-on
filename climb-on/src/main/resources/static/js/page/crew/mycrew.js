// 백에서 넘어오는 크루코드 저장
const crewCodeTag = document.getElementById('crewcode');
const crewCode = crewCodeTag.getAttribute("data-crew-code");

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
                            <button class="participate-btn" id="crewParticipateButton" data-event-code="${event.id}" data-crew-code="${event.crewCode}">
                              <i class="fa-solid fa-child-reaching"></i><br/>
                              <span> 참여 + </span> <!-- 참여 눌렀을 때 링크테이블에 추가-->
                            </button>
                          </div>

        `;
        eventListContainer.appendChild(eventItem);
    })
    setParticipateBtnEvent();
}

const memberTabBtn = document.getElementById("member-tab");
memberTabBtn.addEventListener('click', async function() {
    try {
        const response = await fetch(`/mycrew/member/${crewCode}`);
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
            <td class="member-row" >
              <div class="position-relative align-items-center" style="width: 60px; height: 60px;">
                <div class="img-wrap d-flex align-items-center justify-content-center" style="width: 100%; height: 100%">
                  <img src="${member.profilePic}" alt="/images/logo.svg" class="w-100 userModalOpen" data-id=${member.id}>
                </div>
              </div>          
            </td>
            <td>
              <p class="mb-1 fw-bold userModalOpen" data-id="${member.id}">${member.nickname}</p>
            </td>   
            <td>
              <div class="badge rounded-pill" style="display: ${member.role == 'CAPTAIN'? "" : "none" }">크루장</div>
            </td>                     
            `
            memberListContainer.appendChild(memberTr);
        });
        await openUserModal();
    } catch (error) {
        console.error("AJAX 오류:", error);
    }
})

const albumTabBtn = document.getElementById("album-tab");
albumTabBtn.addEventListener('click', async function() {
    try {
        const response = await fetch(`/mycrew/album/${crewCode}`);
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

        if(data.length === 0){
            console.log(data.length);
            const div = document.createElement('div');
            div.innerText = "사진이 포함된 크루 게시글이 아직 없습니다.";
            gridContainer.appendChild(div);
        }
    } catch (error) {
        console.error("AJAX 오류:", error);
    }
});

function setParticipateBtnEvent (){
    const buttons = document.querySelectorAll("#crewParticipateButton")
    buttons.forEach(button => {
        button.addEventListener("click", async function()
        {
            const eventCode = this.getAttribute("data-event-code");
            const crewCode = this.getAttribute("data-crew-code");
            const userCode = 0;

            console.log("이벤트 코드(" + eventCode + ")랑 크루 코드(" + crewCode +")");

            const participationData = {
                eventCode: eventCode,
                crewCode: crewCode,
                userCode: userCode
            }
            try{
                const response = await fetch('/participate/crewevents',{
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(participationData)
                });
                if (response.ok) {
                    alert("참여가 완료되었습니다.");
                } else {
                    alert("참여에 실패했습니다.");
                }
            } catch (error) {
                console.error('Error:', error);
                alert("서버 오류가 발생했습니다.");
            }
        });
    })
}

/* 타유저 프로필 모달 */

async function openUserModal() {
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
}

const NoteTab= document.getElementById("news-tab");
NoteTab.addEventListener('click', async function setCrewPosts() {
    try {
        const response = await fetch(`/mycrew/posts/${crewCode}`);
        if (!response.ok) {
            throw new Error("서버 오류: " + response.status + " response = " + response);
        }
        const data = await response.json();
        const crewPostsContainer = document.getElementById("crew-posts-container");
        crewPostsContainer.innerHTML = "";

        data.forEach((post) => {
            const postItem = document.createElement('div');
            postItem.classList.add('post-container');
            postItem.innerHTML = `
                    <div class="post-header">
                        <div class="profile-picture" style="background-image: ${post.userProfilePic}"></div>
                        <div class="post-info">
                            <h3 class="author-name">${post.nickname}</h3>
                            <p class="post-time">${post.createdAt}</p>
                        </div>
                    </div>
                    <div class="post-content">
                        <p class="post-text">${post.content}</p>
                        <div class="post-images">
                            <img src="${post.imgUrl}" alt="Climbing Image 1">
                        </div>
                    </div>
                    <div class="post-footer">
                        <button class="like-button">좋아요</button>
                        <span class="comments-count">댓글 2</span>
                        <button class="share-button">공유하기</button>
                    </div>
            `
            crewPostsContainer.appendChild(postItem);
        });

    } catch (error) {
        console.log(error);
    }
});

const crewApplyBtn = document.querySelector('.crew-join-apply');
// 크루 가입신청 후

/*crewApplyBtn.display*/





/*========================크루 가입 모달========================*/

