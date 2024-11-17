
Quill.register('modules/imageResize', ImageResize.default || ImageResize);

// textEditor 생성 및 설정
const quill = new Quill('#editor', {
    modules: {
        toolbar: '#toolbar-container',
        imageResize:{
            displaySize: true
        },
    },

    // placeholder: 'Compose an epic...',
    theme: 'snow',
});

// file upload 시에 필요한 객체 미리 생성
const formData = new FormData();
const hiddenImgUrlInput = document.getElementById("hidden_imgURL_input");

// window.onload()
const content = /*[[${crewBoardDTO.content}]]*/ ''; // Use Thymeleaf to inject `postDTO.content`
// quill.clipboard.dangerouslyPasteHTML(content);
quill.insertEmbed(0, 'string', content);

// textEditor 는 div에 구현되어 form 태그에 속해 있어도 submit 시 dto에 담기지 않는다.
// textEditor 에 기입되는 내용물 hidden input 으로 전달
// input 태그에 전달된 내용물이 form submit 시 같이 dto 에 담겨 넘어간다.
const hiddenContentInput = document.getElementById("hidden_content_input");
quill.on('text-change', function (){
    hiddenContentInput.value = quill.root.innerHTML;
    console.log(quill.root.innerHTML);
});

document.getElementById('anonymous').addEventListener('change', function () {
    document.getElementById('isAnonymous').value = this.checked ? 'true' : 'false';
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
//
document.getElementById('writePostForm').onsubmit = function (event) {
    event.preventDefault();

    // html 태그 제거
    const cleanTxt = quill.root.innerHTML.replace(/<(?!img\s*\/?)[^>]+>/g, "");
    // 모든 공백 제거
    const resultTxt = cleanTxt.replace(/\s/g, '').trim();
    console.log(resultTxt);

    if (resultTxt.length === 0) {
        appendAlert("본문에 내용이 있어야 등록 가능합니다!", 'success');
        return false;
    } else{
        fetch('/file/upload', {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json(); // Ensure text response is processed
            })
            .then(data => {
                console.log('File uploaded successfully:', data);
                hiddenImgUrlInput.value = data;
                const contentHTML = quill.root.innerHTML;
                const parser = new DOMParser();
                const doc = parser.parseFromString(contentHTML, 'text/html');
                const imgTags = Array.from(doc.getElementsByTagName('img'));
                imgTags.forEach((img, index) => {
                    img.src = data[index];
                });
                quill.root.innerHTML = doc.body.innerHTML;
                hiddenContentInput.value = quill.root.innerHTML;
                console.log(quill.root.innerHTML);
                console.log(hiddenContentInput.value);
                document.getElementById('writePostForm').submit();

            })
            .catch(err => {
                console.error('Upload failed:', err);
            });

        return false;

    }
};


quill.getModule('toolbar').addHandler('image', function (){
    selectLocalImage();
});

function selectLocalImage() {
    const fileInput = document.createElement('input');
    fileInput.setAttribute('type', 'file');
    fileInput.accept = "image/*";
    fileInput.click();
    fileInput.addEventListener("change", function () {  // change 이벤트로 input 값이 바뀌면 실행

        if (this.value !== "") { // 파일이 있을때만.
            var ext = this.value.substring(this.value.lastIndexOf(".")).toLowerCase();
            if (["gif", "jpg", "jpeg", "png", "bmp"].includes(ext)) {
                alert("jpg, jpeg, png, bmp, gif 파일만 업로드 가능합니다.");
                return;
            }

            // var fileSize = this.files[0].size;
            // var maxSize = 20 * 1024 * 1024;
            // if (fileSize > maxSize) {
            //     alert("업로드 가능한 최대 이미지 용량은 20MB입니다.");
            //     return;
            // }
            const file = fileInput.files[0];
            const reader = new FileReader();
            reader.onload = () => {
                const base64URL = reader.result;
                const range = quill.getSelection();
                quill.insertEmbed(range.index, 'image', base64URL);
            };
            reader.readAsDataURL(file);

            console.log(file);
            formData.append('uploadFile', file);
            console.log(formData);
        }
    });
}
