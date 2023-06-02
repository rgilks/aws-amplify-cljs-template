import as from '../support/as'

describe('Games listing', () => {
  beforeEach(async () => {
    await as('testUser1')
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
