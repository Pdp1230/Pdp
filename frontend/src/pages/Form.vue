<template>
  <form @submit.prevent="submitForm">
  <div v-if="form" :style="form.style">
    <h1 class="row justify-center">{{ form.title }}</h1>
    
      <div class="row justify-center q-ml-md">
        <q-card class="bg-grey-3 col-md-5 col-sm-10 col-xs-10 col-lg-6 col-xl-6">
          <q-card-section class="q-ml-md justify-start">
            <q-input
              type="name"
              placeholder="Fullname"
              v-model="name"
            />
          </q-card-section>
          <q-card-section class="q-ml-md justify-start">
            <q-input
              type="email"
              placeholder="Email"
              v-model="email"
            />
          </q-card-section>
        </q-card>
      </div>
      <div
        v-for="answer in answers"
        :key="answer.index"
        class="q-my-md"
      >
        <div class="row justify-center q-ml-md">
          <q-card
            class="bg-grey-3 col-md-5 col-sm-10 col-xs-10 col-lg-6 col-xl-6"
          >
            <q-card-section class="row justify-center">
              {{ form.questions[answer.index - 1].modelQ }}
            </q-card-section>
            <q-card-section class="q-ml-md justify-start">
              <div class="bg-grey-1">
                <div v-if="answer.type === 'text'">
                  <q-input v-model="answer.text" />
                </div>
                <div v-if="answer.type === 'textarea'">
                  <q-input type="textarea" v-model="answer.textArea" />
                </div>
                <div v-if="answer.type === 'radio'" class="q-pa-lg" >
                  
                  <q-option-group
                    v-model="answer.radioChoice"
                    :options="form.questions[answer.index - 1].options"
                    color="primary"
                    @options-updated="updateOptions(answer.index)"
                  />
                </div>
                <div v-if="answer.type === 'checkbox'" class="q-pa-lg">
                  <q-option-group
                    v-model="answer.checkboxChoices"
                    :options="form.questions[answer.index - 1].options"
                    color="green"
                    type="checkbox"
                  />
                </div>
                <div v-if="answer.type === 'ranking'" class="q-pa-lg" >
                  <span v-if="form.questions[answer.index - 1].numberOfOptionsToClassify > 1">You have to classify {{ form.questions[answer.index - 1].numberOfOptionsToClassify }} options</span>
                  <span v-if="form.questions[answer.index - 1].numberOfOptionsToClassify <= 1">You have to classify {{ form.questions[answer.index - 1].numberOfOptionsToClassify }} option</span>
                  <DraggableOptionGroup
                    v-model="answer.selected"
                    :options="form.questions[answer.index - 1].options"
                    :numberOfOptionsToClassify="form.questions[answer.index - 1].numberOfOptionsToClassify"
                    @sorted-options-updated="onSortedOptionsUpdated"
                    type="ranking"

                  />
                </div>
                <div v-if="answer.type === 'select'" class="q-gutter-md">
                  <q-select
                    filled
                    v-model="answer.selectChoice"
                    :options="form.questions[answer.index - 1].options"
                    emit-value
                    map-options
                  />
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="row justify-center q-my-xl">
        <q-btn v-if="currentNumberOfOptionsToClassify() == 0" type="submit" label="Submit" />
        <q-btn v-if="currentNumberOfOptionsToClassify() != 0" type="submit" :disabled="!isSortedOptionsValid" label="Submit" />
        <q-btn icon="ios_share" @click="exportToCSV" title='Export to csv' />
    </div>
    </div>
    </form>
  
</template>

<script>
import api from "src/api/api";
import DraggableOptionGroup from "../components/DraggableOption.vue"
import { ref } from "vue";

