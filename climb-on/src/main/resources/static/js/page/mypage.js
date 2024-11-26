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

/*페이지네이션 없이..*/
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
                    <button type="button" class='favorite-btn' data-id="${item.id}" data-favorite="${isFavorite}">
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

                console.log(facilityId);
                // UI 업데이트
                const updatedFavoriteStatus = await checkFavorite(facilityId);
                console.log("update", updatedFavoriteStatus);

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
        const favoriteList = document.getElementById("favorite").querySelector("ul.list");
        favoriteList.textContent = "";

        if (data.message === '저장된 즐겨찾기가 없습니다.' || data.length === 0) {
            const liItem = document.createElement("li");
            liItem.classList.add('no-result', 'border-top');
            liItem.textContent = "저장된 즐겨찾기가 없습니다.";
            favoriteList.appendChild(liItem);
        }else {
            const itemsPerPage = 4; // 한 페이지에 표시할 아이템 수
            let currentPage = 1;

            // 데이터 렌더링 함수
            const renderData = async (page) => {
                const favoriteList = document.getElementById("favorite").querySelector("ul.list");
                favoriteList.textContent = ""; // 기존 리스트 초기화

                const startIndex = (page - 1) * itemsPerPage;
                const endIndex = startIndex + itemsPerPage;
                const itemsToShow = data.slice(startIndex, endIndex);

                if (itemsToShow.length === 0) {
                    console.log("렌더링할 데이터가 비어 있음");
                    const liItem = document.createElement("li");
                    liItem.classList.add('no-result', 'border-top');
                    liItem.textContent = "저장된 즐겨찾기가 없습니다.";
                    favoriteList.appendChild(liItem);
                    document.querySelector("#favorite .pagination").textContent = "";
                    return; // 데이터가 없으면 이후 렌더링 중단
                }

                for (const item of itemsToShow) {
                    const liItem = document.createElement("li");
                    const isFavorite = await getIsFavorite(item.id);
                    liItem.classList.add("border");
                    liItem.innerHTML = `
                    <div class="img-wrap border">
                        <img src="${item.imageUrl != null ? item.imageUrl : " "}" alt="시설 이미지">
                    </div>
                    <a href="/facilities/select" class="name">${item.facilityName}</a>
                    <p class="address">${item.address}</p>
                    <button type="button" class="favorite-btn" data-id="${item.id}" data-favorite="${isFavorite}">
                        <i class="fa-bookmark fa-solid"></i>
                    </button>
                `;
                    favoriteList.appendChild(liItem);
                }
            };

            // 페이지네이션 버튼 렌더링
            const renderPagination = () => {
                console.log('페이지네이션 렌더링');
                const pagination = document.querySelector("#favorite .pagination");
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
                console.log(prevButton);

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
            if (Array.isArray(data)) await renderData(currentPage);
            if(data.length > itemsPerPage) renderPagination();

            // 즐겨찾기 버튼 클릭 이벤트 설정
            const favoriteList = document.getElementById("favorite").querySelector("ul.list");
            favoriteList.addEventListener("click", async (event) => {
                const button = event.target.closest(".favorite-btn");
                if (button) {
                    button.disabled = true;
                    try{
                        const facilityId = parseInt(button.getAttribute("data-id"));
                        const currentFavoriteStatus = parseInt(button.getAttribute("data-favorite"));
                        if (isNaN(currentFavoriteStatus)) {
                            console.error("Invalid data-favorite value:", button.getAttribute("data-favorite"));
                            return;
                        }

                        console.log(facilityId, currentFavoriteStatus);

                        // 즐겨찾기 상태 토글
                        await toggleFavorite(facilityId, currentFavoriteStatus);

                        // UI 업데이트
                        const updatedFavoriteStatus = await checkFavorite(facilityId);
                        console.log(updatedFavoriteStatus, "updatedFavoriteStatus")

                        button.setAttribute("data-favorite", updatedFavoriteStatus);

                        if (updatedFavoriteStatus === 0) {
                            const liItem = event.target.closest("li"); // 버튼이 속한 li를 찾음
                            if (liItem) {
                                liItem.remove(); // li 삭제
                            }

                            // 데이터 배열에서 삭제
                            const indexToRemove = data.findIndex(item => item.id === facilityId);
                            if (indexToRemove > -1) {
                                data.splice(indexToRemove, 1); // 배열에서 해당 아이템 삭제
                            }


                            // 데이터가 없으면 '저장된 즐겨찾기가 없습니다.' 메시지 표시
                            if (data.length === 0) {
                                const noResultItem = document.createElement("li");
                                noResultItem.classList.add('no-result', 'border-top');
                                noResultItem.textContent = "저장된 즐겨찾기가 없습니다.";
                                favoriteList.appendChild(noResultItem);
                            }

                            // 현재 페이지가 데이터 크기를 초과하면 이전 페이지로 이동
                            const totalPages = Math.ceil(data.length / itemsPerPage);
                            if (currentPage > totalPages) {
                                currentPage = totalPages;
                            }

                            // 삭제 후 다시 렌더링
                            await renderData(currentPage);
                            if (data.length >= itemsPerPage) {
                                renderPagination();
                            } else {
                                document.querySelector(".pagination").textContent = "";
                            }
                        }
                    }  catch (error) {
                        console.error("Error toggling favorite:", error);
                    } finally {
                        button.disabled = false; // Re-enable
                    }


                }
            });
        }


    } catch (error) {
        console.error("AJAX 오류:", error);
    }
});


