/**
 * A generalization of {@link MD5} to any of the standard md5_encrypted message.
 * This class represents a MD5(input) base class, that will encrypt the content by the MD5 message-digest algorithm.
 * For example, MD5("your_content") should be equivalent to the MD5 class, and it is the only way to initialize it.
 * 
 * Note!!: Need install npm package, using "npm install md5" 
 * @param parameter The input of the content who needs to be encrypted.
 * @var _encrypt(#) The private unencrptied content that is only accessible by inside of the class.
 */

class MD5{

    // Privates should start with #. They are only accessible from inside the class.
    #_encrypt = "";

    // Initialize the constructor, include setter:
    constructor(parameter)
    {
        var md5 = require('md5');
        this.#_encrypt = md5(parameter.toString());
        return this.#_encrypt;
    }

    get md5()
    {
        return this.#_encrypt;
    }
}

export default MD5;