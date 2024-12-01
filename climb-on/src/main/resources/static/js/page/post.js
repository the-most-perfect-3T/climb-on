let currentPostCount = 2; // 초기 개수
const maxPostCount = 5; // 최대 개수
const keyword = '[[${keyword}]]'; // Thymeleaf에서 검색어 가져오기

function loadMorePosts() {
    if (currentPostCount >= maxPostCount) {
        alert("더 이상 데이터를 로드할 수 없습니다.");
        return;
    }

    fetch(`/search/loadMorePosts?currentCount=${currentPostCount}&limit=5&keyword=${encodeURIComponent(keyword)}`)
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
                                    <span>${post.updatedAt || post.createdAt}</span>
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