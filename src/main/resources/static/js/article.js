const videos = document.querySelectorAll('.video-background video');
let currentVideo = 0;

setInterval(() => {
    videos[currentVideo].style.display = 'none';
    currentVideo = (currentVideo + 1) % videos.length;
    videos[currentVideo].style.display = 'block';
}, 10000); // 切换间隔时间（以毫秒为单位）

$(document).ready(function() {
    // 你可以在这里添加其他 jQuery 代码
});
