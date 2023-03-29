# TODO LIST


## BACK-END

- [x] On signup, send a HTTP code, for success or failure
- [x] On signin, send a HTTP code, for success or failure
- [ ] Create new forms architecture, in order to treat users forms data
- [ ] On signup, create a new user table 'forms' to save users forms data in it, this can also be done when new user creates a form for the first time for optimization purposes.
- [ ] On login, send user's forms data as a response in order for the front to process it.
- [ ] Treat POST requests of type add, edit, or remove a form

## FRONT-END

- [ ] Create a component for each question type, and use them in the home dialog window.
- [ ] Fix editing a form bug: submit should be disabled if form doesn't have any active changes
- [ ] Fix editing a form bug: when submitting after editing an existing form, a new form is created which is wrong, the right use of edit is to modify the edited form and not create a new one.
- [ ] Show messages after sign-in and sign-up attempts.
- This can only be done after finishing the [back-end](#BACK-END) tasks:
- [ ] Redirect user to home page after successful login, and load his existing data by parsing the backend response into question components.
- [ ] Send request to API in order to add, edit, or remove a form



