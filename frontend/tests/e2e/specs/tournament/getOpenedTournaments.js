describe('Create Tournament', () => {
  beforeEach(() => {
    cy.demoAdminStudent();
  });
  afterEach(() => {
    cy.get('[data-cy="logout"]').click()
  });

  it('getOpenedTournaments', () => {
    cy.getOpenedTournaments();
  });
});
