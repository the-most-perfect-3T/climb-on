/* 프로필탭에서 회원정보수정으로 이동 */
const btnModify = document.querySelector(".section-mypage #btn-modify");
const profileCont = document.querySelector(".section-mypage .profile-cont");
const modifyCont = document.querySelector(".section-mypage .modify-cont");
const btnProfileTab = document.querySelector(".section-mypage #profile-tab");

btnModify.addEventListener("click", function(){
    modifyCont.classList.add("active");
    profileCont.classList.remove("active");
});

btnProfileTab.addEventListener("click", function(){
    modifyCont.classList.remove("active");
    profileCont.classList.add("active");
});


// 회원정보수정 form 유효성 검증
const onSubmitHandlerForMypage = () => {
    let isError = false;
    let errorMessages = document.querySelectorAll(".error-message");
    let inputs = document.querySelectorAll("input");

    // 에러메시지가 있으면 에러상태
    for (const message of errorMessages) {
        if (message.textContent.trim() !== "") {
            isError = true;
            break;
        }
    }

    // 에러 상태이면
    if (isError) {
        alert("입력값에 문제가 있습니다. 다시 확인해주세요.");
        inputs.forEach(input => {
            input.value = '';
        });
        errorMessages.forEach(message => {
            message.textContent = "";
        });
        return false;
    }

    // 에러 X 중복확인 안했을 때
    let isCheck = false;
    let messageIndex = 0;
    let successMessages = document.querySelectorAll(".success-message");
    const nicknameExisting = document.getElementById("nicknameExisting") !== null ? document.getElementById("nicknameExisting") : null;
    const nickname = document.getElementById("nickname").value;

    // 기존값이 있으면
    if(nicknameExisting !== null){
        if(nicknameExisting.value !== nickname) {
            // 기존과 동일하지 않으면
            for (const message of successMessages) {
                if (message.textContent.trim() === "") {
                    // 성공 메시지가 없으면 중복확인 x
                    isCheck = true;
                    break;
                }
                messageIndex++;
            }

            // 중복확인 안했으면
            if (isCheck) {
                alert("닉네임 중복확인을 해주세요.");

                return false;
            }
        }else {
            // 기존과 동일하면
            return true;
        }
    }

    return true;
}


// 회원정보수정 - 프로필이미지 input file 대신 클릭
const btnModifyProfile = document.querySelector(".section-mypage #profile .modify-cont .btn-modify");
const profileFile = document.getElementById('profilePic');
const btnDeleteProfile = document.querySelector(".section-mypage #profile .modify-cont .btn-delete");

const profileImg = document.getElementById('profileImg');
const profileImgSrc = profileImg.getAttribute("src");

if (profileImgSrc.includes("/img/profile")) {
    btnDeleteProfile.removeAttribute("disabled");
} else {
    btnDeleteProfile.setAttribute("disabled", true);
}

btnModifyProfile.addEventListener("click", function(e){
    profileFile.click();
});

profileFile.addEventListener('change', function () {
    if (this.files.length > 0) {
        btnDeleteProfile.removeAttribute("disabled");

        const originalName = this.files[0].name;
        const ext = originalName.substring(originalName.lastIndexOf(".")).slice(1);
        const allowedType = ["jpeg", "jpg", "gif", "png"];

        if(allowedType.includes(ext.toLowerCase())){
            document.getElementById('profileForm').submit();
        }else {
            profileFile.value = "";
            return alert("지원하지 않는 형식입니다. 다시 업로드해주세요.");
        }

    } else {
        btnDeleteProfile.setAttribute("disabled", true);
    }

});


// 회원정보수정 - 비즈니스계정전환 checkbox 클릭시 disabled 해제
const inputDeleteAgree1 = document.getElementById("agreeCheck1") !== null ? document.getElementById("agreeCheck1") : null;
const btnApply = document.querySelector(".section-mypage #profile .modify-cont .btn-apply") !== null ? document.querySelector(".section-mypage #profile .modify-cont .btn-apply") : null;
if(inputDeleteAgree1 !== null ){
    inputDeleteAgree1.addEventListener("change", function(){
        const is_checked = this.checked;
        if(is_checked){
            btnApply.removeAttribute("disabled");
        }else {
            btnApply.setAttribute("disabled", true);
        }
    });
}



// 회원정보수정 - 계정삭제 checkbox 클릭시 disabled 해제
const inputDeleteAgree = document.getElementById("agreeCheck") !== null ? document.getElementById("agreeCheck") : null;
const btnWithdrawal = document.querySelector(".section-mypage #profile .modify-cont .btn-withdrawal") !== null ? document.querySelector(".section-mypage #profile .modify-cont .btn-withdrawal") : null;
if(inputDeleteAgree !== null){
    inputDeleteAgree.addEventListener("change", function(){
        const is_checked = this.checked;
        if(is_checked){
            btnWithdrawal.removeAttribute("disabled");
        }else {
            btnWithdrawal.setAttribute("disabled", true);
        }
    });
}


