<template>
    <div>
      <div class="row justify-center q-mt-lg">
        <q-btn size="md" icon="add" color="blue-8" dense round @click="addForm">
        </q-btn>
      </div>
      <div class="row justify-center">
        <p class="text-caption text-grey-8">
          Click on this button in order to add a form
        </p>
      </div>
  
      <q-dialog
        v-model="dialogForm"
        maximized
        transition-show="slide-up"
        transition-hide="slide-down"
      >
        <q-card class="bg-grey-2">
          <div class="row justify-end">
            <q-btn
              flat
              dense
              round
              color="red"
              icon="close"
              @click="closeDialog"
            />
          </div>
          <div class="row col-12 q-ml-md">
            <q-input
              placeholder="Titre du formulaire"
              hint="obligatory"
              v-model="title"
              class="col-md-4 col-sm-12 col-xs-12 col-lg-3 col-xl-3"
            />
            <q-btn
              v-if="questions.length === 0"
              dense
              icon="add"
              label="addQuestion"
              @click="addQuestion"
              flat
              class="q-ml-md"
              no-caps
            />
          </div>
          <div class="q-ml-md">
            <div v-for="question in questions" :key="question.index">
              <div class="flex row">
                <q-input
                  v-model="question.modelQ"
                  hint="you need to fill in the question"
                  :placeholder="'question number ' + question.index"
                  class="col-md-4 col-sm-12 col-xs-12 col-lg-3 col-xl-3"
                />
                <q-btn
                  v-if="question.index === questions[questions.length - 1].index"
                  flat
                  dense
                  icon="add"
                  label="addQuestion"
                  @click="addQuestion"
                  no-caps
                />
                <q-btn
                  flat
                  dense
                  icon="delete"
                  label="deleteQuestion"
                  @click="deleteQuestion(question.index)"
                  no-caps
                />
              </div>
              <!--<q-separator class ="q-mt-md q-mb-md" v-if="question.index !== questions[questions.length - 1].index" color="black"/>-->
            </div>
            <div class="row justify-center q-mt-lg q-mb-md">
              <q-btn
                icon="save"
                label="Submit the form"
                color="green"
                @click="submitForm"
                :disable="!titleValid || !questionsValid"
              />
              <q-tooltip v-if="!titleValid">
                you need to write a title first
              </q-tooltip>
              <q-tooltip v-if="titleValid && !questionsValid">
                you need to finish your questions first
              </q-tooltip>
            </div>
          </div>
        </q-card>
      </q-dialog>
    </div>
  </template>
  
  <script>
  export default {
    name: "formCreator",
    data() {
      return {
        forms: [],
        questions: [],
        qadd: false,
        title: "",
        cptQuestion: 0,
        dialogForm: false,
        formData: {}
      };
    },
    computed: {
      titleValid() {
        return this.title !== "";
      },
      questionsValid() {
        return (
          this.questions.length !== 0 &&
          this.questions.filter((question) => question.modelQ === "").length === 0
        );
      },
    },
    methods: {
      addForm() {
        this.dialogForm = true;
      },
      closeDialog() {
        this.dialogForm = false;
        this.title = "";
        this.questions = [];
        this.cptQuestion = 0;
      },
      addQuestion() {
        this.cptQuestion += 1;
        this.questions.push({
          index: this.cptQuestion,
          modelQ: "",
        });
      },
      deleteQuestion(index) {
        let cpt = 1;
        this.questions = this.questions
          .filter((question) => question.index !== index)
          .map((question) => ({
            ...question,
            index: cpt++,
          }));
        this.cptQuestion -= 1;
      },
      submitForm() {
  this.forms.push({
    title: this.title,
    questions: [...this.questions],
  });

  // Ajouter un type par défaut "radio" pour chaque question qui n'a pas de type défini
  this.questions.forEach(question => {
    if (!question.type) {
      question.type = "radio";
    }
  });

  this.formData = {
    forms: this.forms
  };

  const fileName = `${this.title.replace(/ /g, "-").toLowerCase()}.json`;
  const fileContent = JSON.stringify(this.formData, null, 2);
  const fileBlob = new Blob([fileContent], { type: "application/json" });

  const fileLink = document.createElement("a");
  fileLink.href = URL.createObjectURL(fileBlob);
  fileLink.download = fileName;
  fileLink.click();

  this.title = "";
  this.questions = [];
  this.dialogForm = false;
  this.formAdd = false;
  this.cptQuestion = 0;

  const formDataUrl = URL.createObjectURL(fileBlob);
  fetch(formDataUrl)
    .then(response => response.json())
    .then(data => {
      this.formData = data.forms[0]; // Récupérer le premier formulaire dans le tableau de formulaires
      this.questions = this.formData.questions;
      this.questions.forEach(question => {
        if (!question.type) {
          question.type = "radio";
        }
      });
    });
},



    },
  };
  </script>