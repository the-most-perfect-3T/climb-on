document.addEventListener('DOMContentLoaded', function() {
    let calendarEl = document.getElementById('calendar');
    let eventData;
    let calendar = new FullCalendar.Calendar(calendarEl, {

        customButtons: {
            myCustomButton: {
                text: '일정등록!',
                click: function() {
                    $("#exampleModal").modal("show");
                    //모달창 이벤트
                    $("#saveChanges").on("click", async function () {
                        eventData = {
                            title: $("#title").val(),
                            start: $("#start").val(),
                            end: $("#end").val(),
                            color: $("#color").val(),
                        };
                        // Check for empty values
                        if (eventData.title === "" || eventData.start === "" || eventData.end === "") {
                            alert("입력하지 않은 값이 있습니다.");
                        } else if (eventData.start > eventData.end) {
                            // Validate start and end dates
                            alert("시간을 잘못입력 하셨습니다.");
                        } else {
                            // Add the event to FullCalendar view
                            calendar.addEvent(eventData);

                            // // Send the event to the database
                            // $.ajax({
                            //     url: '/events',
                            //     method: 'POST',
                            //     contentType: 'application/json',
                            //     data: JSON.stringify(eventData),
                            //     success: function() {
                            //         calendar.refetchEvents(); // Refresh events from the server
                            //     },
                            //     error: function() {
                            //         alert("이벤트 저장에 실패했습니다.");
                            //     }
                            // });

                            // Get all events from the calendar
                            let allEvents = calendar.getEvents();
                            let eventsData = allEvents.map(event => ({
                                title: event.title,
                                start: event.start.toISOString(),
                                end: event.end ? event.end.toISOString() : null,
                                color: event.backgroundColor
                            }));

                            // Save all events to the database in a batch
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

                            // Clear form and close modal
                            $("#exampleModal").modal("hide");
                            $("#title").val("");
                            $("#start").val("");
                            $("#end").val("");
                            $("#color").val("");
                        }
                    });
                }
            },
            mySaveButton: {
                text: '저장!',
                click: async function()
                {
                    if(confirm("저장하시겠습니까?"))
                    {
                        // Get all events from the calendar
                        let allEvents = calendar.getEvents();
                        let eventsData = allEvents.map(event => ({
                            title: event.title,
                            start: event.start.toISOString(),
                            end: event.end ? event.end.toISOString() : null,
                            color: event.backgroundColor
                        }));

                        // Save all events to the database in a batch
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
                    }
                }
            }
        },
        headerToolbar: {
            left: 'prev,next today,myCustomButton,mySaveButton',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        height: '700px', // calendar 높이 설정
        expandRows: true, // 화면에 맞게 높이 재설정
        navLinks: true, // can click day/week names to navigate views
        selectable: true,
        selectMirror: true,
        select: function(arg) {
            var title = prompt('Event Title:');
            if (title) {
                calendar.addEvent({
                    title: title,
                    start: arg.start,
                    end: arg.end,
                    color: arg.backgroundColor
                })
            }
            calendar.unselect()
        },
        eventClick: function(arg) {
            if (confirm('Are you sure you want to delete this event?')) {
                arg.event.remove()
            }
        },
        editable: true,
        dayMaxEvents: true, // allow "more" link when too many events
        events: '/events'
    });
    calendar.render();
});