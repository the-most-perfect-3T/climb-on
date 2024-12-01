/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 크루 리스트~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
const crewListContainer = document.getElementById("crewList");
const showMoreBtn = document.getElementById("showMoreBtn");
const sortByBtn = document.getElementById("sortBy");
const areasFilterBtn = document.getElementById("areasFilter");
const triggers = document.querySelectorAll('[data-bs-target="#areaModal"]');
let sortOrder = "latest";
let currentPage = 1;
const filterAreas = [];
let currentContext;


// 지역 필터 버튼 클릭시(모달에서의 이벤트는 맨밑에서 관리, 등록,리스트 어디서 눌렀는지에 따라 이벤트 분리)
areasFilterBtn.addEventListener('click', function () {
    secondModal.show();
    // 두 모달과 뒤의 backdrop(어두운 배경) z-index 조정
    document.getElementById('areaSelectModal').style.zIndex = '1060';
    secondModal.backdrop = true;
});

// 어디서 모달을 불러왔는지 저장(지역선택 모달을 crewList 페이지와 crewRegister 페이지 두 곳에서 불러오기 때문)
triggers.forEach(trigger => {
    trigger.addEventListener('click', () => {
        currentContext = trigger.dataset.context;
    });
});

// 최신순/회원수 버튼 클릭시 리스트를 새로 채워준다
sortByBtn.addEventListener('click', () => {
    sortByBtn.innerHTML = (sortByBtn.innerText.includes("최신순")) ? "<i class=\"fa-solid fa-arrow-down-short-wide\"></i>회원수" : "<i class=\"fa-solid fa-arrow-down-short-wide\"></i>최신순";
    sortOrder = (sortOrder === "latest") ? "mostMembers" : "latest";
    crewListContainer.innerHTML = '';
    currentPage = 0;
    loadCrews(currentPage);
});

// 더보기 버튼 클릭시
showMoreBtn.addEventListener('click', () => {
    loadCrews(currentPage);
});

async function loadCrews(page) {
    try {
        const response = await fetch(`/crew/crewlistMore?page=${page}&sort=${sortOrder}&areas=${filterAreas}`);
        if(!response.ok){
            throw new Error("크루 더보기 조회 실패");
        }
        const data = await response.json();
        const count = data.count;
        const crewList = data.crews;
        if(count !== null){
            document.querySelector('.count-number').textContent = `${count} 개`;
        }
        currentPage++;

        crewList.forEach((crew) => {
            const tr = document.createElement('tr');
            tr.classList.add('border-bottom');
            tr.innerHTML = `
            <td class="d-flex flex-column gap-2" >
                <div class="position-relative align-items-center" style="width: 60px; height: 60px;">
                    <div class="img-wrap d-flex align-items-center justify-content-center" style="width: 100%; height: 100%">
                        <img src="${crew.imgUrl}" alt="/images/logo.svg" class="w-100">
                    </div>
                </div>
                <span class="badge rounded-pill" style="display: ${crew.recruitingStatus == true? "" : "none"}">모집중</span>
            </td>
            <td>
                <a href="/crew/myCrew/${crew.id}">
                    <p class="mb-1 fw-bold">${crew.crewName}</p>
                    <small class="text-muted">${crew.activeArea}</small>
                </a>
            </td>
            <td class="description">${crew.description}</td>
            `
            crewListContainer.appendChild(tr);
        });
        if(crewList.length < 5) {
            showMoreBtn.style.display = 'none';
            alert("모든 크루 리스트 입니다.");
        }else {
            showMoreBtn.style.display = '';
        }

    } catch (error){
        console.error(error);
        alert("크루 리스트를 불러오는중 문제가 발생했습니다.")
    }
}

document.getElementById("registerCrewModalBtn").addEventListener('click', async (event) => {
    event.preventDefault();

    try {
        const response = await fetch('/crew/checkHasCrew')
        if(!response.ok){
            throw new Error("서버에러");
        }
        const message = await response.text();
        if(message.includes("로그인")){
            window.location.href = "/auth/login";
        }
        else if(message.includes("소속된 크루X")){
            firstModal.show();
        }
        else if(message.includes("okay")){
            alert("소속된 크루가 없어야 크루 등록이 가능합니다. \n1인 1크루 원칙!");
        }
    }catch (error){
        console.error(error);
        alert(error);
    }
});




/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 크루등록 모달~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
const firstModal = new bootstrap.Modal(document.getElementById('creWRegisterModal'), {
    backdrop: 'static',
    keyboard: false
});
const secondModal = new bootstrap.Modal(document.getElementById('areaSelectModal'), {
    keyboard: false
});

