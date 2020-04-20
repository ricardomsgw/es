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
Cypress.Commands.add('demoAdminLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="adminButton"]').click();
  cy.contains('Administration').click();
  cy.contains('Manage Courses').click();
});

Cypress.Commands.add('demoAdminStudent', () => {
  cy.visit('/')
  cy.get('[data-cy="studentButton"]').click()
  cy.get('[data-cy="Tournaments"]').click()

});

Cypress.Commands.add('createTournament', (numberOfQuestions) => {
  cy.get('[data-cy="MyTournaments"]').click();
  cy.get('[data-cy="createTournamentButton"]').click();
  cy.get('.layout > :nth-child(1) > .v-input').click();
  cy.get('.v-date-picker-title__year').click();
  cy.get('.v-date-picker-years > :nth-child(100)').click();
  cy.get('tbody > :nth-child(1) > :nth-child(1) > .v-btn > .v-btn__content').click();
  cy.get(':nth-child(2) > :nth-child(5) > .v-btn > .v-btn__content').click();
  cy.get('.v-time-picker-clock__item--active > span').click();
  cy.get('.green--text > .v-btn__content').click();
  cy.wait(1000);
  cy.get(':nth-child(2) > .v-input').click();
  cy.get('[tabindex="0"][style="z-index: 204;"] > .v-dialog > .v-sheet > .v-card__text > .v-tabs > .v-window > .v-window__container > .v-window-item > .v-picker > .v-picker__title > .v-date-picker-title > .v-date-picker-title__year').click();
  cy.get('.v-date-picker-years > :nth-child(99)').click();
  cy.get('tbody > :nth-child(1) > :nth-child(2) > .v-btn > .v-btn__content').click();
  cy.get('[tabindex="0"][style="z-index: 204;"] > .v-dialog > .v-sheet > .v-card__text > .v-tabs > .v-window > .v-window__container > .v-window-item > .v-picker > .v-picker__body > :nth-child(1) > .v-date-picker-table > table > tbody > :nth-child(2) > :nth-child(4) > .v-btn > .v-btn__content').click();
  cy.get('[tabindex="0"][style="z-index: 204;"] > .v-dialog > .v-sheet > .v-card__actions > .green--text').click();
  cy.get('[data-cy="NumberOfQuestions"]').type(numberOfQuestions);
  cy.get('[data-cy="Topics"]').click();
  cy.contains('Architectural Style').click();
  cy.contains('Amazon Silk').click();
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('createTournamentFailed', (numberOfQuestions) => {
  cy.get('[data-cy="MyTournaments"]').click();
  cy.get('[data-cy="createTournamentButton"]').click();
  cy.get('.layout > :nth-child(1) > .v-input').click();
  cy.get('.v-date-picker-title__year').click();
  cy.get('.v-date-picker-years > :nth-child(100)').click();
  cy.get('tbody > :nth-child(1) > :nth-child(1) > .v-btn > .v-btn__content').click();
  cy.get(':nth-child(2) > :nth-child(5) > .v-btn > .v-btn__content').click();
  cy.get('.v-time-picker-clock__item--active > span').click();
  cy.get('.green--text > .v-btn__content').click();
  cy.wait(1000);
  cy.get(':nth-child(2) > .v-input').click();
  cy.get('[tabindex="0"][style="z-index: 204;"] > .v-dialog > .v-sheet > .v-card__text > .v-tabs > .v-window > .v-window__container > .v-window-item > .v-picker > .v-picker__title > .v-date-picker-title > .v-date-picker-title__year').click();
  cy.get('.v-date-picker-years > :nth-child(99)').click();
  cy.get('tbody > :nth-child(1) > :nth-child(2) > .v-btn > .v-btn__content').click();
  cy.get('[tabindex="0"][style="z-index: 204;"] > .v-dialog > .v-sheet > .v-card__text > .v-tabs > .v-window > .v-window__container > .v-window-item > .v-picker > .v-picker__body > :nth-child(1) > .v-date-picker-table > table > tbody > :nth-child(2) > :nth-child(4) > .v-btn > .v-btn__content').click();
  cy.get('[tabindex="0"][style="z-index: 204;"] > .v-dialog > .v-sheet > .v-card__actions > .green--text').click();
  cy.get('[data-cy="NumberOfQuestions"]').type(numberOfQuestions);
  cy.get('[data-cy="saveButton"]').click();
});
Cypress.Commands.add('addUser', (numberOfQuestions) => {
  cy.get('[data-cy="Tournaments"]').click()
  cy.get('[data-cy="OpenedTournaments"]').click();
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
  cy.contains('Opened tournaments').click();
  cy.contains('Tournaments').click();
});

Cypress.Commands.add('createCourseExecution', (name, acronym, academicTerm) => {
  cy.get('[data-cy="createButton"]').click();
  cy.get('[data-cy="Name"]').type(name);
  cy.get('[data-cy="Acronym"]').type(acronym);
  cy.get('[data-cy="AcademicTerm"]').type(academicTerm);
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
    cy.get('[data-cy="Acronym"]').type(acronym);
    cy.get('[data-cy="AcademicTerm"]').type(academicTerm);
    cy.get('[data-cy="saveButton"]').click();
  }
);

