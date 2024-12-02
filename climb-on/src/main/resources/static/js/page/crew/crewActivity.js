async function fetchEventData() {
    try {
        const response = await fetch(`/events/crew?crewCode=${getCrewCode()}`);
        if (!response.ok) {
            throw new Error('Failed to fetch event data');
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching event data:', error);
        alert('이벤트 데이터를 불러오는 데 실패했습니다.');
        return [];
    }
}

document.addEventListener('DOMContentLoaded', function () {
    $('.crew-tab-nav button').on('shown.bs.tab', async function (event) {
        if (event.target.id === 'activity-tab') {
            const eventData = await fetchEventData();
            populateEventList(eventData, getCrewCode() === myCrewCode);
        }
    });
});


