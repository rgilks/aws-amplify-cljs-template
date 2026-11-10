describe('credential-free template browser checks', () => {
  beforeEach(() => {
    // Do not contact Cognito, email services or any other external application.
    cy.intercept('https://**', {statusCode: 503, body: 'Offline test'})
    cy.visit('/')
  })

  it('renders the compiled application and sign-in controls', () => {
    cy.title().should('include', 'APP_NAME')
    cy.get('input[name="username"]').should('be.visible')
    cy.get('input[name="password"]').should('have.attr', 'type', 'password')
    cy.contains('[role="tab"]', 'Create Account').should('be.visible')
  })

  it('keeps password visibility and registration tab navigation working', () => {
    cy.get('.amplify-field__show-password').click()
    cy.get('input[name="password"]').should('have.attr', 'type', 'text')
    cy.get('.amplify-field__show-password').click()
    cy.get('input[name="password"]').should('have.attr', 'type', 'password')
    cy.contains('[role="tab"]', 'Create Account').click()
    cy.get('input[name="confirm_password"]').should('be.visible')
    cy.get('input[name="email"]').should('be.visible')
    cy.contains('[role="tab"]', 'Sign In').click()
    cy.get('input[name="password"]').should('be.visible')
  })

  it('uses the supported asynchronous environment API', () => {
    cy.env(['offlineFixture']).then(({offlineFixture}) => {
      expect(offlineFixture).to.equal('local-only')
    })
  })

  it('refuses file traversal and write requests in the offline fixture server', () => {
    cy.request({url: '/..%2fpackage.json', failOnStatusCode: false})
      .its('status')
      .should('eq', 403)
    cy.request({url: '/', method: 'POST', failOnStatusCode: false})
      .its('status')
      .should('eq', 405)
  })
})
