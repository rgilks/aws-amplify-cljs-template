import Cookies from '../node_modules/js-cookie/dist/js.cookie.mjs'

// amazon-cognito-identity-js v6 imports js-cookie v2 named methods.
export default Cookies
export const get = Cookies.get.bind(Cookies)
export const remove = Cookies.remove.bind(Cookies)
export const set = Cookies.set.bind(Cookies)
export const withAttributes = Cookies.withAttributes.bind(Cookies)
export const withConverter = Cookies.withConverter.bind(Cookies)
