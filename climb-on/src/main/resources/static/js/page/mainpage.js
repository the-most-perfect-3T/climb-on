const loadNotificationPosts = () =>
{
    fetch(`/api/posts/notification`)
        .then(response => {
            if(!response.ok){
                throw new Error(`${response.status} 에러가 발생했습니다`);
            }
            return response.json();
        })
        .then(posts => {
            const notificationContainer = document.getElementById('notifiEventList');
            posts.forEach(post=>{
                const listItem = document.createElement('li');
                listItem.textContent = post.title;
                listItem.addEventListener("click", (e) => {
                    window.location.href = `/community/${post.id}`;
                })
                notificationContainer.appendChild(listItem);
            });
        })
        .catch(error => console.log(error.message));
}

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
                row.addEventListener("click", (e) => {
                    window.location.href = `/community/${post.id}`;
                })
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
                console.log(response.json());
                throw new Error(`${response.status} 에러가 발생했습니다`);
            }
            return response.json();
        })
        .then(posts => {
            const popularPostsContainer = document.getElementById('popular-posts');

            for (let i = 0; i < posts.length; i++) {
                const listItem = document.createElement('li');
                listItem.textContent = (i+1) + "　" + posts[i].title;
                listItem.addEventListener("click", (e) => {
                    window.location.href = `/community/${posts[i].id}`;
                })
                popularPostsContainer.appendChild(listItem);
            }
        })
        .catch(error => console.log(error.message));
}

const loadFacilityInfo = async () => {

    const facilityInfo = await fetch('/api/posts/facilities')
        .then(response => {
            if (!response.ok) {
                throw new Error(`${response.status} 에러가 발생했습니다`);
            }
            return response.json();
        })
        .then(async facilities => {
            const facilityList = document.getElementById('recommendedFacility');
            facilityList.innerHTML = '';
            for (const facility of facilities) {
                const facilityItem = document.createElement('div');
                facilityItem.className = 'facility-item';
                facilityItem.style.backgroundImage = `url(${facility.imageUrl})`;
                facilityItem.innerHTML = `
                    <span class="rating">
                        <i class="fa-solid fa-star"></i>
                        <span>${await getFacility(facility.id)}</span>
                    </span>
                    <p>${facility.facilityName}</p>
                `;
                facilityItem.addEventListener("click", (e) => {
                    window.location.href = `facilities/select`;
                })
                //                        rating 가져와서 ${facility.rating}
                facilityList.appendChild(facilityItem);
            }
        })
        .catch(error => console.log(error.message));  // 핸들 에러
};

document.getElementById('category-tabs').addEventListener('click', (event) => {
    if (event.target.tagName === 'BUTTON') {
        const selectedCategory = event.target.getAttribute('data-category');
        const offset = 0;
        loadRecentPosts(offset, selectedCategory);

        // Update active tab styling
        document.querySelectorAll('#category-tabs .nav-link').forEach(tab => {
            tab.classList.remove('active');
        });
        event.target.classList.add('active');
    }
});

let slideIndex = 1;
let autoSlideTimeout; // 시간 세는 변수
const plusSlides = (n) =>
{
    slideIndex += n;
    showSlides(slideIndex);
    resetAutoSlides();
}

const showSlides = (n) =>
{
    let i;
    let slides = document.getElementsByClassName("mySlides");
    if (n > slides.length) {slideIndex = 1}
    if (n < 1) {slideIndex = slides.length}
    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    slides[slideIndex-1].style.display = "block";
}

const autoSlides = () =>
{
    slideIndex++;
    showSlides(slideIndex);
    autoSlideTimeout = setTimeout(autoSlides, 6000); // 6초마다 바뀜
}

const resetAutoSlides = () =>
{
    clearTimeout(autoSlideTimeout);
    autoSlides();
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
            alert('유저 권한 업데이트');
        } else {
            const errorText = await response.text();  // Get server error response
            alert('Error: ' + errorText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

async function getFacility(id) {
    const test = await fetch(`/api/posts/rate/${id}`).then(response => {
        return response.text()
    });
    console.log(test);
    return test;
}

loadRecentPosts();
loadPopularPosts();
loadNotificationPosts();
loadFacilityInfo();
showSlides(slideIndex);