const allRanks = ['ace', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine', 'ten', 'jack', 'queen', 'king'];
const allSuits = ["clubs", "diamonds", "hearts", "spades"];
const redis_password="A46779EE024D4";


let input = document.getElementById("card-input");

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

// Parse input card info and Show the card image
async function parseCard() {
    let cardString = document.getElementById('card-input').value;
    let cardInfo = [];
    cardInfo = cardString.split(' ');
    try{
        if (cardInfo.length !== 2) {
            throw new Error('Invalid input length!');
        }
        const rank = cardInfo[0].toLowerCase();
        const suit = cardInfo[1].toLowerCase();
        if (!allRanks.includes(rank) || !allSuits.includes(suit)) {
            throw new Error('This card input is not valid!');
        }

        /*
         * Generate key of cards on Redis
         * Example: 2 hearts is stored as two_hearts: two&hearts
         */
        let cardInfo_server = [];
        let request_key = cardInfo[0] + "_" + cardInfo[1];

        // Parse the value associate with key
        let server_value = await redis_get(request_key);
        if (server_value === false) {
            throw new Error('No data stored on the server!');
        } else {
            cardInfo_server=server_value.split('&');
            console.log(cardInfo_server);
        }

        // Generate local storage url of cards
        let url_card = "../all_cards/"+ cardInfo_server[0] + "_" + cardInfo_server[1] + ".png";
        let cardBack = document.getElementById("card-back");
        if (cardBack) {
            cardBack.src = url_card;
            cardBack.className = "card-1";
            cardBack.classList.add('dock');
            if (cardBack.width === 0) {
                throw new Error('Cannot find corresponding card！');
            }
            const rank = cardInfo[0].toLowerCase();
            const suit = cardInfo[1].toLowerCase();
            if (!allRanks.includes(rank) || !allSuits.includes(suit)) {
                throw new Error('This card input is not valid!');
            }
            let url_card = "../all_cards/" + cardInfo[0] + "_" + cardInfo[1] + ".png";
            let cardBack = document.getElementById("card-back");
            if (cardBack) {
                cardBack.src = url_card;
                // cardBack.className = "card-1";
                // cardBack.classList.add('dock');
                cardDock.push(cardString);
                if (cardBack.width == 0) {
                    throw new Error('Cannot find corresponding card!');
                }
                if (cardDock.length == 13) {
                    document.getElementById("card-input").value = "The dock is full!";
                    document.getElementById("card-input").disabled = true;
                    document.getElementById("card-btn").disabled = true;
                    document.getElementById("display-btn").disabled = false;
                }
            }
        } catch (e) {
            document.getElementById('card-input').value = e.name + ':' + e.message;
            console.log(e.name, ': ', e.message);
            return null;
        }
    } catch (e) {
        document.getElementById('card-input').value = e.name + ':'+ e.message;
        console.log(e.name, ': ', e.message);
        return null;
    }
}

// Generate Redis url
function parameters_calculate() {
    // Generate random salt
    let source_text = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    let salt = "";
    for (var i = 16; i > 0; --i) {
        salt += source_text[Math.floor(Math.random() * source_text.length)];
    }

    let hashed = hex_md5(salt + redis_password);
    let url_base = "https://agile.bu.edu/ec500_scripts/redis.php?";
    let url = url_base + "salt=" + salt + "&hash=" + hashed + "&message=";
    return url;
}

// Interaction with Redis
async function redis_get(storage_name) {

    let url = parameters_calculate()+"get " + storage_name;
    const command = {
        method: 'GET'
    }

    // Parse the return message from Redis
    const response = await fetch(url, command);
    let response_text = await response.text();
    let result_index = response_text.indexOf("Result:");
    let return_message_raw = response_text.slice(result_index + 9);
    if (return_message_raw === "") {
        console.log("No data");
        return false;
    } else {
        let return_message = return_message_raw;
        console.log("Get data succeed");
        return return_message;
    }
}
