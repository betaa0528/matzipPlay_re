let check = false;
let numberCheck = false;

function reg() {
    let memberId = document.querySelector('[name="memberId"]').value;
    let memberPass = document.querySelector('[name="memberPass"]').value;
    let sample6_postcode = document.getElementById('sample6_postcode').value;
    let memberAddress = document.querySelector('[name="memberAddress"]').value;
    if (!check) {
        alert("아이디 중복 확인을 해주세요")
        document.querySelector('[name="memberId"]').focus();
    } else if (!numberCheck) {
        alert("휴대전화 인증을 진행해주세요")
        document.querySelector('[name="memberPhoneNumber"]').focus();
    } else if (memberPass.length < 8 || memberId.length > 16) {
        alert("비밀번호는 8자 이상 16자 이하 입니다.");
        document.querySelector('[name="memberPass"]').focus();
    } else if (sample6_postcode == "" || memberAddress == "") {
        alert("주소를 입력해주세요");
        document.querySelector('[name="memberAddress"]').focus();
    } else {
        document.frm.submit();
    }
}

function dup() {
    let memberId = document.querySelector('[name="memberId"]');
    let pattern = /^[a-z0-9]+$/;

    if (memberId.value.length < 4 || memberId.value.length > 10) {
        alert("아이디는 4자 이상 10자 이하 입니다.");
        memberId.focus();
    } else if (!pattern.test(memberId.value)) {
        alert("아이디는 알파벳 소문자와 숫자로만 이루어져야 합니다.");
        memberId.focus();
    } else {
        let formData = new FormData();
        formData.append("memberId", memberId.value);

        fetch("./reg/dup", {
            method: "POST",
            body: formData
        }).then(response => response.text())
            .then(data => {
                console.log(data);
                if (data == 0) {
                    alert("사용 가능한 아이디 입니다.");
                    document.querySelector('[name="memberId"]').readOnly = true;
                    document.getElementById('dupCheck').disabled = true;
                    document.querySelector('[name="memberPass"]').disabled = false;
                    document.querySelector('[name="memberPhoneNumber"]').disabled = false;
                    document.querySelector('[name="memberDetailAddr"]').disabled = false;
                    check = true;
                } else alert("이미 존재하는 아이디 입니다.");
            })
            .catch(error => {
            });
    }
}

function tel() {
    let to = document.querySelector('[name="memberPhoneNumber"]').value;
    let formData = new FormData();
    formData.append("to", to);

    fetch("./reg/send", {
        method: "POST",
        body: formData
    }).then(response => response.text())
        .then(data => {
            if(data==1){
                let resultContainer = document.getElementById("resultContainer");

                let number = document.createElement("input");
                number.setAttribute("name", "number");
                number.setAttribute("type", "text");

                let confirm = document.createElement("button");
                confirm.textContent = "인증확인";
                confirm.setAttribute("id", "confirm");
                confirm.setAttribute("type", "button");
                confirm.style.width = "18%";

                confirm.addEventListener("click", numberConfirm);
                resultContainer.appendChild(number);
                resultContainer.appendChild(confirm);
            }else if(data==0){
                alert("전송에 실패하였습니다");
            }else{
                alert("동일한 번호로 회원가입이 되어있습니다\n아이디 찾기 페이지로 이동합니다.");
                setTimeout(() => {
                    window.location.href = 'forgot';
                }, 1000);
            }

        })
        .catch(error => {
        });
}

function numberConfirm() {
    let memberPhoneNumber = document.querySelector('[name="memberPhoneNumber"]').value;
    let number = document.querySelector('[name="number"]').value;

    let formData = new FormData();
    formData.append("memberPhoneNumber", memberPhoneNumber);
    formData.append("number", number);

    fetch("./reg/confirm", {
        method: "POST",
        body: formData
    }).then(response => response.text())
        .then(data => {
            if (data == 1) {
                numberCheck = true;
                alert("인증이 완료되었습니다.");
                document.querySelector('[name="number"]').disabled = true;
                document.querySelector('[name="memberPhoneNumber"]').readOnly = true;
                document.getElementById('telCheck').disabled = true;
                document.getElementById('confirm').disabled = true;
            } else if(data==2){
                alert("유효기간이 만료된 인증번호입니다.");
            } else{
                alert("올바른 인증번호를 입력해주세요.");
            }

        })
        .catch(error => {
        });
}