async function toggleHeart(event) {
    // 클릭된 버튼에서 postId 가져오기, 정확한 버튼 요소 가져오기
    const buttonElement = event.currentTarget;
    const postId = buttonElement.dataset.postId;
    const heartIcon = buttonElement.querySelector('i'); // 버튼 안의 아이콘 가져오기
    const isLiked = buttonElement.getAttribute('data-liked') === 'true'; // 현재 상태 확인

    // UI 상태 즉시 업데이트
    buttonElement.setAttribute('data-liked', !isLiked);
    if (!isLiked) {
        heartIcon.classList.remove('fa-regular');
        heartIcon.classList.add('fa-solid');
    } else {
        heartIcon.classList.remove('fa-solid');
        heartIcon.classList.add('fa-regular');
    }

    // 서버로 요청 보내기
    try {
        const response = await fetch(`/crew/${postId}/heart`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({liked: !isLiked}), // 요청데이터
        });

        if (!response.ok) {
            console.error('좋아요 상태 변경 실패');
            // 서버 응답 실패 시 UI 를 백
            buttonElement.setAttribute('data-liked', isLiked);
            if (isLiked) {
                heartIcon.classList.remove('fa-regular');
                heartIcon.classList.add('fa-solid');
            } else {
                heartIcon.classList.remove('fa-solid');
                heartIcon.classList.add('fa-regular');
            }
        }

    } catch (error) {
        console.error('네트워크 오류:', error); // 네트워크 오류 처리
        // 네트워크 오류 시 UI 를 백
        buttonElement.setAttribute('data-liked', isLiked);
        if (isLiked) {
            heartIcon.classList.remove('fa-regular');
            heartIcon.classList.add('fa-solid');
        } else {
            heartIcon.classList.remove('fa-solid');
            heartIcon.classList.add('fa-regular');
        }
    }
}

// 공유하기 기능 (Url복사)

function copyPostUrl(){
    const postUrl = window.location.href;
    navigator.clipboard.writeText(postUrl).then(() => {
        alert('URL이 복사되었습니다!');
    }).catch((error) => {
        console.error('URL 복사 실패:' ,error);
    });
}

// 드롭다운 메뉴 토글 처리
function setupDropdownMenus() {
    document.querySelectorAll('.menu-button').forEach(button => {
        button.addEventListener('click', function (event) {
            const dropdownMenu = event.target.closest('.comment-actions-menu').querySelector('.dropdown-menu');

            const isVisible = dropdownMenu.style.display === 'block';

            // 모든 드롭다운 메뉴 닫기
            document.querySelectorAll('.dropdown-menu').forEach(menu => {
                menu.style.display = 'none';
            });

            // 클릭된 드롭다운 메뉴 열기
            if (!isVisible) {
                dropdownMenu.style.display = 'block';
            }
        });
    });

    // 외부 클릭 시 드롭다운 닫기
    document.addEventListener('click', function (event) {
        if (!event.target.closest('.menu-button') && !event.target.closest('.dropdown-menu')) {
            document.querySelectorAll('.dropdown-menu').forEach(menu => {
                menu.style.display = 'none';
            });
        }
    });
}

// 댓글 수정 기능 처리
function setupCommentEdit() {
    // 수정 버튼 클릭 이벤트
    document.querySelectorAll(".edit-button").forEach(button => {
        button.addEventListener("click", function () {
            const commentId = this.getAttribute("data-comment-id");
            console.log("commentId:", commentId);
            const commentElement = document.querySelector(`[data-comment-id="${commentId}"]`);
            console.log("commentElement:", commentElement);
            const editForm = document.querySelector(".comment-edit-form");
            console.log("editForm", editForm);
            const commentText = document.querySelector(".comment-text");
            console.log("commentText",commentText);
            const dropdownMenu = this.closest('.dropdown-menu');
            console.log("dropdownMenu",dropdownMenu);
            // 수정 폼 보이기
            editForm.style.display = "block";
            commentText.style.display = "none";
            dropdownMenu.style.display = "none"; // 드롭다운 메뉴 닫기
        });
    });

    // 취소 버튼 클릭 이벤트
    document.querySelectorAll(".cancel-edit-button").forEach(button => {
        button.addEventListener("click", function () {
            const editForm = this.closest(".comment-edit-form");
            const commentElement = editForm.closest(".comment");
            const commentText = commentElement.querySelector(".comment-text");

            // 수정 폼 숨기기
            editForm.style.display = "none";
            commentText.style.display = "block";
        });
    });
}

// DOMContentLoaded 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
    // 좋아요 버튼 이벤트 리스너 추가
    document.querySelectorAll('.heart-button').forEach(button => {
        button.addEventListener('click', toggleHeart); //toggleHeart 함수 연결
    });

    // 드롭다운 메뉴 이벤트 리스너 추가
    setupDropdownMenus();

    // 댓글 수정 기능 이벤트 리스너 추가
    setupCommentEdit();
});