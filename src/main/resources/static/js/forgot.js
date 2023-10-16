window.addEventListener("DOMContentLoaded", function () {
    let closeModalBtn = document.getElementById("closeModalBtn");
    let modal = document.getElementById("myModal");
    let resultContainer = document.getElementById("resultCon");

    // 모달 닫기 버튼 클릭 시 모달 닫기
    closeModalBtn.addEventListener("click", function () {
        modal.style.display = "none";
        if (resultContainer !== null) resultContainer.innerHTML = "";
    });

    // 모달 외부 클릭 시 모달 닫기
    window.addEventListener("click", function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
            resultContainer.innerHTML = "";
        }
    });
});

let memberNaverId;
let memberPhoneNumber;

function forgotPw() {
    let memberId = document.querySelector('[name="memberId"]').value;

    let formData = new FormData();
    formData.append("memberId", memberId);

    fetch("./forgot/member-pw", {
        method: "POST",
        body: formData
    }).then(response => response.json())
        .then(data => {
            if (data.result == "false") {
                alert("등록되지 않은 아이디입니다.");
                return;
            } else {
                document.getElementById("myModal").style.display = "block";

                memberNaverId = data.naverId;
                memberPhoneNumber = data.phoneNumber;
                let naverIdParagraph;
                let resultContainer = document.getElementById('resultCon');

                // 네이버 이메일 정보에 따라 요소를 생성
                if (memberNaverId === null) {
                    naverIdParagraph = document.createElement("p");
                    naverIdParagraph.setAttribute("id", "memberNaverId");
                    naverIdParagraph.textContent = "네이버 이메일: 이메일이 존재하지 않습니다.";
                } else {
                    naverIdParagraph = document.createElement("p");
                    naverIdParagraph.setAttribute("id", "memberNaverId");
                    naverIdParagraph.textContent = "네이버 이메일: " + memberNaverId;
                }

                // 전화번호 요소를 생성
                let phoneNumberParagraph = document.createElement("p");
                phoneNumberParagraph.setAttribute("id", "memberPhoneNumber");
                phoneNumberParagraph.textContent = "전화번호: " + memberPhoneNumber;

                // 이메일로 임시 비밀번호 발급 버튼 생성
                let naverIdButton = document.createElement("button");
                naverIdButton.textContent = "이메일로 임시 비밀번호 발급";
                naverIdButton.setAttribute("onclick", "naverIdButton()");
                naverIdButton.setAttribute("type", "button");

                // 전화번호로 임시 비밀번호 발급 버튼 생성
                let phoneNumberButton = document.createElement("button");
                phoneNumberButton.textContent = "전화번호로 임시 비밀번호 발급";
                phoneNumberButton.setAttribute("onclick", "phoneNumberButton()");
                phoneNumberButton.setAttribute("type", "button");

                // 생성한 요소를 resultContainer에 추가
                resultContainer.appendChild(naverIdParagraph);
                resultContainer.appendChild(phoneNumberParagraph);
                resultContainer.appendChild(naverIdButton);
                resultContainer.appendChild(phoneNumberButton);
            }
        })
        .catch(error => {
            // 오류 처리
        });
}

function phoneNumberButton() {
    let memberId = document.querySelector('[name="memberId"]').value;

    let formData = new FormData();
    formData.append("memberId", memberId);
    formData.append("to", memberPhoneNumber);

    if (memberPhoneNumber != null) {
        fetch("./forgot/reset-pass-phone", {
            method: "POST",
            body: formData
        }).then(response => response.text())
            .then(data => {
                if (data == 1) alert("임시 비밀번호가 전화번호로 전송되었습니다.");
                else alert("임시 비밀번호가 전송되지 않았습니다.");
            })
            .catch(error => {
                // 오류 처리
            });
        return;
    } else {
        alert("전화번호가 존재하지 않습니다.");
        return;
    }
}

function naverIdButton() {
    let memberId = document.querySelector('[name="memberId"]').value;

    if (memberNaverId != null) {
        let formData = new FormData();
        formData.append("memberId", memberId);
        formData.append("memberNaverId", memberNaverId);

        fetch("./forgot/reset-pass-email", {
            method: "POST",
            body: formData
        }).then(response => response.text())
            .then(data => {
                if (data == 1) alert("임시 비밀번호가 이메일로 전송되었습니다.");
                else alert("임시 비밀번호가 전송되지 않았습니다.");
            })
            .catch(error => {
                // 오류 처리
            });
        return;
    } else {
        alert("연동된 이메일이 존재하지 않습니다.");
        return;
    }

}

function forgotId() {
    let memberPhoneNumber = document.querySelector('[name="memberPhoneNumber"]').value;

    let formData = new FormData();
    formData.append("memberPhoneNumber", memberPhoneNumber);
    formData.append("to", memberPhoneNumber);

    fetch("./forgot/member-id", {
        method: "POST",
        body: formData
    }).then(response => response.text())
        .then(data => {
            if (data == 0) alert("등록되지 않은 전화번호입니다.");
            else alert("등록된 전화번호로 아이디가 전송되었습니다.");
        })
        .catch(error => {
            // 오류 처리
        });
}

function forid() {
    document.getElementById('forgotpw').style.display = "none";
    document.getElementById('forgotid').style.display = "block";
    document.querySelector('.fp').style.borderBottom = "0";
    document.querySelector('.fi').style.borderBottom = "3px solid #5fc37c";
    clean();
}

function forpw() {
    document.getElementById('forgotid').style.display = "none";
    document.getElementById('forgotpw').style.display = "block";
    document.querySelector('.fi').style.borderBottom = "0";
    document.querySelector('.fp').style.borderBottom = "3px solid #5fc37c";
    clean();
}

function clean() {
    let resultContainer = document.getElementById("resultContainer");
    document.querySelector('[name="memberPhoneNumber"]').value = "";
    document.querySelector('[name="memberId"]').value = "";
    if (resultContainer != null) document.getElementById('resultContainer').innerHTML = "";
}