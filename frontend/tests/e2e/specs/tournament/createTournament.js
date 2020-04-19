describe('Create Tournament', () => {
  beforeEach(() => {
    cy.demoAdminStudent();
  });
  afterEach(() => {
    cy.get('[data-cy="logout"]').click()
  });

  it('login creates a tournament', () => {
  cy.createTournament('2021-05-08 15:59', '2021-05-10 15:59',5)

});

/*it('login creates and deletes a course execution', () => {
  cy.createCourseExecution('Demo Course','TEST-AO3','Spring Semester')

  cy.deleteCourseExecution('TEST-AO3')
});

it('login creates two course executions and deletes it', () => {
  cy.createCourseExecution('Demo Course','TEST-AO3','Spring Semester')

  cy.log('try to create with the same name')
  cy.createCourseExecution('Demo Course','TEST-AO3','Spring Semester')

  cy.closeErrorMessage()

  cy.log('close dialog')
  cy.get('[data-cy="cancelButton"]').click()

  cy.deleteCourseExecution('TEST-AO3')
});

it('login creates from a course execution and deletes it', () => {
  cy.createFromCourseExecution('Demo Course','TEST-AO3','Spring Semester')

  cy.deleteCourseExecution('TEST-AO3')
});*/

});
