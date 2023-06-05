const cypressConfig = Cypress.env('CONFIG')

const {testUsers} = cypressConfig

const username = 'testUser1'
const {email, password} = testUsers[username]

describe('Games listing', () => {
  beforeEach(() => {
    cy.visit('/')
    cy.get('input[name="username"]').type(username)
    cy.get('input[name="password"]').type(password)
    cy.get('button[type="submit"]').click()
    cy.findByTestId('logged-in').contains('YOU ARE LOGGED IN!')
    cy.visit('/games')
  })

  it('Has a title', () => {
    cy.title().should('include', 'APP NAME')
    cy.findByTestId('games-title').contains('APP TITLE')
  })

  it('Has a list of games', () => {
    cy.findByTestId('games-list').contains('Games')
  })
})
