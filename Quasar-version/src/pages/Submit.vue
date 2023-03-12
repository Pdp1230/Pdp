<template>
  <div>
    <div v-if="formData" class="q-pa-md" :style="formStyle">
      <h1 class="q-mb-md">{{ formData.forms[0].title }}</h1>
      <form class="q-form">
        <div v-for="(question, index) in formData.forms[0].questions" :key="index" class="q-mb-md">
          <label class="q-mb-sm">{{ question.modelQ }}</label>
          <br>
          <component :is="question.component" v-model="question.response.value" :options="question.options" class="q-input" />
        </div>
        <q-btn label="Submit" class="q-mt-lg" />
      </form>
    </div>
    <div v-if="formData"></div>
    <input type="file" accept=".json" id="json-input">
  </div>
</template>
<script>
import RadioList from "../components/RadioList.vue";
import { ref } from 'vue';

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
    };
  },
  mounted() {
    document.getElementById("json-input").addEventListener("change", (event) => {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.readAsText(file);
      reader.onload = () => {
        const json = reader.result;
        this.formData = JSON.parse(json);
        this.formData.forms.forEach((form) => {
          // Supprimer l'élément de style précédent avant d'en ajouter un nouveau
          const prevStyleElem = document.querySelector("#form-style");
          if (prevStyleElem) {
            document.head.removeChild(prevStyleElem);
          }
          const styleElem = document.createElement("style");
          styleElem.id = "form-style";
          styleElem.innerHTML = form.style.replace(/\n/g, "");
          document.head.appendChild(styleElem);
          form.questions.forEach((question) => {
            switch (question.type) {
              case "text":
              case "textarea":
              case "checkbox":
                question.component = "input";
                break;
              case "radio":
                question.component = "RadioList";
                question.options = this.questionOptions;
                break;
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
    });
  },
  methods: {},
};
</script>