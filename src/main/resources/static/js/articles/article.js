// 페이지 높이 조절 위한 변수
const articleDiv = document.querySelector(".article-div");
// const infoWrap = document.querySelector(".restaurant-info-wrap");
const section = document.querySelector('section');

// 페이지 로드 시와 review-empty-div, review-present-div의 가시성 변경 시 높이를 조절합니다.
function adjustSectionHeight() {
    const articleDivHeight = articleDiv.clientHeight;
    // const infoWrapHeight = infoWrap.clientHeight;
    // const resultHeight = reviewDivHeight + infoWrapHeight;
    section.style.height = `${articleDivHeight + 200}px`;
}

// function adjustReviewTextWidth() {
//     const articleDivWidth = document.querySelector(".article-div");
//     articleDivWidth.style.width = `100%`;
// }
//
// window.addEventListener('load', adjustReviewTextWidth);
// 페이지 로드 시와 review-empty-div, review-present-div의 가시성 변경 시 높이를 조절합니다.
window.addEventListener('load', adjustSectionHeight);
window.addEventListener('resize', adjustSectionHeight);
