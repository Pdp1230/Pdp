<template>
  <div>
    <div v-if="formData">
      <h1>{{ formData.forms[0].title }}</h1>
      <form>
        <div v-for="(question, index) in formData.forms[0].questions" :key="index">
          <label>{{ question.modelQ }}</label>
          <br>
          <component :is="question.component" v-model="question.response.value" :options="question.options" />
        </div>
      </form>
    </div>
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
      formData: null
    };
  },
  mounted() {
    document.getElementById('json-input').addEventListener('change', (event) => {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.readAsText(file);

      reader.onload = () => {
        const json = reader.result;
        this.formData = JSON.parse(json);
        this.formData.forms.forEach((form) => {
          form.questions.forEach((question) => {
            switch(question.type) {
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
                value: null
              };
            }
          });
        });
      };
    });
  }
};
</script>
