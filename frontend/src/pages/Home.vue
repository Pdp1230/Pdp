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
      <q-item class="row" v-for="form in forms" :key="form.index">
        <q-item-section class="col-1">
          <q-item-label>{{ form.title }}</q-item-label>
        </q-item-section>
        <q-item-section side>
          <div class ="row q-my-md">
            <q-btn icon="edit" @click="editForm(form)" />
            <q-btn icon="delete" @click="deleteForm(form.index)" />
            <q-btn icon="share" @click="shareForm(form.url)" />
            <q-btn icon="download" @click="downloadFormJson(form)"/>
          </div>
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
        <div class="row col-12 q-ml-md justify-center">
          <q-input
            placeholder="Titre du formulaire"
            hint="obligatory"
            v-model="actualForm.title"
            class="col-md-4 col-sm-12 col-xs-12 col-lg-3 col-xl-3"
          />
          <q-btn
            dense
            icon="style"
            label="Change Style"
            @click="changeFormStyle = !changeFormStyle"
            flat
            class="q-ml-md"
            no-caps
          />
        </div>
        <div v-if="changeFormStyle" class="row q-mt-lg q-mb-md justify-center">
          <textarea
            id="textcss"
            v-model="actualForm.style"
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
              dense
              icon="style"
              label="Add CSS template"
              class="q-ml-md"
              @click="addCssTemplate"
            />
            <q-btn dense icon="clear" class="q-ml-md" @click="clearStyle" />
          </div>
        </div>

        <div
          class="q-mt-xl"
          v-for="question in actualForm.questions"
          :key="question.index"
        >
          <div class="row justify-center q-mt-md q-ml-md">
            <q-card
              class="bg-grey-3 col-md-5 col-sm-10 col-xs-10 col-lg-6 col-xl-6"
            >
              <q-card-section class="q-mt-sm q-ml-md justify-center">
                <div class="row q-my-sm">
                  <q-input
                    v-model="question.modelQ"
                    hint="you need to fill in the question"
                    :placeholder="'question number ' + question.index"
                    class="col-md-8 col-sm-11 col-xs-11 col-lg-7 col-xl-7"
                  />
                  <q-select
                    v-model="question.type"
                    hint="Select the question type"
                    :options="options"
                    class="col-md-2 col-sm-6 col-xs-6 col-lg-2 col-xl-2 q-ml-md"
                    @update:model-value="
                      updateOptionsArray(question.index, question.type)
                    "
                  />
                </div>
              </q-card-section>
              <div
                v-if="question.type === 'radio' || question.type === 'checkbox'"
              >
                <div v-for="option in question.options" :key="option.index">
                  <q-card-section class="q-ml-md justify-start">
                    <div class="row q-my-sm">
                      <q-input
                        v-model="option.modelQ"
                        hint="you need to fill in the option"
                        :placeholder="'option number ' + option.index"
                        class="col-md-8 col-sm-11 col-xs-11 col-lg-7 col-xl-7 rounded-borders"
                      />
                      <q-btn
                        v-if="question.options.length > 1"
                        class = "q-ml-md"
                        flat
                        dense
                        icon="delete"
                        label="deleteOption"
                        @click="deleteOption(question.index, option.index)"
                        no-caps
                      />
                    </div>
                    <div class="row q-mt-sm">
                      <q-btn
                        v-if="
                          option.index === question.cptOptions &&
                          (question.type === 'radio' ||
                            question.type === 'checkbox')
                        "
                        flat
                        dense
                        icon="add"
                        label="addOption"
                        @click="addOption(question.index)"
                        no-caps
                      />
                    </div>
                  </q-card-section>
                </div>
              </div>
              <div class="row justify-center q-my-lg">
                <q-btn
                  v-if="actualForm.questions.length > 1"
                  flat
                  dense
                  icon="delete"
                  label="deleteQuestion"
                  @click="deleteQuestion(question.index)"
                  no-caps
                  class="row"
                />
              </div>
            </q-card>
          </div>
          <div class="row justify-center q-mt-lg q-ml-md">
            <q-btn
              v-if="question.index === actualForm.questions[actualForm.questions.length - 1].index"
              flat
              dense
              icon="add"
              label="addQuestion"
              @click="addQuestion"
              no-caps
            />
          </div>
        </div>
        <div class="row justify-center q-my-xl">
          <q-btn
            v-if="isAddForm"
            icon="save"
            label="Submit the form"
            color="green"
            @click="submitForm"
            :disable="!titleValid || !questionsValid"
          />
          <q-btn
            v-if="isEditForm"
            icon="edit"
            label="Edit the form"
            color="green"
            @click="submitEditForm"
            :disable="!hasActiveChanges || !titleValid || !questionsValid"
          />
