<!-- eslint-disable vue/no-use-computed-property-like-method -->
<template>
  <div>
    <div class ="row justify-center">
      <div class="q-mr-md">
        <div class="row justify-center q-mt-lg">
          <q-btn size="md" icon="add" color="blue-8" dense round @click="addForm">
        </q-btn>
        </div>
        <div class="row justify-center">
          <p class="text-caption text-grey-8">
            Click to create a form
          </p>
        </div>
      </div>
      <div class="q-ml-md">
        <div class="row justify-center q-mt-lg">
          <q-btn size="md" icon="upload" color="blue-8" dense round @click="loadForm">
        </q-btn>
        </div>
        <div class="row justify-center">
          <p class="text-caption text-grey-8">
            Click to upload a form
          </p>
        </div>
      </div>
    </div>
    <q-list dense>
      <q-item class="row justify-center" v-for="form in forms" :key="form.index">
        <q-card class = "q-py-md q-px-lg q-my-sm card-border bg-grey-2">
          <q-item-section>
            <q-item-label class = "row justify-center">{{ form.title }}</q-item-label>
          </q-item-section>
          <q-item-section>
            <div class ="row q-my-md bg-white">
              <q-btn icon="edit" @click="editForm(form)" />
              <q-btn icon="delete" @click="deleteForm(form.index,form.id)"/>
              <q-btn icon="download" @click="downloadFormJson(form)"/>
              <q-btn icon="assignment" @click="form.resultsShown = !form.resultsShown"/>
              <q-btn icon="share" @click="shareForm(form.id)" />
              <q-btn v-if="form.resultsShown" icon="source" @click="exportToCsv(form)" />
              
            </div>
          </q-item-section>
          <div v-if="form.resultsShown">
            <q-item class="row justify-center" v-for="answer in form.answers" :key="answer.index">
              <q-card class="bg-grey-4 card-answer-border" style="max-width: 500px; width: 100%;">
                <q-card-section class="row items-center">
                  <q-item-section class="col-auto">
                    <q-item-label>{{ answer.email }}</q-item-label>
                    <q-item-label>{{ answer.name }}</q-item-label>
                  </q-item-section>
                  <q-item-section class="text-right">
                    <div>
                      <q-btn class="bg-white" dense size="md" icon="preview" @click="displayAnswers(form, answer.answers, answer.email, answer.name)" />
                    </div>
                  </q-item-section>
                </q-card-section>
              </q-card>
            </q-item>
          </div>
        </q-card>
      </q-item>
    </q-list>

    <q-dialog
      v-model="dialogAnswers"
      maximized
      transition-show="slide-up"
      transition-hide="slide-down"
      no-esc-dismiss
    >
      <div class="bg-grey-2">
        <div class="row justify-end">
          <q-btn
            flat
            dense
            round
            color="red"
            icon="close"
            @click="closeAnswersDialog"
          />
        </div>
        <FormResponse 
          :disabled="true"
          :formProp="formAnswersDisplay"
          :answersProp="answersDisplay"
          :emailProp="emailAnswersDisplay"
          :nameProp="nameAnswersDisplay"
        />
      </div>
    </q-dialog>

    <q-dialog
      v-model="dialogForm"
      maximized
      transition-show="slide-up"
      transition-hide="slide-down"
      no-esc-dismiss
    >
      <q-card class="bg-grey-2">
        <div class="row justify-end">
          <q-btn
            flat
            dense
            round
            color="red"
            icon="close"
            @click="closeFormDialog"
          />
        </div>
        <div class="row col-12 q-ml-md justify-center">
  <q-card class="card-border bg-grey-3 col-md-5 col-sm-10 col-xs-10 col-lg-6 col-xl-6">
    <q-card-section class="q-mt-sm q-mx-md justify-center">
      <q-input
        placeholder="Titre du formulaire"
        hint="obligatory"
        v-model="actualForm.title"
        class="col-md-4 col-sm-12 col-xs-12 col-lg-3 col-xl-3"
      />
    </q-card-section>
    <q-card-section class="row q-mt-sm q-mx-md justify-center">
      <q-btn
        dense
        icon="style"
        label="Change Style"
        @click="changeFormStyle = !changeFormStyle"
        flat
        class="q-ml-md"
        no-caps
      />
    </q-card-section>
    <q-card-section class="row q-mt-sm q-mx-md justify-center">
        <q-card v-if ="changeFormStyle" class="row q-pa-md bg-grey-2">
          <textarea
            id="textcss"
            v-model="style"
            class="q-input justify-center"
            style="width: 85%; height: 200px;"
            placeholder="/* Customize your form here */
              .q-input {
                /* Add custom styles */
              }
              .q-card {
                /* Add custom styles */
              }
              /* Add more custom styles as needed */"
          ></textarea>
          <div class="row q-mt-md q-justify-center">
            <q-btn
              dense
              icon="style"
              label="Add CSS template"
              class="q-ml-md"
              @click="addCssTemplate"
            />
            <q-btn dense icon="clear" class="q-ml-md" @click="clearStyle" />
          </div>
        </q-card>
    </q-card-section>
  </q-card>
