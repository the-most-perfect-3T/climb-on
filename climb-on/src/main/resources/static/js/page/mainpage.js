const loadRecentPosts = () =>
{
    fetch(`/api/posts/recent/paginated`)
        .then(response => response.json())
        .then(posts => {
            const recentPostsContainer = document.getElementById('recent-posts');
            posts.forEach(post => {
                const row = document.createElement('tr');
                row.innerHTML = `
                            <td>${post.category}</td>
                            <td>${post.title}</td>
                        `;
                recentPostsContainer.appendChild(row);
            });
        });
}

const loadPopularPosts = () =>
{
    fetch(`/api/posts/popular`)
        .then(response => response.json())
        .then(posts => {
            const popularPostsContainer = document.getElementById('popular-posts');
            posts.forEach(post => {
                const listItem = document.createElement('li');
                listItem.textContent = post.title;
                popularPostsContainer.appendChild(listItem);
            });
        })
}

loadRecentPosts();
loadPopularPosts();