/* 즐겨찾기 */
async function checkFavorite(id) {
    let isFavorite = await getIsFavorite(id);
    return isFavorite;
}
async function toggleFavorite(id,isFavorite) {
    // 즐겨찾기 목록에서 해당 시설이 이미 존재하는지 확인

    try {
        const response = await updateFavoriteOnServer(id, !isFavorite); // 즐겨찾기 상태를 반전시켜서 서버에 보냄

        if (response.ok) {
            console.log('서버에 즐겨찾기 상태가 업데이트되었습니다.');
            const updatedFavoriteStatus = await checkFavorite(id);


        } else {

            console.error('서버에 즐겨찾기 상태 업데이트 실패');
        }
    } catch (error) {
        console.error('서버와의 통신 중 오류 발생:', error);
    }
}


async function updateFavoriteOnServer(id, addFavorite) {
    const url = '/facilities/update-favorite';
    const data = {
        facilityId: id,
        isFavorite: addFavorite,  // 즐겨찾기 추가 여부
    };

    const response = await fetch(url, {
        method: 'POST',  // POST 메소드로 요청
        headers: {
            'Content-Type': 'application/json',  // JSON 형식으로 전송
        },
        body: JSON.stringify(data),  // 데이터를 JSON 형식으로 변환하여 전송
    });

    return response;
}

