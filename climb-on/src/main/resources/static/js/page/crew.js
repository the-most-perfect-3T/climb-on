// textEditor 생성 및 설정
const quill = new Quill('#editor', {
    modules: {
        toolbar: '#toolbar-container',
    },
    // placeholder: 'Compose an epic...',
    theme: 'snow',
});


// textEditor 는 div에 구현되어 form 태그에 속해 있어도 submit 시 dto에 담기지 않는다.
// textEditor 에 기입되는 내용물 hidden input 으로 전달
// input 태그에 전달된 내용물이 form submit 시 같이 dto 에 담겨 넘어간다.
quill.on('text-change', function (){
    document.getElementById("hidden_input").value = quill.root.innerHTML;
    console.log(quill.root.innerHTML);
});


// required 가 걸리지 않는 textEditor 를 위해
// textEditor 내용물(본문)이 비었을시에 alert를 띄워주기 위한 객체와 함수를 만들어준다.
const alertPlaceholder = document.getElementById('liveAlertPlaceHolder');

const appendAlert = (message, type) => {
    const wrapper = document.createElement('div')
    wrapper.innerHTML = [
        `<div class="alert alert-${type} alert-dismissible" role="alert" style="background-color: var(--beige)">`,
        '<div>',
            '<i class="fa-solid fa-triangle-exclamation" style="color: var(--point)"></i>',
        `   ${message}</div>`,
        '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
    ].join('')

    alertPlaceholder.append(wrapper);
};

// 본문 내용검사후 통과 못하면 alert 적용
document.getElementById('writePostForm').onsubmit = function () {
    // html 태그 제거
    const cleantxt = quill.root.innerHTML.replace(/<\/?[^>]+(>|$)/g, "");
    // 모든 공백 제거
    const resulttxt = cleantxt.replace(/\s/g, '').trim();

    console.log(resulttxt);
    console.log(resulttxt.length);
    if (resulttxt.length === 0) {
        console.log("test")
        appendAlert("본문에 내용이 있어야 등록 가능합니다!", 'success');
        return false;
    } else{
        return true;
    }
};

//
quill.getModule('toolbar').addHandler('image', function (){
    selectLocalImage();
});

function selectLocalImage() {
    const fileInput = document.createElement('input');
    fileInput.setAttribute('type', 'file');
    fileInput.accept = "image/*";

    fileInput.click();

    fileInput.addEventListener("change", function () {  // change 이벤트로 input 값이 바뀌면 실행

        console.log(this.value);

        if (this.value !== "") { // 파일이 있을때만.

            var ext = this.value.substring(this.value.lastIndexOf(".")).toLowerCase();


            if (["gif", "jpg", "jpeg", "png", "bmp"].includes(ext) ) {

                alert("jpg, jpeg, png, bmp, gif 파일만 업로드 가능합니다.");
                return;
            }


            var fileSize = this.files[0].size;

            var maxSize = 20 * 1024 * 1024;

            if (fileSize > maxSize) {

                alert("업로드 가능한 최대 이미지 용량은 20MB입니다.");

                return;

            }

            const formData = new FormData();
            const file = fileInput.files[0];
            formData.append('uploadFile', file);

            const uploadFile = async (formData) => {
                try {
                    const response = await fetch('/file/upload', {
                        method: 'POST',
                        body: formData,
                        headers: {
                            'Accept': 'text/plain' // Expecting plain text response
                        }
                    });

                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }

                    const data = await response.text(); // Get the text response

                    // Assume `quill` is already initialized
                    const range = quill.getSelection();
                    quill.insertEmbed(range.index, 'image', "/file/display?fileName=" + data);
                } catch (err) {
                    console.error('ERROR!! ::', err);
                }
            };

// Assuming you have `formData` already prepared
            uploadFile(formData);

        }

    });
}