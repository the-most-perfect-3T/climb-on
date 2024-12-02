// 백에서 넘어오는 크루코드 저장
const crewCodeTag = document.getElementById('crewcode');
const crewCode = crewCodeTag.getAttribute("data-crew-code");

function createEventInfo(eventContainer, event) {
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
                                <p class="crew-event-location">${event.location}</p>
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
    eventContainer.appendChild(eventItem);
}

function popluateMainEventInMycrewHome(eventData){
    const mainEventContainer = document.getElementById("crewRecentEvent");
    if(mainEventContainer != null)
    {
        mainEventContainer.innerHTML = '';
        createEventInfo(mainEventContainer, eventData[0]);
        setParticipateBtnEvent();
    }
}
// 가져온 이벤트 활동 페이지에 뿌려주는 로직
function populateEventList(eventData, condition) {
    const eventListContainer = document.getElementById('crew-activity-event-list');
    eventListContainer.innerHTML = '';
    if(eventData != null)
    {
        eventData.forEach(event => {
            createEventInfo(eventListContainer, event)
        })
        if(condition === true){
            setParticipateBtnEvent();
        }
        else
        {
            const buttons = document.querySelectorAll("#crewParticipateButton")
            buttons.forEach(button => {
                button.removeEventListener("click", setAuthorityToJoin);
            });
        }
    }
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

        if(data.crewApplys != null){
            data.crewApplys.forEach((newApply) => {
            const memberTr = document.createElement('tr');
            memberTr.classList.add('border-bottom');
            memberTr.innerHTML = `
            <td class="member-row">
              <div class="position-relative align-items-center" style="width: 60px; height: 60px;">
                <div class="img-wrap d-flex align-items-center justify-content-center" style="width: 100%; height: 100%">
                  <img src="${newApply.profilePic}" alt="/images/logo.svg" class="w-100 userModalOpen" data-id=${newApply.userCode}>
                </div>
              </div>          
            </td>
            <td>
              <p class="mb-1 fw-bold userModalOpen" data-id="${newApply.userCode}">${newApply.nickname}</p>
            </td>   
            <td>
              <div class="badge rounded-pill">NEW</div>
            </td>
            <td>
              <div class="text-center">
                <button type="button" class="btn px-4 py-2 fw-bold" id="crew-apply-confirm-modal-open" 
                style="display: ${newApply.isPermission === false? "" : "none"}"
                data-id="${newApply.userCode}">
                가입확인
                </button>
                <button type="button" class="btn px-5 py-2 fw-bold" id="crew-apply-confirm-modal-open" 
                style="display: ${newApply.isPermission === true? "" : "none"}"
                data-id="${newApply.userCode}">
                가입승인/거절
                </button>
              </div>
            </td>                     
            `
                memberListContainer.appendChild(memberTr);
            });

            data.memberList.forEach((member) => {
                const memberTr = document.createElement('tr');
                memberTr.classList.add('border-bottom');
                memberTr.innerHTML = `
            <td class="member-row">
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
              <div class="badge rounded-pill" style="display: ${member.role === 'CAPTAIN'? "" : "none" }">크루장</div>
            </td>                     
            `
                memberListContainer.appendChild(memberTr);
            });
            await openUserModal();
            await openCrewApplyConfirmModal();
        }
        else{
            data.forEach((member) => {
                const memberTr = document.createElement('tr');
                memberTr.classList.add('border-bottom');
                memberTr.innerHTML = `
            <td class="member-row">
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
        }
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
        button.addEventListener("click", setAuthorityToJoin);
    })
}

async function setAuthorityToJoin()
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

                fetch(`/user/${userId}`) // 서버의 요청 URL
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`Error: ${response.status}`);
                        }
                        return response.json(); // JSON 데이터를 기대
                    })
                    .then(data => {
                        // 서버에서 받은 데이터로 모달 내용 채우기
                        const userViewModal = document.getElementById("userViewModal");
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
            console.log(post);
            console.log(post.userProfilePic);
            console.log(post.nickname);
            postItem.classList.add('post-container');
            postItem.innerHTML = `
                    <div class="post-header">
                        <div class="profile-picture userModalOpen" data-id="${post.userId}">                           
                            <img src="${post.userProfilePic}" alt="userProfileImg" class="profile-img"/>
                        </div>
                        <div class="post-info">
                            <h3 class="author-name userModalOpen" data-id="${post.userId}">${post.userNickname}</h3>
                            <p class="post-time">${(post.createdAt).split("T",1)}</p>
                        </div>
                    </div>
                    <div class="post-content">
                        <a href="/crew/post/${post.id}">
                            <div class="post-text">${post.content}</div>
                            <div class="post-images" id="images-${post.id}"></div> <!-- Container for images -->
                        </a>                                 
                    </div>
                    <div class="post-footer">
                    <div class = like-and-comment>
                        <button class="like-button">
                            <i class="fa-regular fa-heart"></i>
                            좋아요
                        </button>
                        <button class="comments-count">
                            <i class="fa-solid fa-comment-dots"></i>
                            댓글 ${post.commentsCount}
                        </button>
                    </div>   
                        <button class="share-button">
                            <i class="fa-solid fa-share-nodes"></i>
                            공유하기
                        </button>
                    </div>
            `
            crewPostsContainer.appendChild(postItem);
            if (post.imgUrl) {
                const imageContainer = document.getElementById(`images-${post.id}`);
                const imgUrls = post.imgUrl.split(',');

                imgUrls.forEach(url => {
                    const img = document.createElement('img');
                    img.src = url.trim();  // Remove any extra spaces
                    img.alt = 'Post Image';
                    img.className = 'post-image';  // Add a class for styling

                    imageContainer.appendChild(img);  // Append each image to the container
                });
            }
        });
        await openUserModal();

    } catch (error) {
        console.log(error);
    }
});

function splitImages(imageArray)
{

}

const crewApplyBtn = document.querySelector('.crew-join-apply');
// 크루 가입신청 후

/*crewApplyBtn.display*/





/*========================크루 가입 모달========================*/

/*========================크루 가입 확인 모달========================*/
async function openCrewApplyConfirmModal() {
    const crewApplyConfirmModalOpen = document.querySelectorAll("#crew-apply-confirm-modal-open");
    crewApplyConfirmModalOpen.forEach(function (el){
        el.addEventListener("click", function (e){
            const userId = parseInt(e.target.getAttribute("data-id"));
            fetch(`/mycrew/member/newApply/${userId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`Error: ${response.status}`);
                    }
                    return response.json(); // JSON 데이터를 기대
                })
                .then(data => {
                    const crewApplyConfirmModal = document.getElementById("crew-apply-confirm-modal");
                    crewApplyConfirmModal.querySelector(".modal-body .left .name").textContent = data.nickname;
                    crewApplyConfirmModal.querySelector(".modal-body .crewApplyContent").textContent = data.content;
                    crewApplyConfirmModal.querySelector(".modal-body #input").value = data.userCode;

                    if(data.isPermission === true){
                        crewApplyConfirmModal.querySelector(".modal-body .btn-confirm").style.display = "none";
                    }else if(data.isPermission === false){
                        crewApplyConfirmModal.querySelector(".modal-body .btn-approval").style.display = "none";
                        crewApplyConfirmModal.querySelector(".modal-body .btn-refusal").style.display = "none";
                    }

                    const modal = new bootstrap.Modal(document.getElementById('crew-apply-confirm-modal'));
                    modal.show();
                })
                .catch(error => {
                    console.error("에러 발생:", error);
                });
        })
    })
    /*
    const crewApplyConfirmModalOpen = document.getElementById("crew-apply-confirm-modal-open");
    crewApplyConfirmModalOpen.addEventListener("click", function (e){
        const userId = parseInt(e.target.getAttribute("data-id"));
        fetch(`/mycrew/member/newApply/${userId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error: ${response.status}`);
                }
                return response.json(); // JSON 데이터를 기대
            })
            .then(data => {
                const crewApplyConfirmModal = document.getElementById("crew-apply-confirm-modal");
                crewApplyConfirmModal.querySelector(".modal-body .left .name").textContent = data.nickname;
                crewApplyConfirmModal.querySelector(".modal-body .crewApplyContent").textContent = data.content;
                crewApplyConfirmModal.querySelector(".modal-body #input").value = data.userCode;
                console.log(data.userCode);
                console.log(data.nickname);
                console.log(data.content);
                const modal = new bootstrap.Modal(document.getElementById('crew-apply-confirm-modal'));
                modal.show();
            })
            .catch(error => {
                console.error("에러 발생:", error);
            });
    })*/

}
