<template>
  <div v-if="form">
    <h1 class="row justify-center">{{ form.title }}</h1>
    <form @submit.prevent="submitForm">
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
                  <q-input type="textarea" v-model="answer.textarea" />
                </div>
                <div v-if="answer.type === 'radio'" class="q-pa-lg">
                  <q-option-group
                    v-model="answer.radio"
                    :options="form.questions[answer.index - 1].options"
                    color="primary"
                    @options-updated="updateOptions(answer.index)"
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
                <div v-if="answer.type === 'ranking'" class="q-pa-lg">
                  <span>You have to classify {{form.questions[answer.index - 1].numberOfOptionsToClassify}} options</span>
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
        <button type="submit" :disabled="!isSortedOptionsValid" >Submit</button>
    </div>
    </form>
  </div>
</template>

<script>
import api from "src/api/api";
import DraggableOptionGroup from "../components/DraggableOption.vue"
import { ref } from "vue";

export default {
  name: "formResponse",
  props: ["id"],
  components:{
    DraggableOptionGroup
  },
  data() {
    return {
      form: null,
      answers: [],
      email: "",
      name: "",
      isSortedOptionsValid:false,
    };
  },
  mounted() {
    this.loadForm();
  },
  methods: {
    onSortedOptionsUpdated(isValid) {
      this.isSortedOptionsValid = isValid;
},
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
              case "ranking":
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