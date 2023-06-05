const cypressConfig = Cypress.env('CONFIG')

const {testUsers} = cypressConfig

const username = 'testUser1'
const {email, password} = testUsers[username]

describe('Login Screen', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('Has Tile', () => {
    cy.title().should('include', 'APP NAME')
  })

  it('Has tabs to Sign In or Create Account', () => {
    cy.title().should('include', 'APP NAME')

    cy.get('.amplify-tabs').contains('Sign In')
    cy.get('.amplify-tabs').contains('Create Account')
  })

  it('Has forgotten password button', () => {
    cy.get('button.amplify-button--link').should(
      'have.text',
      'Forgot your password?'
    )
  })

  it('Has button to make password visible', () => {
    cy.get('button.amplify-field__show-password').should('be.visible')
  })
})

describe('Sign In', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('A registered user can sign in', () => {
    cy.get('input[name="username"]').type(username)
    cy.get('input[name="password"]').type(password)
    cy.get('button[type="submit"]').click()
    cy.findByTestId('logged-in').contains('YOU ARE LOGGED IN!')
  })
})

describe('Create Account', () => {
  beforeEach(() => {
    cy.visit('/')
    cy.get('.amplify-tabs-item').contains('Create Account').click()
  })

  it('A existing user cannot sign up again with the same email address', () => {
    cy.get('input[name="username"]').type(username)
    cy.get('input[name="password"]').type(password)
    cy.get('input[name="confirm_password"]').type(password)
    cy.get('input[name="email"]').type(email)
    cy.get('button[type="submit"]').click()
    cy.get('.amplify-alert__body').contains('User already exists')
  })

  it.skip('A new user can sign up', () => {
    const timestampedUserName = `testUser1+${Date.now()}`

    cy.get('input[name="username"]').type(timestampedUserName)
    cy.get('input[name="password"]').type(password)
    cy.get('input[name="confirm_password"]').type(password)
    cy.get('input[name="email"]').type(email)
    cy.get('button[type="submit"]').click()

    cy.get('.amplify-heading').contains('We Emailed You')

    cy.task('getEmailMessages', {
      user: testUsers[username],
      from: 'no-reply@verificationemail.com',
      subject: 'Your verification code',
      options: {include_body: true}
    }).then(result => {
      const code = /(.{6})\s*$/.exec(result.body.html)[0]
      cy.get('input[name=confirmation_code]').type(code)
      cy.get('[type=submit]').click()
    })

    cy.findByTestId('logged-in').contains('YOU ARE LOGGED IN!')

    cy.task('deleteTestUser', timestampedUserName)

    cy.log('Test user deleted')
  })
})
