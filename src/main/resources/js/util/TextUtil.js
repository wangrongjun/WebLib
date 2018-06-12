/**
 *  by wangrongjun on 2017/7/6.
 */

/**
 * @param updateMap 需要更新的参数列表。格式样例：var map={key1:"value1", key2:"value2"};
 * @return string 参数更新后的url
 */
function updateParamInUrl(oldUrl, updateMap) {
    for (var k in updateMap) {
        var value = updateMap[k];
        if (value === null || value === "") { //如果值为空，则没必要添加进url的参数列
            continue;
        } else if (oldUrl.indexOf(k) !== -1) { //如果key存在
            oldUrl = oldUrl.replace(new RegExp(k + "=[^&]+"), k + "=" + value);
        } else if (oldUrl.indexOf("?") !== -1) { //如果?存在，即有别的参数，添加到参数后面
            oldUrl = oldUrl + "&" + k + "=" + value;
        } else { //如果没有参数，直接加在后面
            oldUrl += "?" + k + "=" + value;
        }
    }
    return oldUrl;
}

// testSetParamInUrl();

function testSetParamInUrl() {
    var s = "http://localhost/app?name=123&pass=abc&sex=1";
    var actual = updateParamInUrl(s, {
        name: "234",
        pass: "bcd"
    });
    var expected = "http://localhost/app?name=234&pass=bcd&sex=1";
    if (actual !== expected) {
        alert("Bad,actual : " + actual);
    } else {
        alert("Good");
    }
    /*
     s = "http://localhost:8080/searchAuctions.action?currentPage=0";
     actual = updateParamInUrl(s, {
     currentPage: 3
     });
     expected = "http://localhost:8080/searchAuctions.action?currentPage=3";
     if(actual != expected) {
     //		alert("Bad,actual : " + actual);
     } else {
     alert("Good");
     }*/
}

/**
 * @param setMap 需要设置的参数列表。格式样例：var map={key1:"value1", key2:"value2"};
 * @return string 参数设置后的url。注意：原先在url的参数如果不在map中，则会删除。
 */
function setParamInUrl(oldUrl, setMap) {
    var index = oldUrl.indexOf("?");
    if (index !== -1) {
        return updateParamInUrl(oldUrl.substring(0, index), setMap);
    } else {
        return updateParamInUrl(oldUrl, setMap);
    }
}

function getTextAfterLastPoint(s) {
    var index = s.lastIndexOf(".");
    if (index === -1) {
        return s;
    }
    return s.substring(index + 1);
}

// 定义endsWith方法
// http://www.cnblogs.com/qinxingnet/p/6022024.html - 在Javascript中使用String.startsWith和endsWith
if (typeof String.prototype.endsWith != 'function') {
    String.prototype.endsWith = function (suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
}
