const cleanupMessage = 'cleanup'

function registerEventListeners() {
    chrome.runtime.onMessage.addListener(
        function (request, sender, sendResponse) {
            if (request['message'] === cleanupMessage) {
                console.log('Got an unregister event , clearing up ....')
                // reload the window to remove the content script         
                window.location.reload()
            }
        })
}

registerEventListeners()

function buildAndSendMessage(actionContext) {
    let locationStrategyList = buildElementLocatorPayload(event)
    let payload = {}
    payload.actionContext = actionContext
    payload.locationStrategyList = locationStrategyList
    payload.pageUrl = window.location.href
    console.log('The payload is ' + JSON.stringify(payload))
    chrome.runtime.sendMessage({
        type: "notification", options: {
            message: payload
        }
    });
}

window.addEventListener('click', function (event) {
    let actionContext = { action: 'CLICK' }
    buildAndSendMessage(actionContext)
})

window.addEventListener('keypress', function (event) {
    let actionContext = { action: 'TYPE', value: event.key }
    buildAndSendMessage(actionContext)
})

window.addEventListener('mouse', function (event) {
    // TODO this needs to be updated
    broadcastEvent(JSON.stringify(buildElementLocatorPayload(event)))
})

function buildElementLocatorPayload(event) {
    return [
        { method: 'id', locator: getElementAttribute(event.target, 'ID') },
        { method: 'name', locator: getElementAttribute(event.target, 'NAME') },
        { method: 'xpath', locator: getElementAttribute(event.target, 'XPATH') }
    ]
}

function getElementXpath(target) {
    var elementPath
    if (target === document.body)
        return '//' + target.tagName.toLowerCase() + ''
    var ix = 0
    var siblings = target.parentNode.childNodes
    for (var i = 0; i < siblings.length; i++) {
        var sibling = siblings[i]
        if (sibling === target) {
            elementPath = getElementXpath(target.parentNode) + '/' + target.tagName + '[' + (ix + 1) + ']'
            return elementPath.toLowerCase()
        }
        if (sibling.nodeType === 1 && sibling.tagName === target.tagName)
            ix++
    }
}

function getElementAttribute(target, attributeName) {
    var attr
    switch (attributeName) {
        case 'XPATH':
            attr = getElementXpath(target)
            break

        case 'ID':
            attr = target.id
            break

        case 'NAME':
            attr = target.name
            break
    }
    if (attr == null || attr === undefined) {
        attr = ''
    }
    return attr
}