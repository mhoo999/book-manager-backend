    if (true) {
    const $gnb = document.querySelector('header .gnb');
    const $lnbs = document.querySelectorAll('header .lnb');
    const $bg_lnb = document.querySelector('.bg_lnb');

    const navFadeIn = function () {
    $bg_lnb.style.display = 'block';

    $lnbs.forEach(function ($lnb) {
    $lnb.style.display = 'block';
});
};

    const navFadeOut = function () {
    $bg_lnb.style.display = 'none';

    $lnbs.forEach(function ($lnb) {
    $lnb.style.display = 'none';
});
};

    $gnb.addEventListener('mouseenter', function () {
    navFadeIn();
});

    $gnb.addEventListener('mouseleave', function () {
    navFadeOut();
});

    $bg_lnb.addEventListener('mouseover', function () {
    navFadeIn();
});

    $bg_lnb.addEventListener('mouseout', function () {
    navFadeOut();
});
}