export default {
  name: "formResponse",
  props: ["id"],
  components: {
    DraggableOptionGroup
  },
  data() {
    return {
      form: null,
      answers: [],
      email: "",
      name: "",
      formStyle: "",
      isSortedOptionsValid: false,
      numberOfOptionsToClassify: 0,
      rowsCsv: [],
    };
  },
  mounted() {
    this.loadForm();
  },
  methods: {


    onSortedOptionsUpdated(isValid) {
      this.isSortedOptionsValid = isValid;

    },
    currentNumberOfOptionsToClassify() {
      return this.numberOfOptionsToClassify;
    },

    async exportToCSV() {
      const header = ["Email Address", "Full Name"];
      // Add header for each question
      this.form.questions.forEach((question) => {
        header.push(`${question.modelQ}:type(${question.type})`);
      });
      const start = [this.email, this.name]; // create new row for each answer
      // Create CSV file
      const csv = [header.join(",") + "\n", ...this.rowsCsv].join("");
      const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });

      // Download CSV file
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.setAttribute("href", url);
      link.setAttribute("download", `${this.form.title}.csv`);
      link.style.visibility = "hidden";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    }
    ,
    async loadForm() {
      try {
        const response = await api.get(`/form/getform/${this.id}`);
        this.form = response.data;

        const styleElem = document.createElement("style");
        styleElem.id = "form-style";
        styleElem.innerHTML = this.form.style.replace(/\n/g, "");
        styleElem.innerHTML = styleElem.innerHTML.replace(/form\{/g, "");
        styleElem.innerHTML = styleElem.innerHTML.replace(/\}/g, "");
        document.head.appendChild(styleElem);
        this.form.questions.forEach((question) => {
          question.options = question.options.map((option) => ({
            label: option.modelQ,
            value: option.index,
          }));
          switch (question.type) {
            case "text":
              this.answers.push({
                index: question.index,
                type: question.type,
                text: "",
              });
              break;
            case "textarea":
              this.answers.push({
                index: question.index,
                type: question.type,
                textArea: "",
              });
              break;
            case "ranking":
              this.numberOfOptionsToClassify = question.numberOfOptionsToClassify;
              this.answers.push({
                index: question.index,
                type: question.type,
                numberOfOptionsToClassify: question.numberOfOptionsToClassify,
                textarea: "",
              });
              break;
            case "radio":
              this.answers.push({
                index: question.index,
                type: question.type,
                radioChoice: 0,
              });
              break;
            case "checkbox":
              this.answers.push({
                index: question.index,
                type: question.type,
                checkboxChoices: [],
              });
              break;
            case "select":
              this.answers.push({
                index: question.index,
                type: question.type,
                selectChoice: 1,
              });
              break;
            default:
              break;
          }
        });
      } catch (error) {
        console.log(error.message);
        this.$router.push({ name: "Error404" });
      }
    },
    sendEmail() {
      const formData = this.form;



      const response = this.answers.map((answer, index) => {
        const question = formData.questions[index];
        if (answer.type == "ranking") {
          const rankinganswer = []
          this.form.questions[answer.index - 1].options.forEach((option) => {
            const value = option.rank || "null";
            const label = option.label;
            rankinganswer.push(`${label} rank : ${value} `);
          });
          return `${question.modelQ}: ${rankinganswer}`
        }


        return `${question.modelQ}: ${answer.text ||
          answer.textArea ||
          answer.radioChoice ||
          answer.checkboxChoices || 
          answer.selectChoice || 
          answer.selected}`;
      }).join(';' + '\n');

      const emailRequestOwner = {
        toEmail: sessionStorage.getItem('userEmail'),
        subject: "Réponse au formulaire : " + formData.title,
        body: this.name + " à répondue au formulaire ses réponses sont : \n" + response,
      };

      const emailRequestUser = { toEmail: this.email, subject: 'Form awnser', body: 'Thanks for awnsering our form' };

      api.post('/email/send', emailRequestUser)
        .then(response => {
          console.log(response);
        })
        .catch(error => {
          console.log(error);
        });
      api.post('/email/send', emailRequestOwner)
        .then(response => {
          console.log(response);
        })
        .catch(error => {
          console.log(error);
        });


    },

    addtocsv() {


      const start = [this.email, this.name]; // create new row for each answer
      // Add row for each response
      this.answers.forEach((answer) => {
        const row = [];

        // Add answer for each question
        if (answer.type === "ranking") {
          this.form.questions[answer.index - 1].options.forEach((option) => {
            const value = option.rank || "null";
            const label = option.label;
            row.push(`${label}:${value}`);
          });
        } else {
          const value = answer.selectChoice || answer.checkboxChoices || answer.radioChoice || answer.text || answer.textArea || "";
          row.push(value);
        }

        start.push(row.join(","));
      });
      this.rowsCsv.push(start + "\n")

    },

    clearForm() {
    this.email = '';
    this.name = '';
      
    },
    submitForm() {
      this.sendEmail();
      this.addtocsv();
      // Clear the input values
      this.clearForm();


    },
  },
};
</script>