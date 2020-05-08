describe('Create Tournament', () => {
  beforeEach(() => {
    cy.demoStudent();
  });
  afterEach(() => {
    cy.get('[data-cy="logoutButton"]').click()
  });

  it('login creates a tournament', () => {
    cy.createTournament(5);
  });

  it('login creates a tournament with number of questions equal zero', () => {
    cy.createTournament(0);
    cy.closeErrorMessage();
    cy.log('close dialog');
    cy.get('[data-cy="cancelButton"]').click();
  });

  it('login creates a tournament without topics', () => {
    cy.createTournamentFailed(10);
    cy.closeErrorMessage();
    cy.log('close dialog');
    cy.get('[data-cy="cancelButton"]').click();
  });

  it('login and check the list of opened tournaments', () => {
    cy.getOpenedTournaments();
  });
});
