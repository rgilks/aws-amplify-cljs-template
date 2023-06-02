import {Amplify} from '@aws-amplify/core'
import {Auth} from '@aws-amplify/auth'

import config from '../../src/amplify/aws-exports'

const cypressConfig = Cypress.env('CONFIG')

const {testUsers} = cypressConfig

Amplify.configure(config)

const as = async username => {
  const {password} = testUsers[username]
  try {
    const user = await Auth.currentAuthenticatedUser()
    if (user.username !== username) {
      throw Error('Logged in as different user')
    }
  } catch {
    await Auth.signIn(username, password)
  }
}

export default as
