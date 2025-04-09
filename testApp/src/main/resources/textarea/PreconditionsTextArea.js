const ID = "preconditionsTextArea"

document.addEventListener("DOMContentLoaded", () => {
    let div = document.getElementById(ID)
    div.contentEditable = "true"

    document.getElementById("checkSyntaxBtn").addEventListener("click", () => {
        let originalText = div.innerText;
        let checkResult = performSyntaxCheck(originalText)
        let resultLabel = document.getElementById("result")

        if (checkResult.isSuccess) {
            displaySuccess(resultLabel)
        } else {
            displayFail(resultLabel)
            div.innerHTML = checkResult.exceptions.reduce(
                (accumulator, exception) => highlightSubstring(
                    exception,
                    accumulator,
                    accumulator.length - originalText.length
                ),
                originalText,
            ).replaceAll("\n", "<br/>")
        }
    })
})

function highlightSubstring(exception, str, offset) {
    let startIndex = exception.indexRange.start + offset
    let endIndex = exception.indexRange.end + offset

    let modified = `<span class="error-part" title="${exception.message}">` + str.substring(startIndex, endIndex+1) + `</span>`
    return str.substring(0, startIndex) + modified + str.substring(endIndex+1, str.length)
}

function performSyntaxCheck(str) {
    console.log(`Checking syntax: ${str}`)
    return {
        isSuccess: false,
        exceptions: [
            {
                type: "aaa",
                message: "Some error",
                indexRange: { start: 0, end: 10 },
                additionalData: {},
            },
            {
                type: "aaa",
                message: "Some error 2",
                indexRange: { start: 18, end: 19 },
                additionalData: {},
            },
        ]
    }
}

function displayFail(label) {
    label.textContent = "Fail"
    label.classList.remove("text-red", "text-green")
    label.classList.add("text-red")
}

function displaySuccess(label) {
    label.textContent = "Success"
    label.classList.remove("text-red", "text-green")
    label.classList.add("text-green")
}