async function getIsFavorite(id) {
    try {
        const response = await fetch(`/facilities/getIsFavorite?id=${id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error('서버 오류: ' + response.status);
        }

        const data = await response.json();
        console.log(data);// JSON 데이터를 파싱
        if (data !== null && data !== undefined) {
            return data;  // 데이터를 반환
        } else {
            console.log("즐겨찾기 정보가 없습니다.");
            return null;  // 데이터가 없으면 null 반환
        }
    } catch (error) {
        console.error('오류 발생:', error);
        return null;  // 오류 발생시 null 반환
    }
}

const favoriteTab = document.getElementById("favorite-tab");

/*favoriteTab.addEventListener("click", async function () {
    console.log("즐겨찾기 탭 클릭됨");

    try {
        const response = await fetch("/user/favorite");
        if (!response.ok) {
            throw new Error("서버 오류: " + response.status);
        }

        const data = await response.json();
        console.log("받은 데이터:", data);

        const favoriteList = document.getElementById("favorite").querySelector("ul.list");
        favoriteList.textContent = "";

        if (data.message === '저장된 즐겨찾기가 없습니다.') {
            const liItem = document.createElement("li");
            liItem.classList.add('no-result', 'border-top');
            liItem.textContent = "저장된 즐겨찾기가 없습니다.";
            favoriteList.appendChild(liItem);
        }

        const itemsPerPage = 4;
        let currentPage = 1;


        for (const item of data) {
            const isFavorite = await getIsFavorite(item.id);

            const liItem = document.createElement("li");
            liItem.classList.add("border");
            liItem.innerHTML = `
                <!--<a href="/facilities/select">-->
                    <div class="img-wrap border">
                        <img src="" alt="">
                    </div>
                    <a href="/facilities/select" class="name">${item.facilityName}</a>
                    <p class="address">${item.address}</p>
                    <button type="button" class="favorite-btn data-id="${item.id}" data-favorite="${isFavorite}">
                        <i class="fa-bookmark fa-solid"></i> 
                    </button>
                <!--</a>-->
            `;
            favoriteList.appendChild(liItem);
        }


        // 즐겨찾기 버튼 클릭 이벤트 설정
        favoriteList.addEventListener("click", async (event) => {
            const button = event.target.closest(".favorite-btn");
            if (button) {
                const facilityId = parseInt(button.getAttribute("data-id"));
                const currentFavoriteStatus = parseInt(button.getAttribute("data-favorite"));

                // 즐겨찾기 상태 토글
                await toggleFavorite(facilityId, currentFavoriteStatus);

                // UI 업데이트
                const updatedFavoriteStatus = await checkFavorite(facilityId);

                button.setAttribute("data-favorite", updatedFavoriteStatus);

                // i 태그 클래스 토글
                const icon = button.querySelector("i");
                if (icon) {
                    icon.classList.toggle("fa-solid", updatedFavoriteStatus === 1);
                    icon.classList.toggle("fa-regular", updatedFavoriteStatus === 0);
                }

                if(updatedFavoriteStatus === 0){
                    event.target.closest("li").remove();
                }

            }



        });
    } catch (error) {
        console.error("AJAX 오류:", error);
    }
});*/


favoriteTab.addEventListener("click", async function () {
    console.log("즐겨찾기 탭 클릭됨");

    try {
        const response = await fetch("/user/favorite");
        if (!response.ok) {
            throw new Error("서버 오류: " + response.status);
        }

        const data = await response.json();
        console.log("받은 데이터:", data);



        if (data.message === '저장된 즐겨찾기가 없습니다.') {
            const favoriteList = document.getElementById("favorite").querySelector("ul.list");
            const liItem = document.createElement("li");
            liItem.classList.add('no-result', 'border-top');
            liItem.textContent = "저장된 즐겨찾기가 없습니다.";
            favoriteList.appendChild(liItem);
        }

        const itemsPerPage = 4; // 한 페이지에 표시할 아이템 수
        let currentPage = 1;

        // 데이터 렌더링 함수
        const renderData = (page) => {
            const favoriteList = document.getElementById("favorite").querySelector("ul.list");
            favoriteList.textContent = ""; // 기존 리스트 초기화

            const startIndex = (page - 1) * itemsPerPage;
            const endIndex = startIndex + itemsPerPage;
            const itemsToShow = data.slice(startIndex, endIndex);

            itemsToShow.forEach((item) => {
                const liItem = document.createElement("li");
                liItem.classList.add("border");
                liItem.innerHTML = `
                    <div class="img-wrap border">
                        <img src="" alt="">
                    </div>
                    <a href="/facilities/select" class="name">${item.facilityName}</a>
                    <p class="address">${item.address}</p>
                    <button type="button" class="favorite-btn" data-id="${item.id}">
                        <i class="fa-bookmark fa-solid"></i>
                    </button>
                `;
                favoriteList.appendChild(liItem);
            });
        };

        // 페이지네이션 버튼 렌더링
        const renderPagination = () => {
            const pagination = document.querySelector(".pagination");
            pagination.textContent = "";

            const totalPages = Math.ceil(data.length / itemsPerPage);

            // 이전 버튼
            const prevButton = document.createElement("li");
            prevButton.className = `prev ${currentPage === 1 ? "disabled" : ""}`;
            prevButton.innerHTML = `<a href="#"><i class="fa-solid fa-circle-chevron-left"></i></a>`;
            prevButton.querySelector("a").addEventListener("click", (e) => {
                e.preventDefault();
                if (currentPage > 1) {
                    currentPage--;
                    renderData(currentPage);
                    renderPagination();
                }
            });
            pagination.appendChild(prevButton);

            // 페이지 번호 버튼
            for (let i = 1; i <= totalPages; i++) {
                const pageButton = document.createElement("li");
                pageButton.className = `num ${i === currentPage ? "current" : ""}`;
                pageButton.innerHTML = `<a href="#">${i}</a>`;
                pageButton.querySelector("a").addEventListener("click", (e) => {
                    e.preventDefault();
                    currentPage = i;
                    renderData(currentPage);
                    renderPagination();
                });
                pagination.appendChild(pageButton);
            }

            // 다음 버튼
            const nextButton = document.createElement("li");
            nextButton.className = `next ${currentPage === totalPages ? "disabled" : ""}`;
            nextButton.innerHTML = `<a href="#"><i class="fa-solid fa-circle-chevron-right"></i></a>`;
            nextButton.querySelector("a").addEventListener("click", (e) => {
                e.preventDefault();
                if (currentPage < totalPages) {
                    currentPage++;
                    renderData(currentPage);
                    renderPagination();
                }
            });
            pagination.appendChild(nextButton);
        };

        // 첫 페이지 렌더링
        if (Array.isArray(data)) renderData(currentPage);
        if(data.length > itemsPerPage) renderPagination();

        // 즐겨찾기 버튼 클릭 이벤트 설정
        const favoriteList = document.getElementById("favorite").querySelector("ul.list");
        favoriteList.addEventListener("click", async (event) => {
            const button = event.target.closest(".favorite-btn");
            if (button) {
                const facilityId = parseInt(button.getAttribute("data-id"));
                const currentFavoriteStatus = parseInt(button.getAttribute("data-favorite"));

                // 즐겨찾기 상태 토글
                await toggleFavorite(facilityId, currentFavoriteStatus);

                // UI 업데이트
                const updatedFavoriteStatus = await checkFavorite(facilityId);

                button.setAttribute("data-favorite", updatedFavoriteStatus);

                // i 태그 클래스 토글
               /* const icon = button.querySelector("i");
                if (icon) {
                    icon.classList.toggle("fa-solid", updatedFavoriteStatus === 1);
                    icon.classList.toggle("fa-regular", updatedFavoriteStatus === 0);
                }*/

                if(updatedFavoriteStatus === 0){
                    event.target.closest("li").remove();
                }

            }
        });
    } catch (error) {
        console.error("AJAX 오류:", error);
    }
});
