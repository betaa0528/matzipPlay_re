const restaurantWrap = document.querySelector('.restaurant-wrap');
const pageDiv = document.querySelector('.page-div');
const section = document.querySelector('section');

function adjustSectionHeight() {
    const restaurantWrapHeight = restaurantWrap.clientHeight;
    const pageDivHeight = pageDiv.clientHeight;
    const resultHeight = restaurantWrapHeight + pageDivHeight;
    section.style.height = `${resultHeight + 100}px`;
}

// 페이지 로드 시와 search-result가 변경될 때 높이를 조절합니다.
window.addEventListener('load', adjustSectionHeight);
window.addEventListener('resize', adjustSectionHeight);