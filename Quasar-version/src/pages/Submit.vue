<template>
  <div>
    <div v-if="formData" class="form-wrapper" :style="formStyle">
      <h1 class="form-title">{{ formData.forms[0].title }}</h1>
      <form class="form" @submit.prevent="submitForm">
        <div v-for="(question, index) in formData.forms[0].questions" :key="index" class="form-question">
          <label class="form-label">{{ question.modelQ }}</label>
          <br>
          <component :is="question.component" v-model="question.response.value" :options="question.options"
            class="form-input">
            <template #option="{ option }">
              {{ option.modelQ }}
            </template>
          </component>
        </div>
        <div class="form-email">
          <label class="form-label">Email Address</label>
          <input type="email" v-model="userEmail" class="form-input" required />
        </div>
        <q-btn label="Submit" type="submit" class="form-submit" />
      </form>
    </div>
    <div v-else>
      <input type="file" accept=".json" id="json-input" @change="loadFormData" />
    </div>
  </div>
</template>

<script>
import RadioList from "../components/RadioList.vue";
import CheckboxList from "../components/CheckboxList.vue";
import { ref } from 'vue';
import axios from 'axios';

const API_KEY = 'IH79QZN58E4AWj9cnyiVS';
const SENDGRID_ENDPOINT = 'https://api.sendgrid.com/v3/mail/send';
export default {
  name: "formResponse",
  components: {
    RadioList,
    CheckboxList,
  },
  data() {
    return {
      questionOptions: [],
      formData: null,
      formStyle: "",
      userEmail: "",
      group: [],
    };
  },
  methods: {
    loadFormData(event) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.readAsText(file);
      reader.onload = () => {
        const json = reader.result;
        this.formData = JSON.parse(json);
        const optionsMap = new Map();
        this.formData.forms.forEach((form) => {
          const styleElem = document.createElement("style");
          styleElem.id = "form-style";
          styleElem.innerHTML = form.style.replace(/\n/g, "");
          document.head.appendChild(styleElem);
          form.questions.forEach((question) => {
            switch (question.type) {
              case "select":
                question.component = "select";
                question.options = this.questionOptions;
                break;
              case "textarea":
                question.component = "q-textarea";
                question.options = this.questionOptions;
                break;
              case "checkbox":
                question.component = "CheckboxList";
                if (!question.response) {
                  question.response = {
                    value: null,
                  };
                }
                if (question.options && question.options.length > 0) {
                  question.options = question.options.map((option) => option.modelQ);
                }
                break;
              case "radio":
                question.component = "RadioList";
                if (!question.response) {
                  question.response = {
                    value: null,
                  };
                }
                if (question.options && question.options.length > 0) {
                  question.options = question.options.map((option) => option.modelQ);
                }
                break;
              case "rating":
                question.component = "q-rating";
                question.options = this.questionOptions;
                break;
              default:
                throw new Error(`Unknown question type: ${question.type}`);
            }
            if (!question.response) {
              question.response = {
                value: null,
              };
            }
          });
        });
        // create the questionOptions array
        this.questionOptions = Array.from(optionsMap).map(([modelQ, options]) => {
          return {
            modelQ,
            options,
          };
        }).map(option => option.modelQ);
        // set the options property for each question object
        this.formData.forms.forEach((form) => {
          form.questions.forEach((question) => {
            if (question.options && question.options.length > 0) {
              const options = this.questionOptions.find((qo) => qo.modelQ === question.modelQ);
              if (options) {
                question.options = options.options;
              }
            }
          });
        });
        this.formStyle = "";
      };
    },

    async submitFormbackend() {
      const formData = this.formData.forms[0];
      const data = formData.questions.reduce((acc, question) => {
        acc[question.modelQ] = question.response ? question.response.value : null;
        return acc;
      }, {});
      try {
        //Il faut implémenté coté backend l'envoye de mail et la gestion serveur ou bien utiliser un API d'envoye de mail
        const response = await axios.post('/api/sendmail', {
          ownerEmail: formData.ownersemail,
          userEmail: this.userEmail,
          formTitle: formData.title,
          formData: data
        });
        console.log(response.data);
      } catch (error) {
        console.error(error);
        throw new Error("Error sending email.");
      }
    },
    async submitFormFrontend() {
      const formData = this.formData.forms[0];
      const data = formData.questions.reduce((acc, question) => {
        acc[question.modelQ] = question.response ? question.response.value : null;
        return acc;
      }, {});
      const msg = {
        to: formData.ownersemail,
        from: this.userEmail,
        subject: formData.title,
        text: JSON.stringify(data),
      };
      try {
        // Make a POST request to the SendGrid API endpoint with the email data and API key
        const response = await axios.post(SENDGRID_ENDPOINT, msg, {
          headers: {
            Authorization: `Bearer ${API_KEY}`,
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
          },
        });

        // Log the response from the API
        console.log(response.data);
      } catch (error) {
        // Log any errors that occur
        console.error(error);
      }
    },
  },
};
</script>