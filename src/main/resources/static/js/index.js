const category = (cate) => {
    location.href = "/restaurant/category/" + cate.querySelector('p').textContent;
}