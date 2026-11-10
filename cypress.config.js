const {
  CognitoIdentityProviderClient,
  AdminDeleteUserCommand
} = require('@aws-sdk/client-cognito-identity-provider')
const {defineConfig} = require('cypress')
const gmail_tester = require('gmail-tester')
const promisePoller = require('promise-poller').default

const {
  region,
  userPoolId,
  testUsers = {}
} = JSON.parse(process.env.CYPRESS_CONFIG)

const cognito = new CognitoIdentityProviderClient({region})

const findEmail = async (creds, token, options, subject, from) => {
  const failureMessage = 'Waiting for the requested verification email'

  const messages = await gmail_tester.get_messages(creds, token, options)

  const foundEmail = messages
    .sort((a, b) => new Date(b.date) - new Date(a.date))
    .find(
      email =>
        (from ? email.from.indexOf(from) >= 0 : true) &&
        email.subject.indexOf(subject) >= 0
    )

  if (foundEmail) {
    return foundEmail
  }

  console.error(failureMessage)
  throw Error(failureMessage) // will continue polling
}

async function setupNodeEvents(on, config) {
  on('task', {
    getEmailMessages({options, subject, username, from}) {
      const user = testUsers[username]
      if (!user) throw Error('Unknown test-account alias')
      const {creds, token} = user

      const date = new Date()

      // Check for messages in the last minute.
      // options.after = date.setMinutes(date.getMinutes() - 1)

      // Check for messages in the last 10 seconds.
      options.after = date.setSeconds(date.getSeconds() - 10)

      return promisePoller({
        taskFn: () => findEmail(creds, token, options, subject, from),
        interval: 2000,
        retries: 15,
        masterTimeout: 30000
      })
    }
  })

  on('task', {
    deleteTestUser(username) {
      console.log('Deleting the requested test account')
      const command = new AdminDeleteUserCommand({
        Username: username,
        UserPoolId: userPoolId
      })
      return cognito.send(command)
    }
  })

  config.env = {...config.env}
  delete config.env.CONFIG
  for (const [alias, {email, password}] of Object.entries(testUsers)) {
    config.env[`testUser:${alias}`] = {email, password}
  }
  return config
}

module.exports = defineConfig({
  e2e: {
    defaultCommandTimeout: 20000,
    watchForFileChanges: true,
    experimentalStudio: true,
    baseUrl: 'http://localhost:3000',
    setupNodeEvents
  }
})
