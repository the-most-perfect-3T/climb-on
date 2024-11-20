const loadRecentPosts = (category = "") =>
{
    fetch(`/api/posts/recent/paginated?category=${category}`)
        .then(response => {
            if(!response.ok) {
                throw new Error(`${response.status} 에러가 발생했습니다`);
            }
            return response.json();
        })
        .then(posts => {
            console.log(posts);
            const recentPostsContainer = document.getElementById('recent-posts');
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
        loadRecentPosts(currentCategory);

        // Update active tab styling
        document.querySelectorAll('#category-tabs .nav-link').forEach(tab => {
            tab.classList.remove('active');
        });
        event.target.classList.add('active');
    }
});

loadRecentPosts();
loadPopularPosts();