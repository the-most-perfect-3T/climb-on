let currentPostCount = 2; // 초기 개수
let currentFacilityCount = 3;
const maxPostCount = 5; // 최대 개수
const keyword = document.getElementById('keywordInput').value.trim();

const btnFacilities = document.getElementById('more-btn-facilities');
const btnPosts = document.getElementById("more-btn-post");



async function loadMorePosts() {
    await fetch(`/search/loadMorePosts?currentCount=${currentPostCount}&limit=5&keyword=${encodeURIComponent(keyword)}`)
        .then(response => {
            if (!response.ok) {
                // HTTP 상태 코드가 200이 아닌 경우 처리
                return response.text().then(text => {
                    throw new Error(text || '데이터를 불러오는 중 오류가 발생했습니다.');
                });
            }
            return response.json(); // 성공하면 JSON 데이터를 반환
        })
        .then(data => {
            const postsContainer = document.querySelector(".result-post");
            if (data.length === 0) {
                alert("더 이상 로드할 게시물이 없습니다.");
                return;
            }
            const totalLength = parseInt(document.querySelector(".communityPostCount").textContent);
            const currentLength = document.querySelectorAll(".card").length + 5;

            if(totalLength <= currentLength) {
                btnPosts.style.display = "none";
            }
            console.log("ㅇㅁㅅㅁ", data);
            data.forEach(post => {
                const postElement = `
                        <div class="card d-flex flex-row">
                            <div class="card-body">
                                <div class="card-title">
                                    <a href="/community/${post.id}">
                                        <span>${post.title}</span>
                                    </a>
                                </div>
                                <div class="card-content">${post.content}</div>
                                <div class="card-meta">
                                    <span>${post.displayName}</span>
                                    <span>${post.updatedAt != null ? post.formattedUpdatedAt : post.formattedCreatedAt}</span>
                                    <i class="fa-solid fa-eye" aria-hidden="true"></i><span>${post.viewCount}</span>
                                    <i class="fa-regular fa-heart" aria-hidden="true"></i><span>${post.heartsCount}</span>
                                    <i class="fa-regular fa-comment-dots" aria-hidden="true"></i><span>${post.commentsCount}</span>
                                </div>
                            </div>
                        </div>`;
                postsContainer.insertAdjacentHTML("beforeend", postElement);
            });
            currentPostCount += data.length;
        })
        .catch(error => {
            console.error('Error loading more posts:', error);
            alert(error.message); // 사용자에게 오류 메시지 표시
        });
}


async function loadMoreFacilities() {
    await fetch(`/search/loadMoreFacilities?currentCount=${currentFacilityCount}&limit=5&keyword=${encodeURIComponent(keyword)}`)
        .then(response => {
            if (!response.ok) {
                // HTTP 상태 코드가 200이 아닌 경우 처리
                return response.text().then(text => {
                    throw new Error(text || '데이터를 불러오는 중 오류가 발생했습니다.');
                });
            }
            return response.json(); // 성공하면 JSON 데이터를 반환
        })
        .then(data => {
            const facilitiesContainer = document.querySelector(".result-facility");
            if (data.length === 0) {
                alert("더 이상 로드할 게시물이 없습니다.");
                return;
            }

            const totalLength = parseInt(document.querySelector(".facilityCount").textContent);
            const currentLength = document.querySelectorAll(".facility-item").length + 5;

            if(totalLength <= currentLength) {
                btnFacilities.style.display = "none";
            }

            data.forEach(facility => {
                const facilityElement = `
                        <div class="facility-item">
                            <h3>${facility.facilityName}</h3>
                            <p>${facility.address}</p>
                        </div>`;
                facilitiesContainer.insertAdjacentHTML("beforeend", facilityElement);
            });
            currentFacilityCount += data.length;
        })
        .catch(error => {
            console.error('Error loading more posts:', error);
            alert(error.message); // 사용자에게 오류 메시지 표시
        });
}
