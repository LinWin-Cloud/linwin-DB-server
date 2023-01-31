const crypto = require('crypto')

/**
 * md5 加密
 * @param {String} content 明文
 */
function _md5(content) {
    const md5 = crypto.createHash('md5')
    return md5.update(content).digest('hex')
}
function doCrypto(content) {
    const str = `password=${content}&key=${CRYPTO_SECRET_KEY}`
    return _md5(str)
}
module.exports = {_md5,doCrypto};