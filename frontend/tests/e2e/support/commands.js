// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
/// <reference types="Cypress" />

Cypress.Commands.add('demoStudent', () => {
  cy.visit('/')
  cy.get('[data-cy="demoStudentLoginButton"]').click()
  cy.get('[data-cy="Tournaments"]').click()

});

Cypress.Commands.add('createTournament', (numberOfQuestions) => {
  cy.contains('Manage tournaments').click();
  cy.wait(1000);
  cy.get('[data-cy="createTournamentButton"]').click();
  cy.get('#startDateInput-wrapper > .field').click();
  cy.get('#startDateInput-picker-container-DatePicker > .calendar > .datepicker-controls > .datepicker-container-label > :nth-child(2) > .custom-button > .custom-button-content').click();
  cy.get(':nth-child(10) > .custom-button-content').click();
  cy.get('#startDateInput-picker-container-DatePicker > .calendar > .month-container > :nth-child(1) > .datepicker-days > :nth-child(14) > .datepicker-day-text').click();
  cy.get('#startDateInput-wrapper > .datetimepicker > .datepicker > .datepicker-buttons-container > .validate').click();
  cy.get('.layout > :nth-child(2)').click();
  cy.wait(1000);
  cy.get('#conclusionDateInput-picker-container-DatePicker > .calendar > .datepicker-controls > .datepicker-container-label > :nth-child(2) > .custom-button > .custom-button-content').click();
  cy.get(':nth-child(10) > .custom-button-content').click();
  cy.get('#conclusionDateInput-picker-container-DatePicker > .calendar > .month-container > :nth-child(1) > .datepicker-days > :nth-child(35) > .datepicker-day-text').click();
  cy.get('#conclusionDateInput-wrapper > .datetimepicker > .datepicker > .datepicker-buttons-container > .validate').click();
  cy.get('[data-cy="NumberOfQuestions"]').type(numberOfQuestions);
  cy.get('[data-cy="Topics"]').click();
  cy.contains('Architectural Style').click();
  cy.contains('Amazon Silk').click();
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('cancelTournament', () => {
  cy.wait(1000);
  cy.get(':nth-child(1) > :nth-child(6) > [data-cy=cancelTournament]').click();

});

Cypress.Commands.add('createTournamentFailed', (numberOfQuestions) => {
  cy.contains('Manage tournaments').click();
  cy.wait(1000);
  cy.get('[data-cy="createTournamentButton"]').click();
  cy.get('#startDateInput-wrapper > .field').click();
  cy.get('#startDateInput-picker-container-DatePicker > .calendar > .datepicker-controls > .datepicker-container-label > :nth-child(2) > .custom-button > .custom-button-content').click();
  cy.get(':nth-child(10) > .custom-button-content').click();
  cy.get('#startDateInput-picker-container-DatePicker > .calendar > .month-container > :nth-child(1) > .datepicker-days > :nth-child(14) > .datepicker-day-text').click();
  cy.get('#startDateInput-wrapper > .datetimepicker > .datepicker > .datepicker-buttons-container > .validate').click();
  cy.get('.layout > :nth-child(2)').click();
  cy.wait(1000);
  cy.get('#conclusionDateInput-picker-container-DatePicker > .calendar > .datepicker-controls > .datepicker-container-label > :nth-child(2) > .custom-button > .custom-button-content').click();
  cy.get(':nth-child(10) > .custom-button-content').click();
  cy.get('#conclusionDateInput-picker-container-DatePicker > .calendar > .month-container > :nth-child(1) > .datepicker-days > :nth-child(35) > .datepicker-day-text').click();
  cy.get('#conclusionDateInput-wrapper > .datetimepicker > .datepicker > .datepicker-buttons-container > .validate').click();
  cy.get('[data-cy="NumberOfQuestions"]').type(numberOfQuestions);
  cy.get('[data-cy="saveButton"]').click();
});
Cypress.Commands.add('addUser', (numberOfQuestions) => {
  cy.get('[data-cy="Tournaments"]').click()
  cy.get('[data-cy="ManageTournaments"]').click();
  cy.contains(numberOfQuestions)
      .parent()
      .should('have.length', 1)
      .children()
      .should('have.length', 6)
      .find('[data-cy="joinTournament"]')
      .click();

});

Cypress.Commands.add('getOpenedTournaments',() => {
  cy.contains('Tournaments').click();
  cy.contains('Manage tournaments').click();
  cy.contains('Tournaments').click();
  cy.wait(2000);
});

Cypress.Commands.add('createCourseExecution', (name, acronym, academicTerm) => {
  cy.get('[data-cy="createButton"]').click();
  cy.get('[data-cy="courseExecutionNameInput"]').type(name);
  cy.get('[data-cy="courseExecutionAcronymInput"]').type(acronym);
  cy.get('[data-cy="courseExecutionAcademicTermInput"]').type(academicTerm);
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('closeErrorMessage', (name, acronym, academicTerm) => {
  cy.contains('Error')
    .parent()
    .find('button')
    .click();
});

Cypress.Commands.add('deleteCourseExecution', acronym => {
  cy.contains(acronym)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 7)
    .find('[data-cy="deleteCourse"]')
    .click();
});

Cypress.Commands.add(
  'createFromCourseExecution',
  (name, acronym, academicTerm) => {
    cy.contains(name)
      .parent()
      .should('have.length', 1)
      .children()
      .should('have.length', 7)
      .find('[data-cy="createFromCourse"]')
      .click();
    cy.get('[data-cy="courseExecutionAcronymInput"]').type(acronym);
    cy.get('[data-cy="courseExecutionAcademicTermInput"]').type(academicTerm);
    cy.get('[data-cy="saveButton"]').click();
  }
);

