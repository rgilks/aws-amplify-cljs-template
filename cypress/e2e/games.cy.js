const username = 'testUser1'
let password

before(() => {
  cy.env(['CONFIG']).then(({CONFIG}) => {
    password = CONFIG.testUsers[username].password
  })
})

describe('Games listing', () => {
  beforeEach(() => {
    cy.visit('/')
    cy.get('input[name="username"]').type(username)
    cy.get('input[name="password"]').type(password, {log: false})
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
