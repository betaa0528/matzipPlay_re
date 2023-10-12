alert("네이버 연동을 위해 로그인 해주세요");
function sync() {
    let memberId = document.querySelector('[name=memberId]').value;
    let memberPass = document.querySelector('[name=memberPass]').value;
    let memberNaverId = document.getElementById('memberNaverId').value;

    let formData = new FormData();
    formData.append("memberId", memberId);
    formData.append("memberPass", memberPass);
    formData.append("memberNaverId", memberNaverId);

    fetch("./sync", {
        method: "POST",
        body: formData
    }).then(response => response.text())
        .then(data => {
            if (data === "0") {
                alert("아이디 없음");
            } else if (data === "1") {
                alert("비밀번호 틀림");
            } else {
                alert("연동이 완료되었습니다");
                window.location.href = "/restaurant";
            }
        })
        .catch(error => {
            // 오류 처리
        });
}