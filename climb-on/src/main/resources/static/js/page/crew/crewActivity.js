async function fetchEventData() {
    try {
        const response = await fetch('/events/crew');
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
            populateEventList(eventData);
        }
        //남의 크루 보여주는 테이블이랑 ㅁ마이크루 분리한다고 치면 이 설정으로  보여져야 함
        // const calendarEl = document.getElementById('crew-calendar');
        // let crewCalendar = new FullCalendar.Calendar(calendarEl, {
        //
        //     headerToolbar: {
        //         left: 'prev,next today',
        //         center: 'title',//dayGridMonth 있을 필요 없을 것 같아서
        //         right: ''
        //     },
        //     height: '700px', // calendar 높이 설정
        //     expandRows: true, // 화면에 맞게 높이 재설정
        //     //navLinks: true, // can click day/week names to navigate views
        //     selectMirror: true,
        //     dayMaxEvents: true,
        //     events: eventData
        // });
        // crewCalendar.render();
    });
});


