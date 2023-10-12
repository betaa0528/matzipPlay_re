function login() {
    let memberId = document.querySelector('[name="memberId"]').value;
    let memberPass = document.querySelector('[name="memberPass"]').value;

    let formData = new FormData();
    formData.append("memberId", memberId);
    formData.append("memberPass", memberPass);

    fetch("./login", {
        method: "POST",
        body: formData
    }).then(response => response.text())
        .then(data => {
            if (data === "0") {
                alert("아이디 없음");
            } else if (data === "1") {
                alert("비밀번호 틀림");
            } else {
                alert("환영합니다");
                window.location.href = "/restaurant";
            }
        })
        .catch(error => {
            // 오류 처리
        });
}