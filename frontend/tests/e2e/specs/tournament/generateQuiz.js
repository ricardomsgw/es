describe('Add User', () => {

  beforeEach(() => {
    cy.demoStudent();
  });
  afterEach(() => {
    cy.get('[data-cy="logoutButton"]').click()
  });

  it('student joins a tournament', () => {
    cy.exec('PGPASSWORD=123456 psql -d tutordb -U ricardo -h localhost -c "INSERT INTO TOURNAMENTS ' +
        '(id,conclusion_date,creator_tournament,number_of_questions,start_date,status,course_execution_id) VALUES (4,\'2020-05-10 02:00:00\',678,4,\'2020-05-08 02:00:00\',\'OPENED\',' +
        '11);\n' +
        'INSERT INTO TOURNAMENTS_USERS VALUES (4,678);\n' +
        'INSERT INTO TOPICS_TOURNAMENTS VALUES (82,4);\n' +
        'INSERT INTO USERS_TOURNAMENTS VALUES (678,4);" ');
  });
});
