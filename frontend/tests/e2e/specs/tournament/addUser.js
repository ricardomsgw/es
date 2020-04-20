describe('Create Tournament', () => {
  beforeEach(() => {
    cy.demoAdminStudent();
  });
  afterEach(() => {
    cy.get('[data-cy="logout"]').click()
  });

  it('student joins a tournament', () => {
    cy.createTournament(5);
    cy.addUser(5);
    cy.log('try to join again');
    cy.addUser(5);

    cy.closeErrorMessage();
  });
});
