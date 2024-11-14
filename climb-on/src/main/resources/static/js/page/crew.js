const onSubmitHandler2 = event => {
    event.preventDefault();
    let result  = event.target;
    // let contents = result.
    console.log(result);
    const child =result.firstElementChild;
    console.log(child);
    const mbth = child.firstElementChild.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling;
    console.log(mbth);
    const last = mbth.firstElementChild.nextElementSibling.textContent;
    console.log(last);



    let content = document.getElementById("editor");


    /*let textWithoutHtml = content.innerHTML
        .replace(/<\/?[^>]+(>|$)/g, "") // Remove HTML tags
        .replace(/&nbsp;/g, " ") // Replace &nbsp; with a space

// Remove extra spaces and newlines
    let cleanText = textWithoutHtml.replace(/\s{2,}/g, " ")          // Collapse multiple spaces into a single space
        .replace(/(\s*\n\s*)+/g, "\n")
        .replace()// Remove spaces around newlines
        .trim();*/

    let textWithoutHtml = content.innerHTML
        .replace(/<[^>]*>/g, "")    // Remove all HTML tags
        .replace(/&nbsp;/g, " ");    // Convert &nbsp; to a space

// Step 2: Clean up extra spaces and line breaks
    let cleanText = textWithoutHtml
        .replace(/\s+/g, " ")       // Collapse multiple spaces into a single space
        .replace(/(\n\s*){2,}/g, "\n");

    console.log(content.innerHTML);
    console.log(content.innerText);
    console.log(content.textContent);

    console.log(textWithoutHtml);
    console.log(cleanText);

    let nText = content.textContent.replace(/<[^>]*>/g, "").trim();
    const anText = nText.trim();
    console.log(anText);



    // let form = document.getElementById("writePostForm");

    // form.submit;
}