</div>

        

        <div
          class="q-mt-xl"
          v-for="question in actualForm.questions"
          :key="question.index"
        >
          <div class="row justify-center q-mt-md q-ml-md">
            <q-card
              class="card-border bg-grey-3 col-md-5 col-sm-10 col-xs-10 col-lg-6 col-xl-6"
            >
              <q-card-section class="q-mt-sm q-ml-md justify-center">
                <div class="row q-my-sm q-ml-md">
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
                <q-input
                  v-if="question.type==='ranking'"
                  class="q-ma-md"
                  v-model.number="question.numberOfOptionsToClassify"
                  hint="Number of options to classify"
                  mask="#"
                  fill-mask="0"
                  reverse-fill-mask
                  type="number"
                  filled
                  lazy-rules
                  style="max-width: 200px"
                  :rules="[
                    (val) =>
                      (val <= question.options.length && val > 0) ||
                      'Please type a valid number of options to classify',
                  ]"
                />
              </q-card-section>
              <div
                v-if="question.type === 'radio' || question.type === 'checkbox' || question.type === 'select' ||
                            question.type === 'ranking'"
              >
                <div v-for="option in question.options" :key="option.index">
                  <q-card-section class="q-mt-sm q-ml-md justify-start">
                    <div class="row q-my-sm q-ml-md">
                      <q-input
                        v-model="option.modelQ"
                        hint="you need to fill in the option"
                        :placeholder="'option number ' + option.index"
                        class="col-md-8 col-sm-11 col-xs-11 col-lg-7 col-xl-7"
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
                    <div class="row q-mt-md q-ml-md">
                      <q-btn
                        v-if="
                          option.index === question.cptOptions &&
                          (question.type === 'radio' ||
                            question.type === 'checkbox' ||
                            question.type === 'select' ||
                            question.type === 'ranking')
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
        <div class="row justify-center">
          <div v-if="isAddForm" class="q-my-xl">
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
          <div v-if="isEditForm" class="q-my-xl">
            <div class="row justify-center q-gutter-sm">
              <q-checkbox
                v-model="editTerms"
                label="Answers will be deleted, do you agree?"
              />
            </div>
            <div class="row justify-center">
              <q-btn
                icon="edit"
                label="Edit the form"
                color="green"
                @click="submitEditForm"
                :disable="!hasActiveChanges || !titleValid || !questionsValid || !editTermsAccepted"
              />
              <q-tooltip v-if="isEditForm && !editTermsAccepted">
             you need to accept the editing terms
            </q-tooltip>
            <q-tooltip v-if="isEditForm && editTermsAccepted && !hasActiveChanges">
              you need to edit the form first
            </q-tooltip>
            <q-tooltip v-if="!titleValid">
            you need to write a title first
          </q-tooltip>
          <q-tooltip v-if="titleValid && !questionsValid">
            you need to finish your questions first
          </q-tooltip>
            </div>
          </div>          
        </div>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import { v4 as uuidv4 } from 'uuid';
import api from 'src/api/api';
import _ from "lodash";
import FormResponse from "./Form.vue"

