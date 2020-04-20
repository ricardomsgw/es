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
    cy.exec('PGPASSWORD=123456 psql -d tutordb -U ricardo -h localhost -c "DELETE FROM USERS_TOURNAMENTS WHERE USER_ID=676;\n' +
        'DELETE FROM TOURNAMENTS_USERS WHERE USERS_ID=676;\n' +
        'DELETE FROM TOURNAMENTS WHERE STATUS=\'OPENED\';" ');
  });
  it('student joins a tournament twice', () => {
    cy.createTournament(5);
    cy.addUser(5);
    cy.log('try to join again');
    cy.addUser(5);

    cy.closeErrorMessage();
    cy.exec('PGPASSWORD=123456 psql -d tutordb -U ricardo -h localhost -c "DELETE FROM USERS_TOURNAMENTS WHERE USER_ID=676;\n' +
        'DELETE FROM TOURNAMENTS_USERS WHERE USERS_ID=676;\n' +
        'DELETE FROM TOURNAMENTS WHERE STATUS=\'OPENED\';" ');
  });
});