/*리뷰*/
const reviewTab = document.getElementById("review-tab");
reviewTab.addEventListener("click", reviewClickFunction);


async function reviewClickFunction(){
    try {
        const response = await fetch("/user/review");
        if (!response.ok) {
            throw new Error("서버 오류: " + response.status);
        }

        const data = await response.json();
        console.log("받은 데이터:", data);

        const reviewList = document.getElementById("review").querySelector("ul.review-list");
        reviewList.textContent = "";

        const itemsPerPage = 4; // 한 페이지에 표시할 아이템 수
        let currentPage = 1;

        if (data.message === '작성한 리뷰가 없습니다.') {
            const liItem = document.createElement("li");
            liItem.classList.add('no-result', 'border-top');
            liItem.textContent = "작성한 리뷰가 없습니다.";
            reviewList.appendChild(liItem);
        }else {
            // 데이터 렌더링 함수
            const renderData = async (page) => {
                const reviewList = document.getElementById("review").querySelector("ul.review-list");
                reviewList.textContent = "";

                const startIndex = (page - 1) * itemsPerPage;
                const endIndex = startIndex + itemsPerPage;
                const itemsToShow = data.slice(startIndex, endIndex);

                if (itemsToShow.length === 0) {
                    console.log("렌더링할 데이터가 비어 있음");
                    const liItem = document.createElement("li");
                    liItem.classList.add('no-result', 'border-top');
                    liItem.textContent = "작성한 리뷰가 없습니다.";
                    reviewList.appendChild(liItem);
                    return; // 데이터가 없으면 이후 렌더링 중단
                }

                for (const item of itemsToShow) {
                    const liItem = document.createElement("li");

                    liItem.classList.add("border");
                    liItem.innerHTML = `
                    <div class="d-flex justify-content-between">
                        <div class="left d-flex align-items-center">
                            <div class="img-wrap border">
                                <img src="${item.profilePic}" alt="프로밀 이미지">
                            </div>
                            <div>
                                <p class="name">${item.userNickname}</p>
                                <div class="review-rating">
                                    ${renderStars(item.rating)}
                                </div>
                            </div>
                        </div>
                        <div class="right">
                            <!--할것인지 확인-->
                            <button type="button" class="btn-modify" onclick="editReview(${item.id})"><i class="fa-solid fa-pen-to-square"></i></button>
                            <button type="button" class="btn-delete" data-id="${item.id}"><i class="fa-solid fa-trash-can"></i></button>
                        </div>
                    </div>
                    <p class="contents">${item.comment}</p>
                `;
                    reviewList.appendChild(liItem);
                }
            };

            // 페이지네이션 버튼 렌더링
            const renderPagination = () => {
                const pagination = document.querySelector("#review .pagination");
                console.log("pagination: " + pagination);
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
            if (Array.isArray(data)) await renderData(currentPage);
            if(data.length > itemsPerPage) renderPagination();


            // 리뷰 삭제
            const reviewList = document.getElementById("review").querySelector("ul.review-list");
            reviewList.addEventListener("click", async function(event){
                const button = event.target.closest(".btn-delete");
                if (button) {
                    button.disabled = true;
                    try {
                        const facilityId = parseInt(button.getAttribute("data-id"));

                        // 삭제 결과
                        const result = await deleteReview(facilityId);
                        console.log("result : " + result);

                        if(result > 0){
                            const liItem = event.target.closest("li"); // 버튼이 속한 li를 찾음
                            if (liItem) {
                                liItem.remove(); // li 삭제
                            }
                            const indexToRemove = data.findIndex(item => item.id === facilityId);
                            if (indexToRemove > -1) {
                                data.splice(indexToRemove, 1); // 배열에서 해당 아이템 삭제
                            }

                            if (data.length === 0) {
                                const liItem = document.createElement("li");
                                liItem.classList.add('no-result', 'border-top');
                                liItem.textContent = "작성한 리뷰가 없습니다.";
                                reviewList.appendChild(liItem);
                            }

                            // 현재 페이지가 데이터 크기를 초과하면 이전 페이지로 이동
                            const totalPages = Math.ceil(data.length / itemsPerPage);
                            if (currentPage > totalPages) {
                                currentPage = totalPages;
                            }

                            // 삭제 후 다시 렌더링
                            await renderData(currentPage);
                            if (data.length >= itemsPerPage) {
                                renderPagination();
                            } else {
                                document.querySelector(".pagination").textContent = "";
                            }
                        }


                    }catch(e){
                        console.log("삭제 실패!");
                    } finally {
                        button.disabled = false;
                    }

                }
            });

        }


    } catch (error) {
        console.error("AJAX 오류:", error);
    }
}


function renderStars(averageRating) {
    let starsHtml = '';

    for (let i = 1; i <= 5; i++) {
        if (averageRating >= i) {
            starsHtml += '<span class="filled"><i class="fa fa-star" aria-hidden="true"></i></span>';
        } else if (averageRating >= i - 0.5) {
            starsHtml += '<span class="half-filled"><i class="fa fa-star-half-o" aria-hidden="true"></i></span>';
        } else {
            starsHtml += ''; // 별을 표시하지 않음
        }
    }

    return starsHtml;
}


async function deleteReview(id){
    const url = '/Review/reviewDelete';
    const data = {
        id : id
    };

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (!response.ok) {
            throw new Error('서버 오류: ' + response.status);
        }

        const result = await response.json(); // 응답 데이터를 JSON으로 변환
        console.log("data : ", result);
        return result; // 결과 반환
    } catch (error) {
        console.error('AJAX 오류:', error);
        throw error; // 에러를 호출한 곳으로 전달
    }
}