export default {
  name: "formCreator",
  components: {
    FormResponse
  },
  data() {
    return {
      forms: [],
      actualForm: this.initializeForm(),
      actualEditForm: {},
      dialogForm: false,
      dialogAnswers: false,
      isAddForm: false,
      isEditForm: false,
      editTerms: false,
      cptForms: 0,
      style: "",
      changeFormStyle: false,
      options: [
                      'ranking',
                      'radio',
                      'text',
                      'checkbox',
                      'textarea',
                      'select',
                    ],
      formAnswersDisplay : {},
      answersDisplay : [],
      emailAnswersDisplay : "",
      nameAnswersDisplay : "",
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
    },
    editTermsAccepted() {
      return this.editTerms;
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
        id : "",
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
            numberOfOptionsToClassify: 0
          },
        ],
      }
    },
    closeFormDialog() {
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
      if (questionType !== "radio" && questionType !== "checkbox" && questionType !== "select" && questionType !== "ranking") {
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
      if(questionType === 'ranking')
        this.actualForm.questions[questionIndex - 1].numberOfOptionsToClassify = 1;
      else
        this.actualForm.questions[questionIndex - 1].numberOfOptionsToClassify = 0;
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
    shareForm(id) {
      if (navigator.share) {
        navigator.share({
          url: window.location.href.split("?")[0].replace('home','') + "form/" + id,
        });
      } else {
        window.prompt("Copy the URL below to share the form:", 
        window.location.href.split("?")[0] + "form/" + id);
      }
    },

    loadForm() {
      const input = document.createElement("input");
      input.type = "file";
      input.accept = ".json";
      input.addEventListener("change", async (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.readAsText(file, "UTF-8");
        reader.onload = async (readerEvent) => {
          const json = readerEvent.target.result;
          try {
            const form = JSON.parse(json);
            const newForm = Object.assign({}, form);
            this.actualForm = newForm;
            this.style = newForm.style;
            this.dialogForm = true;
            this.isAddForm = true;

            const style = document.createElement("style");
            style.textContent = newForm.styles;
            document.head.appendChild(style);
          } catch (error) {
            console.error(error);
          }
        };
      });
      input.click();
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
      this.actualForm.id = uuidv4();
      this.cptForms += 1;
      this.actualForm.index = this.cptForms;
      this.actualForm.style = this.style;
      this.saveForm(this.actualForm);
      this.forms.push(this.actualForm);
      this.actualForm = this.initializeForm();
      this.isAddForm = false;
      this.dialogForm = false;
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
   
      this.forms[this.actualForm.index-1] = _.cloneDeep(this.actualForm);
      const authToken = sessionStorage.getItem('authToken');
      api.put(`/form/edit/${this.actualForm.id}`,this.actualForm,{
                  headers: {
                    Authorization : `Bearer ${authToken}`,
                    'Content-Type' : 'application/json'
                  }
                });
      this.actualForm = this.initializeForm();
         
         
      
      this.actualEditForm = {};
      this.isEditForm = false;
      this.dialogForm = false;
      window.location.reload();
    },
    deleteForm(index,id) {
      let cpt = 1;
      this.forms = this.forms.filter((form) => form.index !== index)
                              .map((form) => ({
                                ...form,
                                index: cpt++
                              }));
      this.cptForms -= 1;
      const authToken = sessionStorage.getItem('authToken');
      api.delete(`/form/delete/${id}`,{
                  headers: {
                    Authorization : `Bearer ${authToken}`,
                    'Content-Type' : 'application/json'
                  }});
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
        sessionStorage.removeItem('userEmail');
        this.$router.push({ name: "Login" });
      }
    },
    async exportToCsv(form) {
    // Fetch the responses from the API
    const answers = form.answers;

    // Create the CSV content
    let csvContent = "Name;Email;";
    csvContent += form.questions.map((question) => question.modelQ + " type :" +question.type ).join("; ");
    csvContent += "\n";

    answers.forEach((response) => {
      csvContent += `${response.name}; ${response.email}; `;

      response.answers.forEach((answer) => {
        if(answer.type == "ranking"){
          csvContent += answer.rankingOrder.map((item) => item.label + " ranked " + item.rank).join(", ");
        }
        else{ csvContent += answer.type || "";}
       
        csvContent += "; "
      });

      csvContent += "\n";
    });

    // Download the CSV file
    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = `${form.title} - Responses.csv`;
    link.click();
  },
    displayAnswers(form,answers,email,name){
      this.formAnswersDisplay = _.cloneDeep(form);
      this.answersDisplay = _.cloneDeep(answers);
      this.emailAnswersDisplay = email;
      this.nameAnswersDisplay = name;
      this.dialogAnswers = true;
    },
    closeAnswersDialog(){
      this.dialogAnswers = false;
      this.formAnswersDisplay = {};
      this.answersDisplay = [];
      this.emailAnswersDisplay = "";
      this.nameAnswersDisplay = "";
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
  font-family: 'Courier New', Courier, monospace;
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

.card-border {
  border-left: 3px solid #0d47a1;
}

.card-answer-border {
  border-left: 3px solid #212831;
}

</style>