document.addEventListener('DOMContentLoaded', function() {
    let calendarEl = document.getElementById('calendar');
    let eventData;
    // toISOString 했을 때의 시차를 위해 한국 시간 기준으로 맞춰줄 offset
    const offset = new Date().getTimezoneOffset() * 60000;
    // 메인 캘린더
    const mainCalendar = new FullCalendar.Calendar(calendarEl, {

        customButtons: {
            myCustomButton: {
                text: '일정등록',
                click: function() {
                    $("#addButton").show();
                    $("#modifyButton").hide();
                    $("#deleteButton").hide();

                    $("#exampleModal").modal("show");
                    $("#title").val("");
                    $("#start").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                    $("#end").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                    $("#color").val("red");

                    //모달창 이벤트
                    $("#addButton").off("click").on("click", async function () {
                        eventData = {
                            title: $("#title").val(),
                            start: $("#start").val(),
                            end: $("#end").val(),
                            color: $("#color").val()
                        };
                        // Check for empty values
                        if (eventData.title === "" || eventData.start === "" || eventData.end === "") {
                            alert("입력하지 않은 값이 있습니다.");
                        } else if (eventData.start > eventData.end) {
                            // Validate start and end dates
                            alert("시간을 잘못입력 하셨습니다.");
                        } else {
                            // 캘린더 뷰에 데이터 저장
                            mainCalendar.addEvent(eventData);

                            // Get all events from the calendar
                            let allEvents = mainCalendar.getEvents();
                            let eventsData = allEvents.map(event => ({
                                title: event.title,
                                start: new Date(new Date(event.start).getTime() - offset).toISOString(),
                                end: event.end ? new Date(new Date(event.end).getTime() - offset).toISOString() : null,
                                backgroundColor: event.backgroundColor
                            }));

                            // 모든 이벤트 저장
                            try
                            {
                                const response = await fetch('/events/batch', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    body: JSON.stringify(eventsData)
                                });
                                if (response.ok) {
                                    //await calendar.refetchEvents(); // Refresh events from the server
                                    //등록 완
                                } else {
                                    throw new Error("이벤트 저장에 실패했습니다.");
                                }
                            }
                            catch(error)
                            {
                                alert(error.message);
                            }

                            // 모달 창 초기화
                            $("#exampleModal").modal("hide");
                            $("#title").val("");
                            $("#start").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                            $("#end").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                            $("#color").val("red");
                        }
                    });
                }
            }
        },
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'myCustomButton' //dayGridMonth 있을 필요 없을 것 같아서
        },
        height: '700px', // calendar 높이 설정
        expandRows: true, // 화면에 맞게 높이 재설정
        //navLinks: true, // can click day/week names to navigate views
        selectable: true,
        selectMirror: true,
        select: async function(arg) {
            const title = prompt('이벤트 이름을 등록해주세요!');
            if (title) {
                mainCalendar.addEvent({
                    title: title,
                    start: arg.start,
                    end: arg.end,
                    backgroundColor: arg.backgroundColor
                })

                // 캘린 더 내 모든 이벤트 저장
                let allEvents = mainCalendar.getEvents();
                let eventsData = allEvents.map(event => ({
                    title: event.title,
                    start: new Date(new Date(event.start).getTime() - offset),
                    end: event.end ? new Date(new Date(event.end).getTime() - offset) : null,
                    backgroundColor: event.backgroundColor
                }));

                /**
                 * @todo 저장 로직 수정해야 할 것 같음 중복체크랑 같이 테스트 하면서 손봐야함
                 */
                try
                {
                    const response = await fetch('/events/batch', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(eventsData)
                    });
                    // Check if the response was successful
                    if (!response.ok) {
                        throw new Error("이벤트 저장에 실패했습니다.");
                    }
                }
                catch(error)
                {
                    alert(error.message);
                }
            }
            mainCalendar.unselect();
        },
        eventClick: function(arg)         {
            $("#addButton").hide();
            $("#modifyButton").show();
            $("#deleteButton").show();

            // 모달 창에 argument value 넣어줌
            $("#exampleModal").modal("show");
            $("#title").val(arg.event.title);
            $("#start").val(arg.event.start.toISOString().substring(0,10));
            $("#end").val(arg.event.end ? arg.event.end.toISOString().substring(0,10) : "");
            $("#color").val(arg.event.color);

            //수정 버튼 클릭했을 때
            $("#modifyButton").off("click").on("click", async function(){
                arg.event.setProp('title', $("#title").val());
                arg.event.setStart($("#start").val());
                arg.event.setEnd($("#end").val());
                arg.event.setProp('backgroundColor', $("#color").val());

                let eventData  = ({
                    title: arg.event.title,
                    start: arg.event.start.toISOString(),
                    end: arg.event.end ? arg.event.end.toISOString() : null,
                    backgroundColor: arg.event.backgroundColor
                });

                try
                {
                    // Save all events to the database in a batch using the fetch API
                    const response = await fetch('/events/modify', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(eventData)
                    });
                    // Check if the response was successful
                    if (!response.ok) {
                        throw new Error("이벤트 수정에 실패했습니다.");
                    }
                }
                catch(error)
                {
                    alert(error.message);
                }

                $("#exampleModal").modal("hide");
            });

            //삭제 버튼 클릭했을 때
            $("#deleteButton").off("click").on("click", async function() {
                if (confirm('해당 이벤트를 삭제하시겠습니까?')) {
                    await fetch(`/events/${arg.event.id}`, {
                        method: 'POST'
                    }).then(response => {
                        if (response.ok) {
                            arg.event.remove();
                            alert("성공적으로 삭제되었습니다.");
                        } else {
                            alert("삭제에 실패했습니다.");
                        }
                    });
                    $("#exampleModal").modal("hide");
                }
            });
        },
        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events
        events: '/events?type=main'
    });
    mainCalendar.render();

    // 크루 캘린더
    const crewCalendar  = new FullCalendar.Calendar(calendarEl, {

        customButtons: {
            myCustomButton: {
                text: '일정등록',
                click: function() {
                    $("#addButton").show();
                    $("#modifyButton").hide();
                    $("#deleteButton").hide();

                    $("#exampleModal").modal("show");
                    $("#title").val("");
                    $("#start").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                    $("#end").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                    $("#color").val("red");

                    //모달창 이벤트
                    $("#addButton").off("click").on("click", async function () {
                        eventData = {
                            title: $("#title").val(),
                            start: $("#start").val(),
                            end: $("#end").val(),
                            color: $("#color").val()
                        };
                        // Check for empty values
                        if (eventData.title === "" || eventData.start === "" || eventData.end === "") {
                            alert("입력하지 않은 값이 있습니다.");
                        } else if (eventData.start > eventData.end) {
                            // Validate start and end dates
                            alert("시간을 잘못입력 하셨습니다.");
                        } else {
                            // 캘린더 뷰에 데이터 저장
                            crewCalendar.addEvent(eventData);

                            // Get all events from the calendar
                            let allEvents = crewCalendar.getEvents();
                            let eventsData = allEvents.map(event => ({
                                title: event.title,
                                start: new Date(new Date(event.start).getTime() - offset).toISOString(),
                                end: event.end ? new Date(new Date(event.end).getTime() - offset).toISOString() : null,
                                backgroundColor: event.backgroundColor
                            }));

                            // 모든 이벤트 저장
                            try
                            {
                                // Save all events to the database in a batch using the fetch API
                                const response = await fetch('/events/crew', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    body: JSON.stringify(eventsData)
                                });
                                // Check if the response was successful
                                if (response.ok) {
                                    //await calendar.refetchEvents(); // Refresh events from the server
                                    //등록 완
                                } else {
                                    throw new Error("이벤트 저장에 실패했습니다.");
                                }
                            }
                            catch(error)
                            {
                                alert(error.message);
                            }

                            // 모달 창 초기화
                            $("#exampleModal").modal("hide");
                            $("#title").val("");
                            $("#start").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                            $("#end").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                            $("#color").val("red");
                        }
                    });
                }
            }
        },
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'myCustomButton' //dayGridMonth 있을 필요 없을 것 같아서
        },
        height: '700px', // calendar 높이 설정
        expandRows: true, // 화면에 맞게 높이 재설정
        //navLinks: true, // can click day/week names to navigate views
        selectable: true,
        selectMirror: true,
        select: async function(arg) {
            const title = prompt('이벤트 이름을 등록해주세요!');
            if (title) {
                crewCalendar.addEvent({
                    title: title,
                    start: arg.start,
                    end: arg.end,
                    backgroundColor: arg.backgroundColor
                })

                // 캘린 더 내 모든 이벤트 저장
                let allEvents = crewCalendar.getEvents();
                let eventsData = allEvents.map(event => ({
                    title: event.title,
                    start: new Date(new Date(event.start).getTime() - offset),
                    end: event.end ? new Date(new Date(event.end).getTime() - offset) : null,
                    backgroundColor: event.backgroundColor
                }));

                /**
                 * @todo 저장 로직 수정해야 할 것 같음 중복체크랑 같이 테스트 하면서 손봐야함
                 */
                try
                {
                    const response = await fetch('/events/private', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(eventsData)
                    });
                    // Check if the response was successful
                    if (!response.ok) {
                        throw new Error("이벤트 저장에 실패했습니다.");
                    }
                }
                catch(error)
                {
                    alert(error.message);
                }
            }
            crewCalendar.unselect();
        },
        eventClick: function(arg)         {
            $("#addButton").hide();
            $("#modifyButton").show();
            $("#deleteButton").show();

            // 모달 창에 argument value 넣어줌
            $("#exampleModal").modal("show");
            $("#title").val(arg.event.title);
            $("#start").val(arg.event.start.toISOString().substring(0,10));
            $("#end").val(arg.event.end ? arg.event.end.toISOString().substring(0,10) : "");
            $("#color").val(arg.event.color);

            //수정 버튼 클릭했을 때
            $("#modifyButton").off("click").on("click", async function(){
                arg.event.setProp('title', $("#title").val());
                arg.event.setStart($("#start").val());
                arg.event.setEnd($("#end").val());
                arg.event.setProp('backgroundColor', $("#color").val());

                let eventData  = ({
                    title: arg.event.title,
                    start: arg.event.start.toISOString(),
                    end: arg.event.end ? arg.event.end.toISOString() : null,
                    backgroundColor: arg.event.backgroundColor
                });

                try
                {
                    // Save all events to the database in a batch using the fetch API
                    const response = await fetch('/events/modify', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(eventData)
                    });
                    // Check if the response was successful
                    if (!response.ok) {
                        throw new Error("이벤트 수정에 실패했습니다.");
                    }
                }
                catch(error)
                {
                    alert(error.message);
                }

                $("#exampleModal").modal("hide");
            });

            //삭제 버튼 클릭했을 때
            $("#deleteButton").off("click").on("click", async function() {
                if (confirm('해당 이벤트를 삭제하시겠습니까?')) {
                    await fetch(`/events/${arg.event.id}`, {
                        method: 'POST'
                    }).then(response => {
                        if (response.ok) {
                            arg.event.remove();
                            alert("성공적으로 삭제되었습니다.");
                        } else {
                            alert("삭제에 실패했습니다.");
                        }
                    });
                    $("#exampleModal").modal("hide");
                }
            });
        },
        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events
        events: '/events?type=crew'
    });
    crewCalendar.render();

    const privateCalendar  = new FullCalendar.Calendar(calendarEl, {

        customButtons: {
            myCustomButton: {
                text: '일정등록',
                click: function() {
                    $("#addButton").show();
                    $("#modifyButton").hide();
                    $("#deleteButton").hide();

                    $("#exampleModal").modal("show");
                    $("#title").val("");
                    $("#start").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                    $("#end").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                    $("#color").val("red");

                    //모달창 이벤트
                    $("#addButton").off("click").on("click", async function () {
                        eventData = {
                            title: $("#title").val(),
                            start: $("#start").val(),
                            end: $("#end").val(),
                            color: $("#color").val()
                        };
                        // Check for empty values
                        if (eventData.title === "" || eventData.start === "" || eventData.end === "") {
                            alert("입력하지 않은 값이 있습니다.");
                        } else if (eventData.start > eventData.end) {
                            // Validate start and end dates
                            alert("시간을 잘못입력 하셨습니다.");
                        } else {
                            // 캘린더 뷰에 데이터 저장
                            privateCalendar.addEvent(eventData);

                            // Get all events from the calendar
                            let allEvents = privateCalendar.getEvents();
                            let eventsData = allEvents.map(event => ({
                                title: event.title,
                                start: new Date(new Date(event.start).getTime() - offset).toISOString(),
                                end: event.end ? new Date(new Date(event.end).getTime() - offset).toISOString() : null,
                                backgroundColor: event.backgroundColor
                            }));

                            // 모든 이벤트 저장
                            try
                            {
                                // Save all events to the database in a batch using the fetch API
                                const response = await fetch('/events/batch', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    body: JSON.stringify(eventsData)
                                });
                                // Check if the response was successful
                                if (response.ok) {
                                    //await calendar.refetchEvents(); // Refresh events from the server
                                    //등록 완
                                } else {
                                    throw new Error("이벤트 저장에 실패했습니다.");
                                }
                            }
                            catch(error)
                            {
                                alert(error.message);
                            }

                            // 모달 창 초기화
                            $("#exampleModal").modal("hide");
                            $("#title").val("");
                            $("#start").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                            $("#end").val(new Date(Date.now()-offset).toISOString().substring(0,10));
                            $("#color").val("red");
                        }
                    });
                }
            }
        },
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'myCustomButton' //dayGridMonth 있을 필요 없을 것 같아서
        },
        height: '700px', // calendar 높이 설정
        expandRows: true, // 화면에 맞게 높이 재설정
        //navLinks: true, // can click day/week names to navigate views
        selectable: true,
        selectMirror: true,
        select: async function(arg) {
            const title = prompt('이벤트 이름을 등록해주세요!');
            if (title) {
                privateCalendar.addEvent({
                    title: title,
                    start: arg.start,
                    end: arg.end,
                    backgroundColor: arg.backgroundColor
                })

                // 캘린 더 내 모든 이벤트 저장
                let allEvents = privateCalendar.getEvents();
                let eventsData = allEvents.map(event => ({
                    title: event.title,
                    start: new Date(new Date(event.start).getTime() - offset),
                    end: event.end ? new Date(new Date(event.end).getTime() - offset) : null,
                    backgroundColor: event.backgroundColor
                }));

                /**
                 * @todo 저장 로직 수정해야 할 것 같음 중복체크랑 같이 테스트 하면서 손봐야함
                 */
                try
                {
                    const response = await fetch('/events/batch', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(eventsData)
                    });
                    // Check if the response was successful
                    if (!response.ok) {
                        throw new Error("이벤트 저장에 실패했습니다.");
                    }
                }
                catch(error)
                {
                    alert(error.message);
                }
            }
            privateCalendar.unselect();
        },
        eventClick: function(arg)         {
            $("#addButton").hide();
            $("#modifyButton").show();
            $("#deleteButton").show();

            // 모달 창에 argument value 넣어줌
            $("#exampleModal").modal("show");
            $("#title").val(arg.event.title);
            $("#start").val(arg.event.start.toISOString().substring(0,10));
            $("#end").val(arg.event.end ? arg.event.end.toISOString().substring(0,10) : "");
            $("#color").val(arg.event.color);

            //수정 버튼 클릭했을 때
            $("#modifyButton").off("click").on("click", async function(){
                arg.event.setProp('title', $("#title").val());
                arg.event.setStart($("#start").val());
                arg.event.setEnd($("#end").val());
                arg.event.setProp('backgroundColor', $("#color").val());

                let eventData  = ({
                    title: arg.event.title,
                    start: arg.event.start.toISOString(),
                    end: arg.event.end ? arg.event.end.toISOString() : null,
                    backgroundColor: arg.event.backgroundColor
                });

                try
                {
                    // Save all events to the database in a batch using the fetch API
                    const response = await fetch('/events/modify', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(eventData)
                    });
                    // Check if the response was successful
                    if (!response.ok) {
                        throw new Error("이벤트 수정에 실패했습니다.");
                    }
                }
                catch(error)
                {
                    alert(error.message);
                }

                $("#exampleModal").modal("hide");
            });

            //삭제 버튼 클릭했을 때
            $("#deleteButton").off("click").on("click", async function() {
                if (confirm('해당 이벤트를 삭제하시겠습니까?')) {
                    await fetch(`/events/${arg.event.id}`, {
                        method: 'POST'
                    }).then(response => {
                        if (response.ok) {
                            arg.event.remove();
                            alert("성공적으로 삭제되었습니다.");
                        } else {
                            alert("삭제에 실패했습니다.");
                        }
                    });
                    $("#exampleModal").modal("hide");
                }
            });
        },
        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events
        events: '/events?type=private'
    });
    privateCalendar.render();

    // privateCalendar.refetchEvents()

    // 탭 초기화 테스트
    let triggerTabList = [].slice.call(document.querySelectorAll('a[data-bs-toggle="tab"]'))
    triggerTabList.forEach(function (triggerEl) {
        let tabTrigger = new bootstrap.Tab(triggerEl)
        triggerEl.addEventListener('hidden.bs.tab', function (event) {

            mainCalendar.updateSize();
            crewCalendar.updateSize();

        })
    })

    // function initCalendar()
    // {
    //     mainCalendar.updateSize();
    //     crewCalendar.updateSize();
    // }
    //
    // $(function() {
    //     let calendarInit = false;
    //     $('#activity-tab').on('shown.bs.tab', function (e) {
    //         if (!calendarInit) {
    //             initCalendar();
    //             //initCalendarDragNDrop();
    //             calendarInit = true;
    //         }
    //     });
    // });
});



