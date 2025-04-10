const { fromEvent, of, tap, delay, from } = rxjs;

const ID = "preconditionsTextArea"

function initLoadingDiv() {
    let div = document.createElement('div');
    div.innerHTML = `
        <div id="loading" class="loading-container">
            <img src="loading.gif" height="40">
        </div>
    `.trim();
    return div.firstChild
}

const loadingDiv = initLoadingDiv()

fromEvent(document, "DOMContentLoaded").subscribe(() => {
    let editorDiv = document.getElementById(ID)
    unlock(editorDiv)

    fromEvent(document.getElementById("checkSyntaxBtn"), "click").subscribe(() => {
        let originalText = editorDiv.innerText;

        lock(editorDiv)
        performSyntaxCheck(originalText)
            .pipe(
                tap(result => displayResultStatus(result.isSuccess)),
                tap(_ => unlock(editorDiv)),
            ).subscribe((result => {
                editorDiv.innerHTML = result.exceptions.reduce(
                    (accumulator, exception) => highlightSubstring(
                        exception,
                        accumulator,
                        accumulator.length - originalText.length
                    ),
                    originalText,
                ).replaceAll("\n", "<br/>")
            }))
    })
})

function displayResultStatus(isSuccess) {
    let resultLabel = document.getElementById("result")
    if (isSuccess) {
        displaySuccess(resultLabel)
    } else {
        displayFail(resultLabel)
    }
}

function highlightSubstring(exception, str, offset) {
    let startIndex = exception.indexRange.start + offset
    let endIndex = exception.indexRange.end + offset

    let modified = `<span class="error-part" title="${exception.message}">` + str.substring(startIndex, endIndex+1) + `</span>`
    return str.substring(0, startIndex) + modified + str.substring(endIndex+1, str.length)
}

function performSyntaxCheck(str) {
    console.log(`Checking syntax: ${str}`)

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: JSON.stringify({"preconditionText": str}),
        redirect: "follow"
    };

    return from(
        fetch("http://localhost:8080/check-syntax", requestOptions)
            .then((response) => response.text())
            .then((result) => JSON.parse(result))
    )

    // return of(
    //     {
    //         isSuccess: false,
    //         exceptions: [
    //             {
    //                 type: "aaa",
    //                 message: "Some error",
    //                 indexRange: { start: 0, end: 10 },
    //                 additionalData: {},
    //             },
    //             {
    //                 type: "aaa",
    //                 message: "Some error 2",
    //                 indexRange: { start: 18, end: 19 },
    //                 additionalData: {},
    //             },
    //         ]
    //     }
    // ).pipe(delay(2000))
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

function lock(editor) {
    editor.contentEditable = "false"
    editor.insertBefore(loadingDiv, editor.firstChild)
}

function unlock(editor) {
    editor.contentEditable = "true"
    let loading = document.getElementById("loading")
    if (loading) {
        loading.remove()
    }
}