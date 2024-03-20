// search-result 요소와 section 요소를 가져옵니다.
const searchResult = document.querySelector('.search-result');
const pageDiv = document.querySelector('.page-div');
const section = document.querySelector('section');

// search-result의 높이를 가져와서 section의 높이로 설정합니다.
function adjustSectionHeight() {
    const searchResultHeight = searchResult.clientHeight;
    const pageDivHeight = pageDiv.clientHeight;
    const resultHeight = searchResultHeight + pageDivHeight;
    section.style.height = `${resultHeight + 200}px`;
}

// 페이지 로드 시와 search-result가 변경될 때 높이를 조절합니다.
window.addEventListener('load', adjustSectionHeight);
window.addEventListener('resize', adjustSectionHeight);

