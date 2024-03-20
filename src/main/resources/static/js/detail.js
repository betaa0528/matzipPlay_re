
const saveWish = (name, id) => {
    $.ajax({
        type: "post", url: "/reviews/wishlist",
        data: {
            "restaurantId": id,
            "memberWishId": name
        }, success: function (res) {
            // console.log("성공", res);
            const wishIconText = document.querySelector(".wish-icon-text");
            const wishIcon = document.querySelector(".wish-icon");
            wishIconText.textContent = res === 0 ? "찜하기" : "찜!!"; // res 값 => 찜됐는지 안됐는지
            if (res === 1) {
                wishIconText.style.color = '#5fc37c';
                wishIcon.style.filter = 'opacity(0.5) drop-shadow(0 0 0 #7afa9f)';
            } else {
                wishIconText.style.color = `grey`;
                wishIcon.style.filter = 'opacity(0.5) drop-shadow(0 0 0 #707070);';
            }
        }, error: function () {
            // console.log("실패");
        }
    })
}
const wishKeepCss = () => {
    const wishIconText = document.querySelector(".wish-icon-text");
    const wishIcon = document.querySelector(".wish-icon");
    if (wishIconText.textContent === "찜!!") {

        wishIconText.style.color = '#5fc37c';
        wishIcon.style.filter = 'opacity(0.5) drop-shadow(0 0 0 #7afa9f)';
    } else {
        wishIconText.style.color = `grey`;
        wishIcon.style.filter = 'opacity(0.5) drop-shadow(0 0 0 #707070);';
    }
}

window.addEventListener('load', wishKeepCss);

// 페이지 높이 조절 위한 변수
const reviewDiv = document.querySelector(".review-div");
const infoWrap = document.querySelector(".restaurant-info-wrap");
const section = document.querySelector('section');

// 페이지 로드 시와 review-empty-div, review-present-div의 가시성 변경 시 높이를 조절합니다.
function adjustSectionHeight() {
    const reviewDivHeight = reviewDiv.clientHeight;
    const infoWrapHeight = infoWrap.clientHeight;
    const resultHeight = reviewDivHeight + infoWrapHeight;
    section.style.height = `${resultHeight + 200}px`;
}

function adjustReviewTextWidth() {
    const reviewDivWidth = document.querySelector(".review-text-div");
    reviewDivWidth.style.width = `100%`;
}

window.addEventListener('load', adjustReviewTextWidth);
// 페이지 로드 시와 review-empty-div, review-present-div의 가시성 변경 시 높이를 조절합니다.
window.addEventListener('load', adjustSectionHeight);
window.addEventListener('resize', adjustSectionHeight);

const movePage = (id, page) => {

    $.ajax({
        type: "get", url: "/reviews", data: {page: page - 1, "id": id}, success: function (res) {
            // console.log("요청성공", res);
            $('#review-div').replaceWith(res);
            var targetElement = document.getElementsByClassName("page-div");
            if (targetElement) {
                targetElement.scrollIntoView({behavior: 'smooth', block: 'start'});
            }
            // console.log("작동");
        }, error: function (err) {
            console.log("요청실패", err);
        }
    });
}


const modal = document.querySelector(".modal");
const img = document.querySelector(".review-file-div-img");
const modal_img = document.querySelector(".modal-img");
const span = document.querySelector(".modal-close");
const modal_userID = document.querySelector(".modal-review-user-id");
const modal_review_text = document.querySelector(".modal-review-text");
const modal_review_createAt = document.querySelector(".modal-review-createAt");
const modal_user_img = document.querySelector(".modal-member-img")
const beforeBtn = document.querySelector(".modal-before-btn");
const afterBtn = document.querySelector(".modal-after-btn");
const modalStart = (test) => {
    const hidden_memberId = test.querySelector(".hidden-review-memberId");
    const hidden_review_createAt = test.querySelector(".hidden-review-createAt");
    const hidden_reviewText = test.querySelector(".hidden-review-reviewText");
    const storedNameInputs = test.querySelectorAll('.stored-name');
    const storedNames = [];
    let count = 0;

    storedNameInputs.forEach(input => {
        storedNames.push(input.value);
    });

    console.log('Stored Names:', storedNames);
    modalDisplay("block");
    modal_img.src = test.querySelector('.review-file-div-img').src;
    modal_user_img.src = test.querySelector(".hidden-review-text-div-img").src;
    modal_userID.textContent = hidden_memberId.value;
    modal_review_text.textContent = hidden_reviewText.value;
    modal_review_createAt.textContent = hidden_review_createAt.value;

    beforeBtn.addEventListener('click', () => {
        count = indexNumber(count - 1);
        modal_img.src = "/review_img/" + storedNames[count];
        console.log("이전" + count);

    })

    afterBtn.addEventListener('click', () => {
        count = indexNumber(count + 1);
        modal_img.src = "/review_img/" + storedNames[count];
        console.log("다음" + count);
    })
    const indexNumber = (num) => {
        if (num < 0) {
            return storedNames.length - 1;
        } else if (num > storedNames.length - 1) {
            return 0;
        } else {
            return num;
        }
    }
}
span.addEventListener('click', () => {
    modalDisplay("none");
});

function modalDisplay(text) {
    modal.style.display = text;
}


// 작성한 리뷰가 있으면 리뷰쓰기 버튼이 사라짐
// const reviewChk = () => {
//     // console.log("memberId? " + member_id);
//     if (member_id != null) {
//         // console.log(review_list);
//         const reviewIconBox = document.querySelector(".review-icon-box");
//         if (review_list.some(item => item.memberId === member_id)) {
//             reviewIconBox.style.display = "none";
//         }
//     }
// }
// window.addEventListener('load', reviewChk);
