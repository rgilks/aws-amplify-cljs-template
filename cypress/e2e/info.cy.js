describe('Info Page', () => {
  beforeEach(() => {
    cy.visit('/info.html')
  })

  it('Has a title', () => {
    cy.title().should('include', 'APP INFO')
    cy.get('h1').contains('APP INFO')
    // Add test for content when there is some..
  })

  it('Has a button leading to /games', () => {
    cy.get('.button').contains('ENTER').click()
    cy.url().should('include', '/games')
  })
})
