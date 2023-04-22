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
              :disable="disabled"
            />
          </q-card-section>
          <q-card-section class="q-ml-md justify-start">
            <q-input
              type="email"
              placeholder="Email"
              v-model="email"
              :disable="disabled"
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
                  <q-input v-model="answer.text" :disable="disabled" />
                </div>
                <div v-if="answer.type === 'textarea'">
                  <q-input type="textarea" v-model="answer.textArea" :disable="disabled"/>
                </div>
                <div v-if="answer.type === 'radio'" class="q-pa-lg" >
                  <q-option-group
                    v-model="answer.radioChoice"
                    :options="form.questions[answer.index - 1].options"
                    color="primary"
                    @options-updated="updateOptions(answer.index)"
                    :disable="disabled"
                  />
                </div>
                <div v-if="answer.type === 'checkbox'" class="q-pa-lg">
                  <q-option-group
                    v-model="answer.checkboxChoices"
                    :options="form.questions[answer.index - 1].options"
                    color="green"
                    type="checkbox"
                    :disable="disabled"
                  />
                </div>
                <div v-if="answer.type === 'ranking'" class="q-pa-lg" >
                  <span v-if="form.questions[answer.index - 1].numberOfOptionsToClassify > 1">You have to classify {{ form.questions[answer.index - 1].numberOfOptionsToClassify }} options</span>
                  <span v-if="form.questions[answer.index - 1].numberOfOptionsToClassify <= 1">You have to classify {{ form.questions[answer.index - 1].numberOfOptionsToClassify }} option</span>
                  <DraggableOptionGroup
                    :modelValue="answer.rankingOrder"
                    :options="form.questions[answer.index - 1].options"
                    :numberOfOptionsToClassify="form.questions[answer.index - 1].numberOfOptionsToClassify"
                    @sorted-options-updated="onSortedOptionsUpdated"
                    @sorted-options="getSortedOptions"
                    type="ranking"
                    :draggable="!disabled"
                    @update:modelValue="newValue => answer.rankingOrder = newValue"
                  />
                </div>
                <div v-if="answer.type === 'select'" class="q-gutter-md">
                  <q-select
                    filled
                    v-model="answer.selectChoice"
                    :options="form.questions[answer.index - 1].options"
                    emit-value
                    map-options
                    :disable="disabled"
                  />
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="row justify-center q-my-xl">
        <q-btn v-if="!disabled && currentNumberOfOptionsToClassify() == 0" type="submit" label="Submit"  />
        <q-btn v-if="!disabled && currentNumberOfOptionsToClassify() != 0" type="submit" :disabled="!isSortedOptionsValid" label="Submit" />
    </div>
    </div>
    </form>
  
</template>

<script>
import api from "src/api/api";
import DraggableOptionGroup from "../components/DraggableOption.vue"
import _ from "lodash";

export default {
  name: "FormResponse",
  props: {
    id: {
      type: String,
      required: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    formProp: {
      type: Object,
      default: null
    },
    answersProp: {
      type: Array,
      required: false
    },
    emailProp: {
      type: String,
      default: ""
    },
    nameProp: {
      type: String,
      default: ""
    }
  },
  components: {
    DraggableOptionGroup
  },
  data() {
    return {
      form: {
      title: "",
      questions: [],
      style: "",
    },
      answers: [],
      email: "",
      name: "",
      formStyle: "",
      isSortedOptionsValid: false,
      totalclassify:0,
      numberOfOptionsToClassify: 0,
      rowsCsv: [],
      sortedOptions: [],
    };
  },
  
  mounted() {
    if(this.disabled){
      this.form = _.cloneDeep(this.formProp);
      this.form.questions.forEach((question) => {
        question.options = question.options.map((option) => ({
          label: option.modelQ,
          value: option.index,
        }));
      });
      this.answers = _.cloneDeep(this.answersProp);
      this.email = this.emailProp;
      this.name = this.nameProp;
      console.log(this.answers);
    }
    else
      this.loadForm();
  },
  methods: {

    getSortedOptions(val){
      this.sortedOptions = val;
      this.answers.forEach((answer) =>{
        if(answer.type == "ranking"){
          
          
        }
        
      });
      
      
    },
    totalNumberOfOptionsToClassify() {
      let sum =0;
      this.form.questions.forEach((question) => {
        if (question.type === 'ranking') {
          sum +=1;
      }
        
      });
      return sum;
    },

    onSortedOptionsUpdated(isValid) {
      if(this.totalclassify<1)
      this.totalclassify = 0;

      this.totalclassify += isValid;
      this.isSortedOptionsValid= (this.totalclassify==this.totalNumberOfOptionsToClassify());

    },
    currentNumberOfOptionsToClassify(){
      return this.numberOfOptionsToClassify;
    },


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
                rankingOrder: [],
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
   

    clearForm() {
    this.email = '';
    this.name = '';


      
    },
    async postAnswers(){
      
      const data = {
        email: this.email,
        name: this.name,
        answers: this.answers
      }
      try{
        console.log(data);
        const response = await api.post(`/answer/submit/${this.id}`,data);

        if (response.status !== 200) {
          throw new Error('Posting answers failed.');
        } 
      } catch (error) {
        console.log(error.message);
      }
    },
    submitForm() {
      this.addtocsv();
      this.postAnswers();
      this.clearForm();
    },
  },
};
</script>