async function editReview(id){
    // fetch로 데이터를 불러옵니다.
    try {
        let review = await getReview(id);

        loadReviewData(review);  // 리뷰 데이터를 모달에 로드
        const exampleModal = new bootstrap.Modal(document.getElementById('exampleModal'));
        exampleModal.show();
    } catch (error) {
        console.error('리뷰 데이터를 불러오는 데 실패했습니다:', error);
        alert('리뷰 데이터를 불러오는 데 실패했습니다.');
    }
}

async function getReview(id) {
    const response = await fetch(`/Review/getReview?id=${id}`);
    const review = await response.json();  // 리뷰 데이터를 JSON으로 파싱
    console.log(review);

    return review;
}

// 리뷰 데이터를 모달에 로드하는 함수
function loadReviewData(review) {

    // review.rating 값을 숨겨진 input에 설정
    document.getElementById('ratingValue').setAttribute('value', review.rating);
    console.log("보자보자"+ review.rating)

// review.comment 값을 textarea에 설정
    console.log("review.comment" + review.comment);
    document.getElementById('comment').value = review.comment;
    console.log("review.id" + review.id);
    document.getElementById("reviewId").value = review.id;
    document.getElementById("facilityId").value = review.facilityId;

// 평점에 맞는 별 표시
    document.querySelectorAll('#rating .star').forEach(star => {
        const starValue = star.getAttribute('data-value'); // 각 별의 data-value 속성 값 가져오기
        const icon = star.querySelector("i"); // <i> 태그 찾기

        if (icon) { // <i> 태그가 존재하는지 확인
            if (starValue <= review.rating) { // 별의 값이 리뷰 평점보다 작거나 같으면
                icon.classList.remove("fa-star"); // 빈 별을 제거
                icon.classList.add("fa-star"); // 채워진 별 추가
                icon.style.color = "#f79256"; // 채워진 별은 색상 변경
            } else { // 별의 값이 리뷰 평점보다 크면
                icon.classList.remove("fa-star"); // 채워진 별을 제거
                icon.classList.add("fa-star"); // 빈 별 추가
                icon.style.color = "gray"; // 빈 별은 회색
            }
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    const stars = document.querySelectorAll(".rating .star"); // 별 요소들

    let rating= 0;
    // 별 클릭 시 평점 설정
    stars.forEach(star => {
        star.addEventListener("click", function() {
            rating = parseInt(this.getAttribute("data-value"));
            updateStars(rating);
        });

        // 마우스 오버 시 동적 별 채우기
        star.addEventListener("mouseover", function() {
            const value = parseInt(this.getAttribute("data-value"));
            updateStars(value);
        });

        // 마우스 아웃 시 현재 평점 상태 유지
        star.addEventListener("mouseout", function() {
            updateStars(rating);
        });
    });

    const modalElement = document.getElementById("exampleModal");

    // 모달 닫힘 이벤트에 리스너 추가
    modalElement.addEventListener("hidden.bs.modal", function () {
        // 입력 필드 초기화
        document.getElementById("comment").value = "";
        document.getElementById("ratingValue").value = "";
        document.getElementById("facilityId").value = "";

        resetStars();
        console.log("모달이 닫혔습니다. 데이터 초기화 완료!");
    });

    // 별 초기화 함수
    function resetStars() {
        stars.forEach(star => {
            star.querySelector("i").classList.remove("fa-star");
            star.querySelector("i").classList.add("fa-star");
            star.querySelector("i").style.color = "gray"; // 기본 회색으로 되돌리기
        });
        rating = 0; // 초기 평점으로 리셋
    }

    // 별 상태 업데이트 함수
    function updateStars(rating) {
        stars.forEach(star => {
            const value = parseInt(star.getAttribute("data-value"));
            if (value <= rating) {
                star.querySelector("i").classList.remove("fa-star");
                star.querySelector("i").classList.add("fa-star");
                star.querySelector("i").style.color = "#f79256";
            } else {
                star.querySelector("i").classList.remove("fa-star");
                star.querySelector("i").classList.add("fa-star");
                star.querySelector("i").style.color = "gray"; // 빈 별은 회색
            }
        });
    }

    const reviewForm = document.getElementById("reviewinsertForm");

    reviewForm.addEventListener("submit", async function (event) {
        // 평점이 선택되지 않았으면 경고

        event.preventDefault();

        const allGray = Array.from(document.querySelectorAll('#rating .star i')).every(icon => {
            return icon.style.color === 'gray'; // 모든 아이콘이 회색인지 확인
        });

        if (allGray) {
            console.log("모든 별이 회색입니다.");
            alert("평점을 선택해 주세요!");
            return;
        }
        console.log("dsds"+rating);
        if(rating === 0 || rating===""){
            rating = document.getElementById("ratingValue").value;
            if (rating === 0 || rating ==="") {
                console.log("입력했을때안했을때"+ value)
                alert("평점을 선택해 주세요!");
                return;
            }

        }else {
            document.getElementById("ratingValue").value = rating;
            // 모든 별 요소를 선택

            console.log("입력했을때" + rating)
            if (rating === 0 || rating ==="") {
                console.log("입력했을때안했을때"+ value)
                alert("평점을 선택해 주세요!");
                return;
            }
        }

        // 리뷰 내용이 비어있으면 경고
        const comment = document.getElementById("comment").value;
        if (comment.trim() === "") {
            alert("리뷰 내용을 작성해 주세요!");
            event.preventDefault();  // 폼 제출을 막음
            return;
        }
        // 이 부분을 통해 시설 ID를 가져오는 로직을 작성하세요.
        let id = document.getElementById('reviewId').value;
        /*document.getElementById('facilityId').value = review.facilityId;*/
        const facilityId = document.getElementById("facilityId").value;

        // 폼 제출이 문제없이 진행됨
        console.log("리뷰 데이터:", {
            rating: rating,
            comment: comment,
            facilityId: facilityId,
            id : id
        });


        console.log("되나요ㅕ?"+ id);

        const url = '/Review/reviewInsert';
        const data = {
            rating: rating,
            comment: comment,
            facilityId: facilityId,
            id : id
        };

        await fetch(url, {
            method: 'POST',  // POST 메소드로 요청
            headers: {
                'Content-Type': 'application/json',  // JSON 형식으로 전송
            },
            body: JSON.stringify(data),  // 데이터를 JSON 형식으로 변환하여 전송
        });
        document.getElementById('reviewId').value = null;

        /*await loadReviews(review.facilityId);*/
        await reviewClickFunction();

    });
});


/*게시판*/
const boardTab = document.getElementById("board-tab");
boardTab.addEventListener("click", async function(){
    console.log("클릭됨");

    const response = await fetch("/user/board");
    if (!response.ok) {
        throw new Error("서버 오류: " + response.status);
    }

    const post = await response.json();
    console.log("받은 데이터:", post);

    const cardContainer = document.querySelector("#board .card-container");
    cardContainer.textContent = "";

    if(post.length === 0 || post.message === "작성된 게시물이 없습니다."){

    }else {
        const table = document.createElement("table");
        table.style.width = '100%';
        table.style.borderCollapse = 'collapse';
        let tableHtml = `
            <thead>
                <tr>
                    <th>카테고리</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                </tr>
            </thead>
            <tbody>`

        /*공지 핀포스트*/
        for(let item of post.pinnedNoticePosts){
            tableHtml +=`
                <tr class="pinned-post">
                    <td>${item.category}</td>
                    <td>
                        <a href="/community/${item.id}">
                            <span>${item.title}</span>
                            <span class="comment-count">[<span>${item.commentsCount}</span>]</span>
                        </a>
                    </td>
                    <td>${item.displayName}</td>
                    <td>${item.updatedAt != null ? item.formattedUpdatedAt : item.formattedCreatedAt}</td>
                </tr>
            `
        }
        /* 가이드 핀포스트 */
        for(let item of post.pinnedGuidePosts) {
            tableHtml +=`
            <tr class="pinned-post">
                    <td>${item.category}</td>
                    <td>
                        <a href="/community/${item.id}">
                            <span>${item.title}</span>
                            <span class="comment-count">[<span>${item.commentsCount}</span>]</span>
                        </a>
                    </td>
                    <td>${item.displayName}</td>
                    <td>${item.updatedAt != null ? item.formattedUpdatedAt : item.formattedCreatedAt}</td>
                </tr>
            `;
        }

        tableHtml += `</tbody>`;

        table.innerHTML = tableHtml;

        const cardList = document.createElement("div");
        cardList.classList.add('card-list');
        let cardHtml = '';
        for(let item of post.generalPosts){
            cardHtml += `
                <div class="card-item">
                    <div class="card-category">${item.category}</div>
                    <div class="card-title">
                        <a th:href=/community/${item.id}">
                            <span>${item.title}</span>
                        </a>
                    </div>
                    <div class="card-content">${item.content}</div>
                    <div class="card-meta">
                        <span>${item.displayName}</span>
                        <span>${item.updatedAt != null ? item.formattedUpdatedAt : item.formattedCreatedAt}</span>
                        <i class="fa-solid fa-eye"></i><span>${item.viewCount}</span>
                        <i class="fa-regular fa-heart"></i><span>${item.heartsCount}</span>
                        <i class="fa-regular fa-comment-dots"></i><span>${item.commentsCount}</span>
                    </div>
                </div>
            `
        }

        cardList.innerHTML = cardHtml;
        cardContainer.appendChild(table);
        cardContainer.appendChild(cardList);
    }

});