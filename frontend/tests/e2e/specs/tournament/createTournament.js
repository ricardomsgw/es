describe('Create Tournament', () => {
  beforeEach(() => {
    cy.demoAdminStudent();
  });
  afterEach(() => {
    cy.get('[data-cy="logout"]').click()
  });

  it('login creates a tournament', () => {
    cy.createTournament(5);
  });

  it('login creates a tournament with number of questions equal zero', () => {
    cy.createTournament(0);
  });

  it('login creates a tournament without topics', () => {
    cy.createTournamentFailed(10);
  });
});
