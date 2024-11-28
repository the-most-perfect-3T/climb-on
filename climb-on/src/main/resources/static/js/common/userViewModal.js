/* 타유저 프로필 모달 */
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
