<!-- eslint-disable vue/no-use-computed-property-like-method -->
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
    <q-list dense>
  <q-item v-for="form in forms" :key="form.id">
    <q-item-section>
      <q-item-label>{{ form.title }}</q-item-label>
      <q-item-label caption>{{ form.email }}</q-item-label>
    </q-item-section>
    <q-item-section side>
      <q-btn icon="edit"   @click="editForm(form)" />
      <q-btn icon="delete" @click="deleteForm(form.id)" />
      <q-btn icon="share"  @click="shareForm(form.url)"/>

    </q-item-section>
  </q-item>
</q-list>

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
          <q-input 
            v-model="email" 
            filled type="email"
            placeholder="Email" 
            hint="obligatory"
            class="q-ml-md" />
          <q-btn
            dense
            icon="style"
            label="Change Style"
            @click="changeFormStyle = !changeFormStyle"
            flat
            class="q-ml-md"
            no-caps/>
        </div>
        <div class="row" >
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
        <div v-if="changeFormStyle" class="row justify-bottom q-mt-lg q-mb-md">
          <textarea 
          id="textcss"
          v-model="style" 
          class="q-input col-md-4 col-sm-12 col-xs-12 col-lg-3 col-xl-3"  
          placeholder="      /* Customize your form here */
                                .q-input {
                                  /* Add custom styles */
                                }
                                .q-card {
                                  /* Add custom styles */
                                }
                                /* Add more custom styles as needed */"
          ></textarea>
          <div id="textbuttons">
            <q-btn 
            dense icon="style" 
            label="Add CSS template" 
            class="q-ml-md"
            @click="addCssTemplate" />

            <q-btn
            dense
            icon="clear"
            class="q-ml-md"
            @click="clearStyle"
            />
          </div>
        </div>

        <div class="q-ml-md">
          <div v-for="question in questions" :key="question.index">
            <div class="flex row q-mt-lg">
              <q-input
                v-model="question.modelQ"
                hint="you need to fill in the question"
                :placeholder="'question number ' + question.index"
                class="col-md-4 col-sm-12 col-xs-12 col-lg-3 col-xl-3"
              />
              <q-select
                v-model="question.type"
                hint="Select the question type"
                :options="['radio', 'text', 'checkbox', 'textarea','rating']"
                class="col-md-2 col-sm-6 col-xs-6 col-lg-2 col-xl-2 q-ml-md"
                @update:model-value="updateOptionsArray(question.index,question.type)"
              />
              <q-btn
                v-if="question.cptOptions === 0 && (question.type==='radio' || question.type==='checkbox')"
                dense
                icon="add"
                label="addOption"
                @click="addOption(question.index)"
                flat
                no-caps
              />
            </div>
            <div v-if="question.type==='radio' || question.type==='checkbox'">
              <div class="row q-my-md" v-for="option in question.options" :key="option.index">
                <q-input
                  v-model="option.modelQ"
                  hint="you need to fill in the option"
                  :placeholder="'option number ' + option.index"
                  class="col-md-4 col-sm-8 col-xs-10 col-lg-3 col-xl-3 bg-grey-1 rounded-borders"
                />
                <q-btn
                  v-if="option.index === question.cptOptions && (question.type==='radio' || question.type==='checkbox')"
                  flat
                  dense
                  icon="add"
                  label="addOption"
                  @click="addOption(question.index)"
                  no-caps
                />
                <q-btn
                  flat
                  dense
                  icon="delete"
                  label="deleteOption"
                  @click="deleteOption(question.index,option.index)"
                  no-caps
                />
              </div>
            </div>
            <div class="row">
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
              :disable="!titleValid || !questionsValid || !emailValid" 
            />
            <q-tooltip v-if="!titleValid">
              you need to write a title first
            </q-tooltip>
            <q-tooltip v-if="!emailValid">
              you need to fill your email first
            </q-tooltip>
            <q-tooltip v-if="titleValid && emailValid && !questionsValid">
              you need to finish your questions first
            </q-tooltip>
            <q-tooltip v-if="titleValid && !emailValid && !questionsValid">
              you need to fill your email first
            </q-tooltip>
          </div>
        </div>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import { v4 as uuidv4 } from "uuid";
