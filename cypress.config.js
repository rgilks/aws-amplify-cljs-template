const {defineConfig} = require('cypress')
const gmail_tester = require('gmail-tester')
const promisePoller = require('promise-poller').default

const findEmail = async (creds, token, options, subject, from) => {
  const failureMessage = from
    ? `Waiting for email from: ${from}, subject: ${subject}`
    : `Waiting for email with subject: ${subject}`

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
    getEmailMessages({options, subject, user, from}) {
      const {creds, token} = user

      // Check for messages in the last 2 minutes.
      const date = new Date()
      options.after = date.setMinutes(date.getMinutes() - 2)

      return promisePoller({
        taskFn: () => findEmail(creds, token, options, subject, from),
        interval: 2000,
        retries: 30,
        masterTimeout: 60000
      })
    }
  })

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
