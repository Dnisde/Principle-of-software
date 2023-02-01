const allRanks = ['ace', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine', 'ten', 'jack', 'queen', 'king'];
const allSuits = ["clubs", "diamonds", "hearts", "spades"];


var input = document.getElementById("card-input");

// Store the card in the dock
var cardDock = [];

// Execute a function when the user presses a key on the keyboard
input.addEventListener("keypress", function (event) {
    // If the user presses the "Enter" key on the keyboard
    if (event.key === "Enter") {
        // Cancel the default action, if needed
        event.preventDefault();
        // Trigger the button element with a click
        document.getElementById("card-btn").click();
    }
});

//Parse input card info and Show the card image
function parseCard() {
    let cardString = document.getElementById('card-input').value;
    let cardInfo = [];
    cardInfo = cardString.split(' ');
    // Check repeated cards
    if (cardDock.includes(cardString)) {
        alert("Already in the dock!")
    }
    // Check if the dock is full
    else if (cardDock.length > 13) {
        alert("Dock is already full!")
    }
    else if (cardInfo.length != 2) {
        alert('Invalid input length!');
    }
    else {
        const rank = cardInfo[0].toLowerCase();
        const suit = cardInfo[1].toLowerCase();
        let url_card = "../all_cards/" + cardInfo[0] + "_" + cardInfo[1] + ".png";
        let cardBack = document.getElementById("card-back");
        if (!allRanks.includes(rank) || !allSuits.includes(suit)) {
            alert('This card input is not valid!');
        }
        else if (cardBack) {
            cardBack.src = url_card;
            cardDock.push(cardString);
            // Update Current Card Number
            document.getElementById("dock-number").innerHTML = "Dock Cards: " + cardDock.length + " of 13";
            if (cardBack.width == 0) {
                alert("Cannot find corresponding card!");
            }
            if (cardDock.length == 13) {
                document.getElementById("card-input").value = "The dock is full!";
                document.getElementById("card-input").disabled = true;
                document.getElementById("card-btn").disabled = true;
                document.getElementById("display-btn").disabled = false;
            }
        }
    }
}

function displayCard() {
    var divRemove = ["card-display", "card-btn", "card-input", "display-btn"]
    for (var i = 0; i < divRemove.length; i++) {
        let card = document.getElementById(divRemove[i]);
        card.remove();
    }
    for (var i = 0; i < cardDock.length; i++) {
        createCardDiv(i, cardDock[i]);
    }

}

function createCardDiv(num, cardInfo) {
    let card_Num = cardInfo.split(" ")[0];
    let card_Suit = cardInfo.split(" ")[1];
    let cardTemp = document.createElement("img");
    cardTemp.setAttribute("id", "p1card" + (num + 1));
    cardTemp.setAttribute("class", "card-" + (num + 1));
    cardTemp.classList.add('dock');
    cardTemp.setAttribute("width", "200");
    cardTemp.setAttribute("height", "290");
    cardTemp.setAttribute("usemap", "#p1cardMap" + (num + 1));
    var url_card = "../all_cards/" + card_Num + "_" + card_Suit + ".png";
    cardTemp.setAttribute("src", url_card);
    document.getElementById('n').appendChild(cardTemp);

    // Add map and area in terms of each image
    let cardMap = document.createElement("map");
    cardMap.setAttribute("name", "p1cardMap" + (num + 1))
    let cardClickArea = document.createElement("area");
    cardClickArea.setAttribute("shape", "rect");
    if ((num + 1) != cardDock.length) {
        cardClickArea.setAttribute("coords", "0,0,40,290");
    }
    else {
        cardClickArea.setAttribute("coords", "0,0,200,290");
    }
    cardClickArea.onclick = function () {
        alert("Inactivate this card");
        cardTemp.style.filter = "brightness(0.8)";
        cardTemp.style.pointerEvents = 'none';
    }
    cardMap.appendChild(cardClickArea);
    document.getElementById('n').appendChild(cardMap);
}
