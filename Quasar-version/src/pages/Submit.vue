<template>
  <div>
    <div v-if="formData" class="q-pa-md" :style="formStyle">
      <h1 class="q-mb-md">{{ formData.forms[0].title }}</h1>
      <form class="q-form" @submit.prevent="submitForm">
        <div v-for="(question, index) in formData.forms[0].questions" :key="index" class="q-mb-md">
          <label class="q-mb-sm">{{ question.modelQ }}</label>
          <br>
          <component :is="question.component" v-model="question.response.value" :options="question.options" class="q-input" />
        </div>
        <div class="q-mb-md">
          <label class="q-mb-sm">Email Address</label>
          <input type="email" v-model="userEmail" class="q-input" required />
        </div>
        <q-btn label="Submit" type="submit" class="q-mt-lg" />
      </form>
    </div>
    <div v-else>
      <input type="file" accept=".json" id="json-input" @change="loadFormData" />
    </div>
  </div>
</template>
<script>
import RadioList from "../components/RadioList.vue";
import { ref } from 'vue';
import axios from 'axios';

export default {
  name: "formResponse",
  components: {
    RadioList,
  },
  data() {
    return {
      questionOptions: [1, 2, 3, 4, 5],
      formData: null,
      formStyle: "",
      userEmail: "",
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
                question.component = "textarea";
                question.options = this.questionOptions;
                break;
              case "checkbox":
                question.component = "input";
                question.options = this.questionOptions;
                break;
              case "radio":
                question.component = "RadioList";
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
        this.formStyle = "";
      };
    },
    async submitForm() {
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
  },
};
</script>