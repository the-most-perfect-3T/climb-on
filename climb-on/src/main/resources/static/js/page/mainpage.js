const loadRecentPosts = (offset =0, category = "") =>
{
    fetch(`/api/posts/recent/paginated`)
        .then(response => {
            if(!response.ok) {
                throw new Error(`${response.status} 에러가 발생했습니다`);
            }
            return response.json();
        })
        .then(posts => {
            console.log(posts);
            const recentPostsContainer = document.getElementById('recent-posts');
            if (offset === 0) {
                recentPostsContainer.innerHTML = ""; // 카테고리 바뀔 때 목록 초기화
            }
            posts.forEach(post => {
                const row = document.createElement('tr');
                row.innerHTML = `
                            <td>${post.category}</td>
                            <td>${post.title}</td>
                        `;
                recentPostsContainer.appendChild(row);
            });
        })
        .catch(error => console.log(error.message));
}

const loadPopularPosts = () =>
{
    fetch(`/api/posts/popular`)
        .then(response => {
            if(!response.ok) {
                throw new Error(`${response.status} 에러가 발생했습니다`);
            }
            return response.json();
        })
        .then(posts => {
            const popularPostsContainer = document.getElementById('popular-posts');
            posts.forEach(post => {
                const listItem = document.createElement('li');
                listItem.textContent = post.title;
                popularPostsContainer.appendChild(listItem);
            });
        })
        .catch(error => console.log(error.message));
}

document.getElementById('category-tabs').addEventListener('click', (event) => {
    if (event.target.tagName === 'BUTTON') {
        const selectedCategory = event.target.getAttribute('data-category');
        currentCategory = selectedCategory; // 현재 카테고리를 선택한 카테고리로 설정
        const offset = 0;
        loadRecentPosts(offset, currentCategory);

        // Update active tab styling
        document.querySelectorAll('#category-tabs .nav-link').forEach(tab => {
            tab.classList.remove('active');
        });
        event.target.classList.add('active');
    }
});

let slideIndex = 1;


const plusSlides = (n) =>
{
    showSlides(slideIndex += n);
}

const currentSlides = (n) =>
{
    showSlides(slideIndex = n);
}

const showSlides = (n) =>
{
    let i;
    let slides = document.getElementsByClassName("mySlides");
    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    slideIndex++;
    if (slideIndex > slides.length) {slideIndex = 1}
    slides[slideIndex-1].style.display = "block";
    setTimeout(showSlides, 5500); // 5.5초마다 바뀜
}

// 유저 롤 변경
async function updateUserRole(newRole) {
    try {
        const response = await fetch('/updateUserRole', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ id: "", role: newRole })
        });
        if (response.ok) {
            alert('업데이트 됏긔');
        } else {
            const errorText = await response.text();  // Get server error response
            alert('Error: ' + errorText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }


}

loadRecentPosts();
loadPopularPosts();
showSlides(slideIndex);