export default {
  name: "formCreator",
  data() {
    return {
      forms: [],
      questions: [],
      qadd: false,
      title: "",
      email:"",
      style:"",
      type:"",
      cptQuestion: 0,
      dialogForm: false,
      formData: {},
      changeFormStyle: false,
      formUrl: "",
    };
  },
  computed: {
    titleValid() {
      return this.title !== "";
    },
    emailValid() {
      const emailPattern = /^(?=[a-zA-Z0-9@._%+-]{6,254}$)[a-zA-Z0-9._%+-]{1,64}@(?:[a-zA-Z0-9-]{1,63}\.){1,8}[a-zA-Z]{2,63}$/;
      return emailPattern.test(this.email);
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
      this.title = "";
      this.email = "";
      this.questions = [];
      this.formId = uuidv4();
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
        type:'radio',
        options: [],
        cptOptions: 0,
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
    updateOptionsArray(questionIndex,questionType){
      if(questionType !== 'radio' && questionType !== 'checkbox'){
        this.questions[questionIndex-1].options = [];
        this.questions[questionIndex-1].cptOptions = 0;
      }
    },
    addOption(index){
      this.questions[index-1].cptOptions += 1;
      this.questions[index-1].options.push({
        index: this.questions[index-1].cptOptions,
        modelQ: "",
      });
    },
    deleteOption(indexQuestion, indexOption) {
      let cpt = 1;
      this.questions[indexQuestion-1].options = this.questions[indexQuestion-1].options
        .filter((option) => option.index !== indexOption)
        .map((option) => ({
          ...option,
          index: cpt++,
        }));
      this.questions[indexQuestion-1].cptOptions -= 1;
    },
    shareForm(url) {
      if (navigator.share) {
        navigator.share({
          url: url
        });
      } else {
        window.prompt("Copy the URL below to share the form:", url);
      } 
    },
    addCssTemplate() {
  const cssTemplate = `

    form {
     font-family: Arial, sans-serif;
          background-color: #f2f2f2;
        padding: 20px;
        border-radius: 10px;
          box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
        }

        label {
          display: block;
          margin-bottom: 10px;
          font-weight: bold;
        }

        input[type="text"],
        textarea {
          width: 100%;
          padding: 10px;
          border: none;
          border-radius: 5px;
          background-color: #ffffff;
          margin-bottom: 20px;
          box-sizing: border-box;
        }

        button {
          background-color: #4CAF50;
          color: white;
          border: none;
          padding: 10px;
          border-radius: 5px;
          cursor: pointer;
        }

        button:hover {
          background-color: #3e8e41;
        }

  `;
  if(this.style=="")
  this.style += cssTemplate;
  },
  clearStyle() {
    this.style = "";
  },
      submitForm() {
           const uuid = require("uuid");
          const formId = uuid.v4();
          const url = window.location.origin + '/form/' + formId;
          

          this.formUrl = url;
          this.forms.push({
            title: this.title,
            id: this.formId,
            style: this.style,
            ownersemail: this.ownersemail,
            email: this.email,
            url: url,
            questions: [...this.questions],
          });
          // Ajouter un type par défaut "radio" pour chaque question qui n'a pas de type défini
          this.questions.forEach(question => {
            if (!question.type) {
              question.type = this.type;
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
        editForm(form) {
  this.dialogForm = true;
  this.formId = form.id;
  this.title = form.title;
  this.email = form.email;
  this.style = form.style;
  this.questions = form.questions;
}
,
deleteForm(formId) {
  this.forms = this.forms.filter((form) => form.id !== formId);
},


    },
  };
  </script>

  <style scoped>

  #textbuttons{
    width: 350px;

  }
  textarea {
  margin-top: 10px;
  margin-left: 50px;
  width: 500px;
  height: 100px;

  background: none repeat scroll 0 0 rgba(0, 0, 0, 0.07);
  border-color: -moz-use-text-color #FFFFFF #FFFFFF -moz-use-text-color;
  border-image: none;
  border-radius: 6px 6px 6px 6px;
  border-style: none solid solid none;
  border-width: medium 1px 1px medium;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.12) inset;
  color: #555555;
  font-family: Helveticaf;
  font-size: 1em;
  line-height: 1.4em;
  padding: 5px 8px;
  transition: background-color 0.2s ease 0s;
  scrollbar-color: #ccc #f5f5f5;
  scrollbar-width: thin;
}


#textcss::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

#textcss::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 3px;
}

#textcss::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

textarea:focus {
    background: none repeat scroll 0 0 #FFFFFF;
    outline-width: 0;
}


  </style>