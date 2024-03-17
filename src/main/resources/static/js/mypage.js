window.addEventListener("DOMContentLoaded", function () {
    let reviewButton = document.getElementById("review");
    let reviewContainer = document.getElementById("reviewContainer");
    let wishButton = document.getElementById("wish");
    let wishContainer = document.getElementById("wishContainer");

    // 리뷰 클릭
    reviewButton.addEventListener("click", function () {
        reviewContainer.style.display = "block";
        reviewButton.style.color = "black";
        wishContainer.style.display = "none";
        wishButton.style.color = "lightgray";
    });

    // 찜 클릭
    wishButton.addEventListener("click", function () {
        wishContainer.style.display = "block";
        wishButton.style.color = "black";
        reviewContainer.style.display = "none";
        reviewButton.style.color = "lightgray";
    });

    let openModalButton = document.getElementById("openModalBtn");
    let modal = document.getElementById("myModal");

    // 버튼 클릭 시 모달 열기
    openModalButton.addEventListener("click", function () {
        modal.style.display = "block";
    });

    // 모달 외부 클릭 시 모달 닫기
    window.addEventListener("click", function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    });

    let wishDelete = document.querySelectorAll('.wishDelete');
    let reviewDelete = document.querySelectorAll('.reviewDelete');

    wishDelete.forEach(function (button) {
        button.addEventListener("click", function () {
            let wrap = this.closest("#wishWrap");
            let id = wrap.querySelector('[name="id"]').value;
            let wishCount = document.getElementById("wishCount").innerText;

            let formData = new FormData();
            formData.append("id", id);
            formData.append("_method", "delete");

            fetch("./mypage/wish/" + id, {
                method: "POST",
                body: formData
            }).then(response => response.text())
                .then(data => {
                    if (data) {
                        wrap.remove();
                        document.getElementById("wishCount").innerText = --wishCount;
                    }
                })
                .catch(error => {
                    // 오류 처리
                });
        })
    });

    reviewDelete.forEach(function (button) {
        button.addEventListener("click", function () {
            let wrap = this.closest("#reviewWrap");
            let id = wrap.querySelector('[name="id"]').value;
            let reviewCount = document.getElementById("reviewCount").textContent;

            let formData = new FormData();
            formData.append("id", id);
            formData.append("_method", "delete");

            fetch("./mypage/review/" + id, {
                method: "POST",
                body: formData
            }).then(response => response.text())
                .then(data => {
                    if (data) {
                        wrap.remove();
                        document.getElementById("reviewCount").textContent = --reviewCount;
                    }
                })
                .catch(error => {
                    console.log(error)
                });
        });
    });
    document.getElementById('file').addEventListener('change', function (event) {
        let file = event.target.files[0];
        let reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById("profileImg").src = e.target.result;
        };
        reader.readAsDataURL(file);
    });

    document.getElementById("profileChange").addEventListener("click", function () {
        let memberId = document.getElementsByName("memberId")[0].value;
        let file = document.getElementById('file').files[0];

        let formData = new FormData();
        formData.append("file", file);

        fetch("./mypage/profile", {
            method: "POST",
            body: formData
        }).then(response => {
            if (response.status === 400) throw new Error("400 Bad Request");
            return response.json();
        })
            .then(data => {
                if (data === true) {
                    alert("프로필 사진이 변경되었습니다");
                    window.location.href = "mypage";
                }
            })
            .catch(error => {
                alert("사진을 등록해주세요");
            });
    });

});

const movePage = (page) => {
    $.ajax({
        type: "get",
        url: "/mypage/review/list",
        data: {page: page - 1},
        success: function (res) {
            console.log("요청성공", res);
            $('#reviewContainer').replaceWith(res);
            var targetElement = document.getElementsByClassName("page-div");
            if (targetElement) {
                targetElement.scrollIntoView({behavior: 'smooth', block: 'start'});
            }
        },
        error: function (err) {
            console.log("요청실패", err);
        }
    });
}
