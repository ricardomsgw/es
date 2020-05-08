describe('Cancel Tournament', () => {
  beforeEach(() => {
    cy.demoStudent();
  });
  afterEach(() => {
    cy.get('[data-cy="logoutButton"]').click()
  });

  it('login creates and cancel a tournament', () => {
    cy.createTournament(5);
    cy.cancelTournament();
  });


});