<!--todo: if question type has options, options should be valid in order to submit-->

          <q-tooltip v-if="isEditForm && !hasActiveChanges">
            you need to edit the form first
          </q-tooltip>
          <q-tooltip v-if="!titleValid">
            you need to write a title first
          </q-tooltip>
          <q-tooltip v-if="titleValid && !questionsValid">
            you need to finish your questions first
          </q-tooltip>
        </div>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import { v4 as uuidv4 } from 'uuid';
import api from 'src/api/api';
import _ from "lodash";

export default {
  name: "formCreator",
  data() {
    return {
      forms: [],
      actualForm: this.initializeForm(),
      actualEditForm: {},
      dialogForm: false,
      isAddForm: false,
      isEditForm: false,
      cptForms: 0,
      changeFormStyle: false,
      options: [
                      'radio',
                      'text',
                      'checkbox',
                      'textarea',
                      'rating',
                    ]
    }
  },
  mounted(){
    this.loadData();
  },
  computed: {
    titleValid() {
      return this.actualForm.title !== "";
    },
    questionsValid() {
      return (
        this.actualForm.questions.length !== 0 &&
        this.actualForm.questions.filter((question) => question.modelQ === "").length === 0
      );
    },
    hasActiveChanges() {
      return !_.isEqual(this.actualForm, this.actualEditForm);
    }
  },
  methods: {
    addForm() {
      this.isAddForm = true;
      this.dialogForm = true;
    },
    initializeForm(){
      return {
        title : "",
        style : "",
        cptQuestions : 1,
        questions : [
          {
            index: 1,
            modelQ: "",
            type: "radio",
            cptOptions: 1,
            options: [
              {
                index: 1,
                modelQ: "",
              },
            ],
          },
        ]
      }
    },
    closeDialog() {
      this.actualForm = this.initializeForm();
      this.dialogForm = false;
      this.isAddForm = false;
      this.isEditForm = false;
    },
    addQuestion() {
      this.actualForm.cptQuestions += 1;
      this.actualForm.questions.push({
        index: this.actualForm.cptQuestions,
        modelQ: "",
        type: "radio",
        cptOptions: 1,
        options: [
          {
            index: 1,
            modelQ: "",
          },
        ],
      });
    },
    deleteQuestion(index) {
      let cpt = 1;
      this.actualForm.questions = this.actualForm.questions
        .filter((question) => question.index !== index)
        .map((question) => ({
          ...question,
          index: cpt++,
        }));
      this.actualForm.cptQuestions -= 1;
    },
    updateOptionsArray(questionIndex, questionType) {
      if (questionType !== "radio" && questionType !== "checkbox") {
        this.actualForm.questions[questionIndex - 1].options = [];
        this.actualForm.questions[questionIndex - 1].cptOptions = 0;
      } else if (this.actualForm.questions[questionIndex - 1].options.length === 0) {
        this.actualForm.questions[questionIndex - 1].options = [
          {
            index: 1,
            modelQ: "",
          },
        ];
        this.actualForm.questions[questionIndex - 1].cptOptions = 1;
      }
    },
    addOption(index) {
      this.actualForm.questions[index - 1].cptOptions += 1;
      this.actualForm.questions[index - 1].options.push({
        index: this.actualForm.questions[index - 1].cptOptions,
        modelQ: "",
      });
    },
    deleteOption(indexQuestion, indexOption) {
      let cpt = 1;
      this.actualForm.questions[indexQuestion - 1].options = this.actualForm.questions[
        indexQuestion - 1
      ].options
        .filter((option) => option.index !== indexOption)
        .map((option) => ({
          ...option,
          index: cpt++,
        }));
      this.actualForm.questions[indexQuestion - 1].cptOptions -= 1;
    },
    shareForm(url) {
      if (navigator.share) {
        navigator.share({
          url: url,
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
      if (this.style == "") this.style += cssTemplate;
    },
    clearStyle() {
      this.style = "";
    },
    submitForm() {
      this.actualForm.url = window.location.href.split("?")[0] + "?form=" + uuidv4();
      this.cptForms += 1;
      this.actualForm.index = this.cptForms;
      this.saveForm(this.actualForm);
      /*if (form.index > -1) {
        if (JSON.stringify(form) === JSON.stringify(this.forms[formIndex])) {
          alert("No changes were made to the form.");
          return;
        }
        this.forms.splice(formIndex, 1, form);
      } else {*/
      this.forms.push(this.actualForm);
      this.actualForm = this.initializeForm();
      this.isAddForm = false;
      this.dialogForm = false;
      //}
    },
    downloadFormJson(form){
      const fileName = `${form.title.replace(/ /g, "-").toLowerCase()}.json`;
      const fileContent = JSON.stringify(form, null, 2);
      const fileBlob = new Blob([fileContent], { type: "application/json" });
      const fileLink = document.createElement("a");
      fileLink.href = URL.createObjectURL(fileBlob);
      fileLink.download = fileName;
      fileLink.click();
      /*const formDataUrl = URL.createObjectURL(fileBlob);
      fetch(formDataUrl)
        .then((response) => response.json())
        .then((data) => {
          this.formData = data.forms[0]; // Get the first form in the array of forms
        });*/
    },
    editForm(form) {
      this.actualEditForm = _.cloneDeep(form);
      this.actualForm = _.cloneDeep(form);
      this.isEditForm = true;
      this.dialogForm = true;
    },
    submitEditForm() {
      //todo api call update form
      this.forms[this.actualForm.index-1] = _.cloneDeep(this.actualForm);
      this.actualForm = this.initializeForm();
      this.actualEditForm = {};
      this.isEditForm = false;
      this.dialogForm = false;
    },
    deleteForm(index) {
      let cpt = 1;
      this.forms = this.forms.filter((form) => form.index !== index)
                              .map((form) => ({
                                ...form,
                                index: cpt++
                              }));
      this.cptForms -= 1;
    },
    async loadData() {
      const authToken = sessionStorage.getItem('authToken');

      if (authToken != null) {
        try {
          const response = await api.get('/form/get', {
            headers: {
              'Authorization': `Bearer ${authToken}`,
              'Content-Type': 'application/json'
            },
          });

          if (response.status !== 200) {
            throw new Error('Posting form failed.');
          } 
          else {
            this.forms = response.data.forms;
            this.cptForms = this.forms.length;
          }
        } catch (error) {
          this.$router.push({ name: "Login" });
          console.log(error.message);
        }
      }
      else{
        this.$router.push({ name: "Login" });
      }
    },
    async saveForm(form){
      const authToken = sessionStorage.getItem('authToken');

      if (authToken != null) {
        try {
          console.log(JSON.stringify(form));
          const response = await api.post('/form/post',form,{
              headers: {
                Authorization : `Bearer ${authToken}`,
                'Content-Type' : 'application/json'
              }
          });

          if (response.status !== 200) {
            throw new Error('Posting form failed.');
          } 
        } catch (error) {
          console.log(error.message);
        }
      }
      else{
        this.$router.push({ name: "Login" });
      }
    }
  },
};
</script>

<style scoped>
#textbuttons {
  width: 350px;
}
textarea {
  margin-top: 10px;
  margin-left: 50px;
  width: 500px;
  height: 100px;

  background: none repeat scroll 0 0 rgba(0, 0, 0, 0.07);
  border-color: -moz-use-text-color #ffffff #ffffff -moz-use-text-color;
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
  background: none repeat scroll 0 0 #ffffff;
  outline-width: 0;
}
</style>