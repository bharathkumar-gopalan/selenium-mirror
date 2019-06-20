function checkNullOrUndefined(value) {
    return (value == null || value === undefined || value.length == 0);
}

document.getElementById("register").addEventListener('click', function (event) {
    var host = document.getElementById('host').value;
    var browser = document.getElementById('browser').value;
    var seleniumUrl = document.getElementById('seleniumUrl').value;

    if (checkNullOrUndefined(host) || checkNullOrUndefined(browser) || checkNullOrUndefined(seleniumUrl)) {
        alert('The host name/browser/seleniumUrl cannot be empty!');
        return;
    }
    console.log('The values are ' + host + '' + browser);
    storeDetails(host, browser, seleniumUrl);
});

function storeDetails(host, browser, seleniumUrl) {
    chrome.storage.sync.set({ 'mirrorHost': host, 'mirrorBrowser': browser, 'seleniumUrl': seleniumUrl }, function () {
        chrome.browserAction.setPopup({
            popup: ""
        });
        alert('Details Saved successfully!');
    });
}