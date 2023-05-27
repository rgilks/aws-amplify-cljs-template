const {testUser1} = Cypress.env('TEST_USERS')

describe('Sign In', () => {
  it('A registered user can sign in', () => {
    cy.visit('/')
    cy.get('#amplify-id-\\:r4\\:').clear()
    cy.get('#amplify-id-\\:r4\\:').type('testUser1')
    cy.get('#amplify-id-\\:r7\\:').clear()
    cy.get('#amplify-id-\\:r7\\:').type(testUser1.password)
    cy.get('.amplify-button--primary').click()
  })
})