// 지역선택모달
document.getElementById('openAreaSelectModal').addEventListener('click', function () {
    secondModal.show();

    // 두 모달과 뒤의 backdrop(어두운 배경) z-index 조정
    document.getElementById('areaSelectModal').style.zIndex = '1060';
    document.querySelector('.modal-backdrop').style.zIndex = '1055';
    document.getElementById('creWRegisterModal').style.zIndex = '';
});

const secondModalClose = document.getElementById('closeAreaSelectModal');

// 지역선택 모달 닫아도 크루등록 모달 보여주기
secondModalClose.addEventListener('click', function () {
    secondModal.hide();
    document.getElementById('creWRegisterModal').style.zIndex = '1059';
});


// 크루 사진 수정
const buttonForPicInput = document.getElementById('imgInsert');
const crewPicInput = document.getElementById('crewPicInput');

// 버튼 클릭시 숨겨진 input(type:file, display: none) 작동
buttonForPicInput.addEventListener("click",function (){
    crewPicInput.click();   // 가상의 클릭
});

// 유저가 이미지 파일 선택해서 "change" 발생시 해당 파일 읽어와서 base64(이미지 파일을 upload 없이 display 해주는 방식)로 만들어 img.src에 넣어줌
crewPicInput.addEventListener("change", function (){
    const file = crewPicInput.files[0];
    const reader = new FileReader();
    reader.onload = () => {
        const base64URL = reader.result;
        const img = document.getElementById('crewPic');
        img.setAttribute('src', base64URL);
    };
    reader.readAsDataURL(file);
});

// 크루이름 제약조건(10자 이내)
// 제약조건 통과 후에만 중복확인 가능
let crewNameInput = document.getElementById('crewName');
const crewNameBtn = document.getElementById('crewNameBtn');
let errorMessage = document.getElementById('crewName-error');
let successMessage = document.getElementById('crewName-success');
crewNameBtn.disabled = true;

crewNameInput.addEventListener('keyup', function (event){
    let value = crewNameInput.value;
    if(value.length >=1 && value.length <= 15){
        errorMessage.textContent = "";
        crewNameBtn.disabled = false;
    }else{
        errorMessage.textContent = "1~15자 이내"
        crewNameBtn.disabled = true;
    }
    successMessage.textContent = "";
});

// 중복확인 후 메시지 출력
crewNameBtn.addEventListener('click', function (){
    const crewName = crewNameInput.value;
    fetch(`/crew/checkCrewName?crewName=${encodeURIComponent(crewName)}`)
        .then(response => response.text())
        .then(message => {
            if(message.includes("중복")){
                errorMessage.innerText = message;
            }else if(message.includes("사용 가능")){
                successMessage.innerText = message;
            }
        });
});

// 크루 소개글 문자수 count 후 출력
let crewIntro = document.getElementById('description');
let crewIntroCount = document.getElementById('crewIntroCount');
crewIntroCount.innerText = crewIntro.value.length;
crewIntro.addEventListener('keyup', function (){
   crewIntroCount.innerText = crewIntro.value.length;
});

// 카테고리 드롭다운에서 선택시 버튼이 추가, 제거가능
const dropdownMenu = document.getElementById('categoryDropdown');
const selectedCategoriesContainer = document.getElementById('selectedCategories');
const placeholder = document.getElementById('placeholder');

// 카테고리 선택 (드롭다운 클릭시 작동)
dropdownMenu.addEventListener('click', (event) => {
   const item = event.target.closest(".dropdown-item");     // 클릭한게 dropdown-item 이 아니면 return !!정확한 동작을 위함
   if (!item) return;

   /*클릭된 항목의 value를 담아서 button을 만들어
    selectedCategoriesContainer 에 넣어주고 해당 항목은 dropdown 에서 지워준다
    선택된 항목이 하나라도 있으면 placeholder도 없애준다.*/
   const value = item.getAttribute("data-value");
   const span = document.createElement("span");
   const imgName = value === "볼더링" ? "climbing-wall.png" : value === "리드" ? "free-icon-carabiner.png" : "outdoor_mountain.png"
   span.className = "btn btn-warning btn-sm rounded-pill d-flex align-items-center gap-2";
   span.dataset.value = value;
   span.innerHTML = `${value} <img src="/images/${imgName}" style="width: 16px; height: 16px">  
                     <button type="button" class="btn-close"></button>`;

   selectedCategoriesContainer.appendChild(span);
   dropdownMenu.removeChild(item.parentNode);

   if(selectedCategoriesContainer.children.length > 0) {
       placeholder.style.display = "none";
   }
});

