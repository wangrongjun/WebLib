/**
 * 上传图片前显示预览图
 *
 * 参考：https://www.cnblogs.com/skura23/p/6767825.html - Js实现input上传图片并显示缩略图
 *
 * 使用样例：
 * <input type="file" class="form-control" name="shopHead"
 * onchange="previewNewShopHead('#shop_head_preview', 'input[name=shopHead]')">
 * <img style="max-height: 300px;max-width: 300px" src="" id="shop_head_preview" accept="image/gif,image/jpeg,image/jpg,image/png">
 */
function previewNewShopHead(imgSelector, inputSelector) {
    let preview = document.querySelector(imgSelector);
    let file = document.querySelector(inputSelector).files[0];
    let fileReader = new FileReader();
    fileReader.onloadend = function () {
        preview.src = fileReader.result;
    };
    if (file) {
        fileReader.readAsDataURL(file);
    } else {
        preview.src = "";
    }
}
