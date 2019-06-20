const hostKey = 'mirrorHost'
const hostBrowser = 'mirrorBrowser'
const seleniumHostKey = 'seleniumUrl'
const cleanupMessage = { 'message': 'cleanup' }

let sessionId
let isRecorderOn = false
let hostName


function checkNullOrUndefined(value) {
    return (value == null || value === undefined || value.length == 0);
}

function doRequest(method, url, payload) {
    return new Promise(function (resolve, reject) {
        let xhr = new XMLHttpRequest()
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                console.log(xhr.response);
                console.log(xhr.responseText);
                resolve(xhr)
            }
        }
        xhr.open(method, url);
        xhr.setRequestHeader('Content-type', 'application/json')
        xhr.setRequestHeader('Accept', 'application/json')
        if (payload === undefined || payload == null) {
            xhr.send(null)
        } else {
            xhr.send(JSON.stringify(payload))
        }
    });
}

async function createSession(hostName, browser, seleniumServerUrl) {
    let registrationRequest = {}
    registrationRequest.browserName = browser
    registrationRequest.seleniumServerUrl = seleniumServerUrl
    let xhr = await doRequest("POST", hostName + '/mirror/register', registrationRequest)
    return JSON.parse(xhr.responseText).sessionId
}

async function deleteSession(hostName, sessionId) {
    console.log('The session id is ' + sessionId)
    let url = hostName + '/mirror/' + sessionId
    console.log('The URL is ' + url)
    let xhr = await doRequest("DELETE", url)
    sessionId = null
}

async function startRecording(hostName, browser, seleniumUrl) {
    isRecorderOn = true
    chrome.browserAction.setBadgeText({ text: 'ON' })
    sessionId = await createSession(hostName, browser, seleniumUrl);
    console.log('The sessionId here is ' + sessionId)
    if (checkNullOrUndefined(sessionId)) {
        chrome.browserAction.setBadgeText({ text: 'ERR' })
    }
    else {
        chrome.tabs.executeScript({
            file: 'recorder.js'
        })
    }
}

chrome.browserAction.onClicked.addListener(function (tab) {
    if (isRecorderOn) {
        chrome.browserAction.setBadgeText({ text: 'OFF' })
        isRecorderOn = false
        deleteSession(hostName, sessionId)
        chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
            chrome.tabs.sendMessage(tabs[0].id, cleanupMessage, function (response) {
            })
        })
        console.log('Sending a cleanup message ')
        return
    }

    chrome.storage.sync.get([hostKey, hostBrowser, seleniumHostKey], function (item) {
        hostName = item[hostKey]
        const browser = item[hostBrowser]
        const seleniumUrl = item[seleniumHostKey]

        if (checkNullOrUndefined(hostName) && checkNullOrUndefined(browser) && checkNullOrUndefined(seleniumUrl)) {
            chrome.browserAction.setPopup({
                popup: "popup.html"
            })
        }
        else {
            //Start the recording
            chrome.browserAction.setPopup({
                popup: ""
            })
            startRecording(hostName, browser, seleniumUrl)
        }
    })
})

chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        doRequest("POST", hostName + '/mirror/' + sessionId, request.options.message)
    })

chrome.webNavigation.onCommitted.addListener(function (params) {
    //Check to see if the recorder is on if so inject the content script 
    if (isRecorderOn && hostKey != undefined) {
        chrome.tabs.executeScript({
            file: 'recorder.js'
        })
    }
}) 