// 카테고리 선택취소
// 취소시 해당버튼제거, dropdown에는 다시 추가
selectedCategoriesContainer.addEventListener("click", (event) => {
    const btn = event.target.closest("button");
    if (!btn) return;

    const value = btn.parentNode.dataset.value;
    selectedCategoriesContainer.removeChild(btn.parentNode);

    const newItem = document.createElement("li");
    newItem.innerHTML = `<a class="dropdown-item" data-value="${value}">${value}</a>`;
    dropdownMenu.appendChild(newItem);

    if (selectedCategoriesContainer.children.length === 0) {
        placeholder.style.display = "block";
    }
});

// post 요청 보내기 전에 required 체크
const formSubmit = document.getElementById('registerCrewForm');
formSubmit.onsubmit = async (event) => {
    event.preventDefault();
    const crewPicInput = document.getElementById("crewPicInput");

    // 모든 필수 입력사항 체크
    if(crewPicInput.files.length === 0){
        alert("크루 사진 등록은 필수 입니다.");
        return false;
    }
    if(!successMessage.textContent.includes("사용 가능")){
        alert("크루 이름 중복확인은 필수 입니다.");
        return false;
    }
    if(selectedCategoriesContainer.children.length === 0){
        alert("클라이밍 카테고리 선택은 필수 입니다.");
        return false;
    }
    if(selectedAreasContainer.children.length === 0){
        alert("활동 지역 선택은 필수 입니다.");
        return false;
    }

    // hidden input 에 추가
    const children = Array.from(selectedCategoriesContainer.children);
    const selectedCategories = [];
    children.forEach((child) => {
        selectedCategories.push(child.dataset.value);
    });
    document.getElementById("climbingCategory").value = selectedCategories;

    const children2 = Array.from(selectedAreasContainer.children);
    const selectedAreas = [];
    children2.forEach((child) => {
        selectedAreas.push(child.dataset.value);
    });
    document.getElementById("activeArea").value = selectedAreas;

    const formData = new FormData();
    formData.append('imageFile', crewPicInput.files[0]);

    try{
        const response = await fetch('/file/uploadCrewPic', {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'text/plain'
            }
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const img = await response.text();
        document.getElementById("imgUrl").value = img;

        formSubmit.submit();
    }  catch(err) {
        console.error('Upload failed:', err);
        alert("크루 이미지 업로드 중 문제가 발생했습니다.2222", err);
        return false;
    }
};


// x 버튼(모든걸 reset)
const closeBtn = document.getElementById('closeBtn');
closeBtn.addEventListener("click", function (event){
    event.preventDefault();
    const resetBtn = document.getElementById('resetBtn');
    resetBtn.click();
    errorMessage.textContent = "";
    successMessage.textContent = "";
    crewIntroCount.innerText = crewIntro.value.length;

    const img = document.getElementById('crewPic');
    img.setAttribute('src', "/images/logo.svg");

    const children = Array.from(selectedCategoriesContainer.children);
    children.forEach((child) => {
        selectedCategoriesContainer.removeChild(child);
        const value = child.dataset.value;
        const newItem = document.createElement("li");
        newItem.innerHTML = `<a class="dropdown-item" data-value="${value}">${value}</a>`;
        dropdownMenu.appendChild(newItem);
    });
    placeholder.style.display = "block";

    const children2 = Array.from(selectedAreasContainer.children);
    children2.forEach((child) => {
       selectedAreasContainer.removeChild(child);
    });
    areaPlaceholder.style.display = "block";
});


/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 지역선택 모달~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
const selectedAreasContainer = document.getElementById('selectedAreasContainer');
const areaPlaceholder = document.getElementById('areaPlaceholder');
const selectedFilterAreasContainer = document.getElementById('selected-filters');

document.querySelectorAll('#areaSelectModal .dropdown-menu').forEach(menu => {
    menu.addEventListener('click', function(event) {
        const item = event.target.closest(".dropdown-item");     // 클릭한게 dropdown-item 이 아니면 return !!정확한 동작을 위함
        if (!item) return;

        let value;
        // 지역 전체 선택시 와 일부 구역(시, 구) 선택 구분
        if(item.parentNode.parentNode.firstElementChild === item.parentNode){
            value = item.getAttribute('data-value');
        }else {
            value = item.parentNode.parentNode.getAttribute('data-value') + " - " + item.getAttribute('data-value');
        }

        // 선택된 활동 지역을 보여줄 버튼 생성
        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'btn btn-sm rounded-pill me-2 gap-2 d-flex align-items-center border-0 btnSelectedArea';
        button.style.backgroundColor = 'var(--beige)';
        button.style.color = 'var(--black)';
        button.style.margin = '0 0.5rem 0.5rem 0';
        button.innerHTML = `${value} <i class="fa-solid fa-xmark ms-1"></i>`;
        button.dataset.value = value;

        // 버튼 삭제시(활동지역 비선택시) 일어나야할 이벤트
        button.querySelector('.fa-xmark').addEventListener('click', function(event) {
            event.stopPropagation();
            if(currentContext === "registerCrew"){
                selectedAreasContainer.removeChild(button);
                // 선택된 지역 없을시 다시 placeholder 보여줌
                if (selectedAreasContainer.children.length === 0) {
                    areaPlaceholder.style.display = "block";
                }

            }else if(currentContext === "filterCrewList"){
                selectedFilterAreasContainer.removeChild(button);
                if(selectedFilterAreasContainer.children.length === 0){
                    selectedFilterAreasContainer.style.display = "none";
                }

                const areas = Array.from(selectedFilterAreasContainer.children);
                filterAreas.length = 0;
                areas.forEach(area => {
                    filterAreas.push(area.dataset.value);
                });
                crewListContainer.innerHTML = '';
                currentPage = 0;
                loadCrews(currentPage);
            }
        });

        if(currentContext === "registerCrew"){
            // 선택된 지역 버튼 보여줄 때
            // 지역 전체 선택이면 if 기존 일부구역 버튼 있으면 delete 후 전체 버튼 append
            // 동일 지역 선택시 append X
            const children = Array.from(selectedAreasContainer.children);

            // 전체 선택인데 구역 있을시
            if(button.textContent.includes("전체")){
                const area = button.textContent.trim().substring(0, button.textContent.length -4);
                children.forEach((child) => {
                    if(child.textContent.includes(area)){
                        selectedAreasContainer.removeChild(child)
                    }
                })
                selectedAreasContainer.appendChild(button);
                secondModalClose.click();
                // 구역 선택인데 전체 있을시, 이미 선택한 구역일시, 나머지
            }else {
                const area2 = button.textContent.trim().substring(0, button.textContent.indexOf("-")).trim();
                let isButtonPresent = false;
                for(let i =0; i<children.length; i++){
                    const child = children[i];
                    if(child.textContent.includes(area2) && child.textContent.includes("전체")){
                        selectedAreasContainer.removeChild(child);
                        selectedAreasContainer.appendChild(button);
                        secondModalClose.click();
                        return false;
                    }
                    if(child.textContent === button.textContent){
                        isButtonPresent = true;
                        secondModalClose.click();
                        return false;
                    }
                }
                if(!isButtonPresent){
                    selectedAreasContainer.appendChild(button);
                }
                secondModalClose.click();
            }

            // 선택된 지역 하나라도 있으면 플레이스 홀더 X
            if (selectedAreasContainer.children.length > 0) {
                areaPlaceholder.style.display = 'none';
            }
        }

        else if(currentContext === "filterCrewList"){
            // 선택된 지역 버튼 보여줄 때
            // 지역 전체 선택이면 if 기존 일부구역 버튼 있으면 delete 후 전체 버튼 append
            // 동일 지역 선택시 append X
            const children = Array.from(selectedFilterAreasContainer.children);

            // 전체 선택인데 구역 있을시
            if(button.textContent.includes("전체")){
                const area = button.textContent.trim().substring(0, button.textContent.length -4);
                children.forEach((child) => {
                    if(child.textContent.includes(area)){
                        selectedFilterAreasContainer.removeChild(child)
                    }
                })
                selectedFilterAreasContainer.appendChild(button);
                secondModalClose.click();
                // 구역 선택인데 전체 있을시, 이미 선택한 구역일시, 나머지
            }else {
                const area2= button.textContent.trim().substring(0, button.textContent.indexOf("-")).trim();
                let isButtonPresent = false;
                for(let i =0; i<children.length; i++){
                    const child = children[i];
                    if(child.textContent.includes(area2) && child.textContent.includes("전체")){
                        selectedFilterAreasContainer.removeChild(child);
                        selectedFilterAreasContainer.appendChild(button);
                        secondModalClose.click();
                        break;
                    }
                    if(child.textContent === button.textContent){
                        isButtonPresent = true;
                        secondModalClose.click();
                        break;
                    }
                }
                if(!isButtonPresent){
                    selectedFilterAreasContainer.appendChild(button);
                }
                secondModalClose.click();
            }
            // 선택된 지역 하나라도 있으면 플레이스 홀더 X
            if (selectedFilterAreasContainer.children.length > 0) {
                selectedFilterAreasContainer.style.display = 'block';
            }

            const areas = Array.from(selectedFilterAreasContainer.children);
            filterAreas.length = 0;
            areas.forEach(area => {
                filterAreas.push(area.dataset.value);
            });
            crewListContainer.innerHTML = '';
            currentPage = 0;
            loadCrews(currentPage);
        }

    });
});





