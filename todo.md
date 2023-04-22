# TODO LIST


## BACK-END

- [x] On signup, send a HTTP code, for success or failure
- [x] On signin, send a HTTP code, for success or failure
- [x] Create new forms architecture, in order to treat users forms data
- [x] On signup, create a new user table 'forms' to save users forms data in it, this can also be done when new user creates a form for the first time for optimization purposes.
- [x] Treat requests of type add
- [x] (Oussama+Firas) Treat new question type ranking.
- [x] (Oussama+Firas) Treat answers of all types.
- [x] (Oussama) Create new endpoint for getting answers of a form
- [x] Treat requests of type edit
- [x] Treat requests of type  remove a form
- [x] Fix bug if style isn't empty the form is not handeled

## FRONT-END

- [x] Fix editing a form bug: submit should be disabled if form doesn't have any active changes
- [x] Fix editing a form bug: when submitting after editing an existing form, a new form is created which is wrong, the right use of edit is to modify the edited form and not create a new one.
- [x] Redirect user to home page after successful login
- [x] load user's existing data by parsing the backend response.
- [x] Send request to API in order to add,
- [x] Edit, or remove a form
- [ ] Show messages after sign-in and sign-up attempts.
- [x] Add import form from JSON functionnality.
- [x] (Firas) Add 'ranking' question type to Home.vue.
- [x] (Ismail) Add RankingQuestion component, props are: options: Array of {label: string, index: integer} that can be dragged and dropped when displaying, and numberOfElementsToClassify: integer to display a separator
- [x] (Firas) Integrate RankingQuestion in Form.vue
- [x] (Ismail) If style exists apply it to Form.vue(style is stocked in this.form.style)
- [x] (Firas+Oussama) Show answers of a given form.
