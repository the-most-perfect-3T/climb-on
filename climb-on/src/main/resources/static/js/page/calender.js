function setToMidnightKST(dateTime) {
    // 한국 표준 시로 맞추기
    const date = new Date(dateTime);
    date.setHours(date.getHours() + 9);
    date.setUTCHours(0, 0, 0, 0);
    return date.toISOString().slice(0, 16);
}

function getCrewCode() {
    const pathSegments = window.location.pathname.split('/');
    const crewCodeIndex = pathSegments.indexOf('myCrew');

    if (crewCodeIndex > -1 && crewCodeIndex + 1 < pathSegments.length) {
        return parseInt(pathSegments[crewCodeIndex + 1]); // Extract crew code
    }
    return null;
}

// toISOString 했을 때의 시차를 위해 한국 시간 기준으로 맞춰줄 offset
const offset = new Date().getTimezoneOffset() * 60000;
let events = null;
let myCrewCode= 0;

async function showModal(calendar){
    $("#addButton").show();
    $("#modifyButton").hide();
    $("#deleteButton").hide();

    $("#calendarModal").modal("show");
    $("#title").val("");
    $("#start").val(new Date(Date.now() - offset).toISOString().substring(0,16));
    $("#end").val(new Date(Date.now() - offset).toISOString().substring(0,16));
    $("#location").val("");
    $("#color").val("red");

    //모달창 이벤트
    $("#addButton").off("click").on("click", async function() {
        let eventData = {
            title: $("#title").val(),
            start: $("#start").val(),
            end: $("#end").val(),
            location: $("#location").val(),
            backgroundColor: $("#color").val()
        };

        if (eventData.title === "" || eventData.start === "" || eventData.end === "") {
            alert("입력하지 않은 값이 있습니다.");
        } else if (eventData.start > eventData.end) {
            alert("시간을 잘못입력 하셨습니다.");
        } else {
            try {
                const response = await fetch('/events/batch', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(eventData)
                });

                if (response.ok) {
                    await calendar.refetchEvents(); // Refresh events from the server
                    calendar.addEventSource(eventData);
                    events = eventData;
                    console.log(eventData);
                } else {
                    throw new Error("이벤트 저장에 실패했습니다.");
                }
            } catch (error) {
                alert(error.message);
            }
            // 모달 창 초기화
            $("#calendarModal").modal("hide");
            $("#title").val("");
            $("#start").val(new Date(Date.now() - offset).toISOString().substring(0, 16));
            $("#end").val(new Date(Date.now() - offset).toISOString().substring(0, 16));
            $("#location").val("");
            $("#color").val("red");
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    let calendarMainE1 = document.getElementById('main-calendar');
    let calendarCrewE1 = document.getElementById('crew-calendar');
    let calendarCrewE2 = document.getElementById('crew-calendar-activity');
    let calendarMyE1 = document.getElementById('my-calendar');

    let isAdmin = false;


    // 메인 캘린더
    if (calendarMainE1) {
        fetch(`/events/main`, {
            method: 'GET'})
            .then(res => res.json())
            .then(data => {
                return data;
            })
            .catch(error => {
                console.error('Error:', error);
            });

        $.ajax(
            {
                url: "/api/user/permissions",
                type: "GET",
                success: function (response) {
                    console.log("reponse Admin: " + response.isAdmin);
                    if (response.isAdmin) {
                        // 관리자 검사 후 캘린더에 옵션 넣어서 렌더링

                        mainCalendar.batchRendering(function () {
                            mainCalendar.setOption('headerToolbar', {
                                left: 'prev,next today',
                                center: 'title',
                                right: 'myCustomButton'
                            });
                            mainCalendar.setOption('selectable', true);
                            mainCalendar.setOption('editable', true);
                            mainCalendar.setOption("eventClick", function (arg) {
                                $("#addButton").hide();
                                $("#modifyButton").show();
                                $("#deleteButton").show();

                                // 모달 창에 argument value 넣어줌
                                $("#calendarModal").modal("show");
                                $("#title").val(arg.event.title);
                                $("#start").val(arg.event.start.toISOString().substring(0, 16));
                                $("#end").val(arg.event.end ? arg.event.end.toISOString().substring(0, 16) : "");
                                $("#location").val(arg.event.location);
                                $("#color").val(arg.event.color);

                                //수정 버튼 클릭했을 때
                                $("#modifyButton").off("click").on("click", async function () {
                                    arg.event.setProp('title', $("#title").val());
                                    arg.event.setStart($("#start").val());
                                    arg.event.setEnd($("#end").val());
                                    arg.event.setExtendedProp('location', $("#location").val());
                                    arg.event.setProp('backgroundColor', $("#color").val());

                                    let eventData = ({
                                        id: arg.event.id,
                                        title: arg.event.title,
                                        start: arg.event.start.toISOString(),
                                        end: arg.event.end ? arg.event.end.toISOString() : null,
                                        location: arg.event.location,
                                        backgroundColor: arg.event.backgroundColor
                                    });

                                    try {
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
                                    } catch (error) {
                                        alert(error.message);
                                    }

                                    $("#calendarModal").modal("hide");
                                });

                                //삭제 버튼 클릭했을 때
                                $("#deleteButton").off("click").on("click", async function () {
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
                                        $("#calendarModal").modal("hide");
                                    }
                                });
                            })
                            mainCalendar.setOption('select', async function (arg) {
                                await showModal(mainCalendar)
                                mainCalendar.unselect();
                            })
                        });
                    }
                },
                error: function (error) {
                    console.log("you got error: " + error);
                }
            }
        )

        let mainCalendar = new FullCalendar.Calendar(calendarMainE1, {
            customButtons: {
                myCustomButton: {
                    text: '일정등록',
                    click: async function () {
                            await showModal(mainCalendar);
                    }
                }
            }, // 얘도 관리자 권한
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',//dayGridMonth 있을 필요 없을 것 같아서
                right: ''
            },
            height: '700px', // calendar 높이 설정
            expandRows: true, // 화면에 맞게 높이 재설정
            //navLinks: true, // can click day/week names to navigate views
            selectable: isAdmin, // 사용자 설정
            selectMirror: true,
            editable: isAdmin, // 얘도 관리자 권한
            dayMaxEvents: true, // allow "more" link when too many events
            events: '/events/main'
        });
        mainCalendar.render();
    }

    // 크루 캘린더
    if (calendarCrewE1) {
        $.ajax(
            {
                url: "/api/user/crewcode",
                type: "GET",
                success: function (response) {
                    const crewCode = response.crewCode;
                    myCrewCode = crewCode;
                    if (getCrewCode() == null && crewCode) {
                        fetch(`/events/myCrew?crewCode=${crewCode}`, {
                            method: 'GET',
                        })
                            .then(response => {
                                if (!response.ok) throw new Error('크루 이벤트 불러오기에 실패하다 ... ');
                                return response.json();
                            })
                            .then(data => {
                                console.log(data);
                                crewCalendar.addEventSource(data);
                                popluateMainEventInMycrewHome(data, myCrewCode===getCrewCode());
                            })
                            .catch(error => {
                                console.error('Error:', error);
                            });

                        crewCalendar.batchRendering(function () {
                            crewCalendar.setOption('headerToolbar', {
                                left: 'prev,next today',
                                center: 'title',
                                right: 'myCustomButton'
                            });
                            crewCalendar.setOption('editable', true);
                        });
                        crewCalendar.refetchEvents();
                    } else {
                        fetch(`/events/myCrew?crewCode=${getCrewCode()}`, {
                            method: 'GET',
                        })
                            .then(response => {
                                if (!response.ok) throw new Error('크루 이벤트 불러오기에 실패하다 ... ');
                                return response.json();
                            })
                            .then(data => {
                                console.log(data);
                                crewCalendar.addEventSource(data);
                                popluateMainEventInMycrewHome(data, myCrewCode===getCrewCode());
                            })
                            .catch(error => {
                                console.error('Error:', error);
                            });
                        crewCalendar.batchRendering(function () {
                            crewCalendar.setOption('headerToolbar', {
                                left: 'prev,next today',
                                center: 'title',
                                right: ''
                            });
                            crewCalendar.setOption('selectable', false);
                            crewCalendar.setOption('editable', false);
                        });
                        crewCalendar.refetchEvents();
                    }
                },
                error: function (error) {
                    console.log("you got error: " + error);
                }
            }
        )
        console.log("you get crew calendar");
        const crewCalendar = new FullCalendar.Calendar(calendarCrewE1, {

            customButtons: {
                myCustomButton: {
                    text: '일정등록',
                    click: async function () {
                        await showModal(crewCalendar);
                    }
                }
            },
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: '' //dayGridMonth 있을 필요 없을 것 같아서
            },
            height: '700px', // calendar 높이 설정
            expandRows: true, // 화면에 맞게 높이 재설정
            //navLinks: true, // can click day/week names to navigate views
            selectable: false,
            selectMirror: true,
            select: async function (arg) {
                await showModal(crewCalendar)
                crewCalendar.unselect();
            },
            eventClick: function (arg) {
                $("#addButton").hide();
                $("#modifyButton").show();
                $("#deleteButton").show();

                // 모달 창에 argument value 넣어줌
                $("#calendarModal").modal("show");
                $("#title").val(arg.event.title);
                $("#start").val(arg.event.start.toISOString().substring(0, 10));
                $("#end").val(arg.event.end ? arg.event.end.toISOString().substring(0, 10) : "");
                $("#location").val(arg.event.location);
                $("#color").val(arg.event.color);

                //수정 버튼 클릭했을 때
                $("#modifyButton").off("click").on("click", async function () {
                    arg.event.setProp('title', $("#title").val());
                    arg.event.setStart($("#start").val());
                    arg.event.setEnd($("#end").val());
                    arg.event.setExtendedProp('location', $("#location").val());
                    arg.event.setProp('backgroundColor', $("#color").val());

                    let eventData = ({
                        id: arg.event.id,
                        title: arg.event.title,
                        start: arg.event.start.toISOString(),
                        end: arg.event.end ? arg.event.end.toISOString() : null,
                        location: arg.event.location,
                        backgroundColor: arg.event.backgroundColor
                    });

                    try {
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
                    } catch (error) {
                        alert(error.message);
                    }

                    $("#calendarModal").modal("hide");
                });

                //삭제 버튼 클릭했을 때
                $("#deleteButton").off("click").on("click", async function () {
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
                        $("#calendarModal").modal("hide");
                    }
                });
            },
            editable: false,
            dayMaxEvents: true, // allow "more" link when too many events
            events: '/events/myCrew'
        });
        crewCalendar.render();

        $('.crew-tab-nav li').click(function () {
            if (crewCalendar) {
                crewCalendar.updateSize();
                crewCalendar.refetchEvents();
            }
        });

    }
    if (calendarCrewE2) {
        $.ajax(
            {
                url: "/api/user/crewcode",
                type: "GET",
                success: function (response) {
                    const crewCode = response.crewCode;
                    console.log("your crew code: " + crewCode);
                    if (getCrewCode() == null && crewCode) {
                        fetch(`/events/myCrew?crewCode=${crewCode}`, {
                            method: 'GET',
                        })
                            .then(response => {
                                if (!response.ok)
                                {
                                    console.log(response);
                                    throw new Error('크루 이벤트 불러오기에 실패하다 ... ');
                                }

                                return response.json();
                            })
                            .then(data => {
                                console.log(data);
                                events = data;
                                crewCalendar.addEventSource(data)

                            })
                            .catch(error => {
                                console.error('Error:', error);
                            });

                        crewCalendar.batchRendering(function () {
                            crewCalendar.setOption('headerToolbar', {
                                left: 'prev,next today',
                                center: 'title',
                                right: 'myCustomButton'
                            });
                            crewCalendar.setOption('editable', true);
                        });
                        crewCalendar.refetchEvents();
                    } else {
                        fetch(`/events/myCrew?crewCode=${getCrewCode()}`, {
                            method: 'GET',
                        })
                            .then(response => {
                                if (!response.ok) throw new Error('크루 이벤트 불러오기에 실패하다 ... ');
                                return response.json();
                            })
                            .then(data => {
                                console.log(data);
                                crewCalendar.addEventSource(data);
                                popluateMainEventInMycrewHome(data, myCrewCode===getCrewCode());
                            })
                            .catch(error => {
                                console.error('Error:', error);
                            });
                        crewCalendar.batchRendering(function () {
                            crewCalendar.setOption('headerToolbar', {
                                left: 'prev,next today',
                                center: 'title',
                                right: ''
                            });
                            crewCalendar.setOption('selectable', false);
                            crewCalendar.setOption('editable', false);
                        });
                        crewCalendar.refetchEvents();
                    }
                },
                error: function (error) {
                    console.log("you got error: " + error);
                }
            }
        )
        console.log("you get crew calendar");
        const crewCalendar = new FullCalendar.Calendar(calendarCrewE2, {

            customButtons: {
                myCustomButton: {
                    text: '일정등록',
                    click: async function () {
                        await showModal(crewCalendar);
                    }
                }
            },
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: '' //dayGridMonth 있을 필요 없을 것 같아서
            },
            height: '700px', // calendar 높이 설정
            expandRows: true, // 화면에 맞게 높이 재설정
            //navLinks: true, // can click day/week names to navigate views
            selectable: false,
            selectMirror: true,
            select: async function (arg) {
                await showModal(crewCalendar);
                crewCalendar.unselect();
            },
            eventClick: function (arg) {
                $("#addButton").hide();
                $("#modifyButton").show();
                $("#deleteButton").show();

                // 모달 창에 argument value 넣어줌
                $("#calendarModal").modal("show");
                $("#title").val(arg.event.title);
                $("#start").val(arg.event.start.toISOString().substring(0, 10));
                $("#end").val(arg.event.end ? arg.event.end.toISOString().substring(0, 10) : "");
                $("#location").val(arg.event.location)
                $("#color").val(arg.event.color);

                //수정 버튼 클릭했을 때
                $("#modifyButton").off("click").on("click", async function () {
                    arg.event.setProp('title', $("#title").val());
                    arg.event.setStart($("#start").val());
                    arg.event.setEnd($("#end").val());
                    arg.event.setExtendedProp('location', $("#location").val());
                    arg.event.setProp('backgroundColor', $("#color").val());

                    let eventData = ({
                        id: arg.event.id,
                        title: arg.event.title,
                        start: arg.event.start.toISOString(),
                        end: arg.event.end ? arg.event.end.toISOString() : null,
                        location: arg.event.location,
                        backgroundColor: arg.event.backgroundColor
                    });

                    try {
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
                    } catch (error) {
                        alert(error.message);
                    }

                    $("#calendarModal").modal("hide");
                });

                //삭제 버튼 클릭했을 때
                $("#deleteButton").off("click").on("click", async function () {
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
                        $("#calendarModal").modal("hide");
                    }
                });
            },
            editable: false,
            dayMaxEvents: true, // allow "more" link when too many events
            events: '/events/myCrew'
        });
        crewCalendar.render();

        $('.crew-tab-nav li').click(function () {
            if (crewCalendar) {
                populateEventList(events, myCrewCode===getCrewCode());
                crewCalendar.updateSize();
                crewCalendar.refetchEvents();
            }
        });
    }

    // 개인 캘린더
    if (calendarMyE1) {
        const privateCalendar = new FullCalendar.Calendar(calendarMyE1, {

            customButtons: {
                myCustomButton: {
                    text: '일정등록',
                    click: async function () {
                        await showModal(privateCalendar);
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
            select: async function (arg) {
                await showModal(privateCalendar);
                privateCalendar.unselect();
            },
            eventClick: function (arg) {
                $("#addButton").hide();
                $("#modifyButton").show();
                $("#deleteButton").show();

                // 모달 창에 argument value 넣어줌
                $("#calendarModal").modal("show");
                $("#title").val(arg.event.title);
                $("#start").val(arg.event.start.toISOString().substring(0, 16));
                $("#end").val(arg.event.end ? arg.event.end.toISOString().substring(0, 16) : "");
                $("#location").val(arg.event.location);
                $("#color").val(arg.event.color);

                //수정 버튼 클릭했을 때
                $("#modifyButton").off("click").on("click", async function () {
                    arg.event.setProp('title', $("#title").val());
                    arg.event.setStart($("#start").val());
                    arg.event.setEnd($("#end").val());
                    arg.event.setExtendedProp('location', $("#location").val());
                    arg.event.setProp('backgroundColor', $("#color").val());

                    let eventData = ({
                        id: arg.event.id,
                        title: arg.event.title,
                        start: arg.event.start.toISOString(),
                        end: arg.event.end ? arg.event.end.toISOString() : null,
                        location: arg.event.location,
                        backgroundColor: arg.event.backgroundColor
                    });

                    try {
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
                    } catch (error) {
                        alert(error.message);
                    }

                    $("#calendarModal").modal("hide");
                });

                //삭제 버튼 클릭했을 때
                $("#deleteButton").off("click").on("click", async function () {
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
                        $("#calendarModal").modal("hide");
                    }
                });
            },
            editable: true,
            dayMaxEvents: true,
            events: '/events/mypage'
        });
        privateCalendar.render();
    }

});