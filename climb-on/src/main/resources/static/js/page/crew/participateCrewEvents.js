document.getElementById("crewParticipateButton").addEventListener("click", async function() {
    const eventCode = this.getAttribute("data-event-code");
    const crewCode = this.getAttribute("data-crew-code");
    const userCode = "USER789"; // Example; retrieve dynamically from logged-in user info

    const participationData = {
        eventCode: eventCode,
        crewCode: crewCode,
        userCode: userCode
    };

    try {

        const response = await fetch('/participate/crewevents', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(participationData)
        });

        if (response.ok) {
            alert("참여가 완료되었습니다.");
        } else {
            alert("참여에 실패했습니다.");
        }
    } catch (error) {
        console.error('Error:', error);
        alert("서버 오류가 발생했습니다.");
    }
});