// document.querySelectorAll('.tab-item').forEach(tab => {
//     tab.addEventListener('click', function () {
//         // Remove active class from all tabs
//         document.querySelectorAll('.tab-item').forEach(item => item.classList.remove('active'));
//         // Add active class to clicked tab
//         this.classList.add('active');
//
//         // Hide all tab panels
//         document.querySelectorAll('.tab-panel').forEach(panel => panel.classList.remove('active'));
//         // Show the corresponding panel
//         const tabId = this.getAttribute('data-tab');
//         document.getElementById(tabId).classList.add('active');
//     });
// });