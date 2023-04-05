# TODO LIST


## BACK-END

- [x] On signup, send a HTTP code, for success or failure
- [x] On signin, send a HTTP code, for success or failure
- [x] Create new forms architecture, in order to treat users forms data
- [x] On signup, create a new user table 'forms' to save users forms data in it, this can also be done when new user creates a form for the first time for optimization purposes.
- [x] Treat requests of type add
- [ ] Treat requests of type edit
- [ ] Treat requests of type  remove a form

## FRONT-END

- [ ] Add 'classement' question type.
- [ ] Create a component for each question type, and use them to create the final quizz.
- [ ] Fix editing a form bug: submit should be disabled if form doesn't have any active changes
- [x] Fix editing a form bug: when submitting after editing an existing form, a new form is created which is wrong, the right use of edit is to modify the edited form and not create a new one.
- [ ] Show messages after sign-in and sign-up attempts.
- This can only be done after finishing the [back-end](#BACK-END) tasks:
- [x] Redirect user to home page after successful login
- and
- [ ] load his existing data by parsing the backend response.
- [ ] Send request to API in order to add, edit, or remove a form



