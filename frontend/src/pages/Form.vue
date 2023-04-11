<template>
  <div v-if="form">
    <h1 class="row justify-center">{{ form.title }}</h1>
    <form @submit.prevent="submitForm">
      <div
        v-for="answer in answers"
        :key="answer.index"
        class="q-my-md q-mx-md"
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
                  <q-input type="textarea" v-model="answer.textarea" />
                </div>
                <div v-if="answer.type === 'radio'" class="q-pa-lg">
                  <q-option-group
                    v-model="answer.radio"
                    :options="form.questions[answer.index - 1].options"
                    color="primary"
                  />
                </div>
                <div v-if="answer.type === 'checkbox'" class="q-pa-lg">
                  <q-option-group
                    v-model="answer.selected"
                    :options="form.questions[answer.index - 1].options"
                    color="green"
                    type="checkbox"
                  />
                </div>
                <div v-if="answer.type === 'select'" class="q-gutter-md">
                  <q-select
                    filled
                    v-model="answer.select"
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
        <button type="submit">Submit</button>
    </div>
    </form>
  </div>
</template>

<script>
import api from "src/api/api";
import { ref } from "vue";

export default {
  name: "formResponse",
  props: ["id"],
  data() {
    return {
      form: null,
      answers: [],
    };
  },
  mounted() {
    this.loadForm();
  },
  methods: {
    async loadForm() {
      try {
        const response = await api.get(`/form/getform/${this.id}`);
        this.form = response.data;

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
                textarea: "",
              });
              break;
            case "radio":
              this.answers.push({
                index: question.index,
                type: question.type,
                radio: 0,
              });
              break;
            case "checkbox":
              this.answers.push({
                index: question.index,
                type: question.type,
                selected: [],
              });
              break;
            case "select":
              this.answers.push({
                index: question.index,
                type: question.type,
                select: 1,
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
    submitForm() {
      console.log(this.form);
      console.log(this.answers);
    },
  },
};
</script>