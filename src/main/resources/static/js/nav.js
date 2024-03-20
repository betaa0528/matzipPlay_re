const category = (cate) => {
    location.href = "/restaurant/category/" + cate.querySelector('p').textContent;
}

const searchBar = document.querySelector(".search-bar");
const search = () => {
    console.log("클릭됨");
    if (searchBar.value === "") {
        alert("검색어를 입력해주세요");
        searchBar.focus();
        return false;
    } else {
        return true;
        // document.searchForm.submit();
    }
}
