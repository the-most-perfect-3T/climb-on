document.addEventListener('DOMContentLoaded', function() {
    let calendarEl = document.getElementById('calendar');
    let eventData;
    let calendar = new FullCalendar.Calendar(calendarEl, {

        customButtons: {
            myCustomButton: {
                text: '일정등록!',
                click: function() {
                    $("#addButton").show();
                    $("#modifyButton").hide();
                    $("#deleteButton").hide();

                    $("#exampleModal").modal("show");
                    $("#title").val("");
                    $("#start").val(new Date().toISOString().substring(0,10));
                    $("#end").val(new Date().toISOString().substring(0,10));
                    $("#color").val("red");

                    //모달창 이벤트
                    $("#saveChanges").on("click", async function () {
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
                            calendar.addEvent(eventData);

                            // Get all events from the calendar
                            let allEvents = calendar.getEvents();
                            let eventsData = allEvents.map(event => ({
                                title: event.title,
                                start: event.start.toISOString(),
                                end: event.end ? event.end.toISOString() : null,
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
                            $("#start").val("");
                            $("#end").val("");
                            $("#color").val("red");
                        }
                    });
                }
            }
        },
        headerToolbar: {
            left: 'prev,next today,myCustomButton',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        height: '700px', // calendar 높이 설정
        expandRows: true, // 화면에 맞게 높이 재설정
        navLinks: true, // can click day/week names to navigate views
        selectable: true,
        selectMirror: true,
        select: async function(arg) {
            const title = prompt('이벤트 이름을 등록해주세요!');
            if (title) {
                calendar.addEvent({
                    title: title,
                    start: arg.start,
                    end: arg.end,
                    backgroundColor: arg.backgroundColor
                })

                // Get all events from the calendar
                let allEvents = calendar.getEvents();
                let eventsData = allEvents.map(event => ({
                    title: event.title,
                    start: event.start.toISOString(),
                    end: event.end ? event.end.toISOString() : null,
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
                    if (!response.ok) {
                        throw new Error("이벤트 저장에 실패했습니다.");
                    }
                }
                catch(error)
                {
                    alert(error.message);
                }
            }
            calendar.unselect();
        },
         eventClick: function(arg)
         {
             $("#addButton").hide();
             $("#modifyButton").show();
             $("#deleteButton").show();
             fixEvents(arg);
         },
        // async function(arg) {
        //     $("#exampleModal").modal("show");
        //     if (confirm('해당 이벤트를 삭제하시겠습니까?')) {
        //         fetch(`/events/${arg.event.id}`, {
        //             method: 'POST'
        //         }).then(response => {
        //             if(response.ok) {
        //                 arg.event.remove();
        //                 alert("성공적으로 삭제되었습니다.");
        //             } else {
        //               alert("삭제에 실패했습니다.");
        //             }
        //         });
        //     }
        // },
        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events
        events: '/events'
    });
    calendar.render();
});

function fixEvents(arg)
{
    // 모달 창에 argument value 넣어줌
    $("#exampleModal").modal("show");
    $("#title").val(arg.event.title);
    $("#start").val(arg.event.start.toISOString().substring(0,10));
    $("#end").val(arg.event.end.toISOString().substring(0,10));
    $("#color").val(arg.event.color.toString());


}