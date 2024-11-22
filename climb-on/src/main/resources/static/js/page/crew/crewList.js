

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 크루등록 모달~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
const firstModal = new bootstrap.Modal(document.getElementById('creWRegisterModal'), {
    backdrop: 'static',
    keyboard: false
});
const secondModal = new bootstrap.Modal(document.getElementById('areaSelectModal'), {
    backdrop: false, // We will keep the first modal's backdrop visible
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

// 지역선택 모달 닫아도 크루등록 모달 보여주기
document.getElementById('closeAreaSelectModal').addEventListener('click', function () {
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
    if(value.length >=1 && value.length <= 10){
        errorMessage.textContent = "";
        crewNameBtn.disabled = false;
    }else{
        errorMessage.textContent = "1~10자 이내"
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
   const imgName = value === "볼더링" ? "bouldering.svg" : value === "리드" ? "lead_climbing.svg" : "outdoor_mountain.png"
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



/*
const formSubmit = document.getElementById('formSubmit');
formSubmit.addEventListener("submit")*/
// success message 체크
// hidden input 추가 후 submit
//

// x 버튼(모든걸 reset)
const closeBtn = document.getElementById('closeBtn');
closeBtn.addEventListener("click", function (event){
    event.preventDefault();
    const resetBtn = document.getElementById('resetBtn');
    resetBtn.click();
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
});


/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 지역선택 모달~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
const areaSelectModal = document.getElementById('areaSelectModal');
const selectedAreasContainer = document.getElementById('selectedAreasContainer');
const areaPlaceholder = document.getElementById('areaPlaceholder');

document.querySelectorAll('#areaSelectModal .dropdown-menu').forEach(menu => {
    menu.addEventListener('click', function(event) {
        const item = event.target.closest(".dropdown-item");     // 클릭한게 dropdown-item 이 아니면 return !!정확한 동작을 위함
        if (!item) return;
        const value = item.getAttribute('data-value');

        // Create button for selected area
        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'btn btn-warning btn-sm rounded-pill me-2';
        button.innerHTML = `${value} <i class="fa-solid fa-xmark ms-1"></i>`;
        button.dataset.value = value;

        // Add close functionality to the button
        button.querySelector('.fa-xmark').addEventListener('click', function(event) {
            event.stopPropagation();
            selectedAreasContainer.removeChild(button);
            // Add back the dropdown item if removed
            const newItem = document.createElement('li');
            newItem.innerHTML = `<a class="dropdown-item" data-value="${value}">${value}</a>`;
            newItem.addEventListener('click', function() {
                // Call the same event handler logic
            });
            this.closest('.dropdown-menu').appendChild(newItem);

            // Show placeholder if no categories selected
            if (selectedAreasContainer.children.length === 0) {
                areaPlaceholder.style.display = '';
            }
        });

        // Add the button to the selected categories container
        selectedAreasContainer.appendChild(button);

        // Remove selected option from dropdown
        this.parentElement.removeChild(this);

        // Hide placeholder if there's at least one category selected
        if (selectedAreasContainer.children.length > 0) {
            areaPlaceholder.style.display = 'none';
        }

        // Close modal after selection
        $(areaSelectModal).modal('hide');
    });
});





