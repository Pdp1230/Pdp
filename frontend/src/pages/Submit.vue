<template>
  <div>
    <div v-if="formData" class="form-wrapper" :style="formStyle">
      <h1 class="form-title">{{ formData.forms[0].title }}</h1>
      <form class="form" @submit.prevent="submitForm">
        <transition-group name="question-transition" tag="div" class="question-list">
          <div v-for="(question, index) in formData.forms[0]?.questions" :key="index">
            <div
                  class="form-question"
                  :class="{ 'dragging': dragging && dragIndex === index, 'over': hovering === index }"
                  draggable="true"
                  ref="draggedQuestion"
                  :data-question-index="index"
                  @dragstart="startDrag(event, index)"
                  @dragover.prevent="dragOver(event, index)"
                  @dragend="stopDrag"
                  @drop.prevent="drop(index)"
                  @dragenter="dragEnter(index)"
                >
              <div class="drag-handle">
                <label class="form-label">{{ question.modelQ }}</label>
                <br />
                <component
                    :is="question.component"
                    v-model="question.response.value"
                    :options="question.options"
                    :questionIndex="index"
                    class="form-input"
                  >
                  <template #option="{ option }">{{ option.modelQ }}</template>
                </component>
                <div class="question-controls">
                  <q-btn class="up-button" :disabled="index === 0" icon="arrow_circle_up" @click="moveQuestionUp(index)" />
                  <q-btn class="down-button" :disabled="index === formData.forms[0].questions.length - 1" icon="arrow_circle_down" @click="moveQuestionDown(index)" />
                </div>
              </div>
            </div>
          </div>
        </transition-group>
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
//Todo classement a faire entre les questions les non classé ont toute la meme valeur et indicateur du classement a coté de la question et aussi a afficher indicateur visuel si la question est classé ou non 
//todo téléchargement d'un fichier csv contenant les réponses au formulaire formaté
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
      dragging: false,
      dragIndex: null,
      targetIndex: null,
      hovering: null,
    };
  },
  computed: {},
  methods: {
    moveQuestionUp(index) {
      const questions = this.formData.forms[0].questions;
      const temp = questions[index - 1];
      questions[index - 1] = questions[index];
      questions[index] = temp;
    },  

moveQuestionDown(index) {
  const questions = this.formData.forms[0].questions;
  const temp = questions[index + 1];
  questions[index + 1] = questions[index];
  questions[index] = temp;
},
    
    startDrag(event, index) {
  const question = this.formData.forms[0].questions[index];
  this.dragging = true;
  this.dragIndex = index;
  this.draggedQuestion = {
    ...question,
    response: { ...question.response }
  };

  // update selectedChoice state for RadioList component
  const radioListIndex = question.options.findIndex(o => o === question.response.value);
  this.group[radioListIndex].selectedChoice = question.response.value;

  event.target.classList.add("dragging");
},

dragEnter(index) {
  this.hovering = index;
},
stopDrag(event) {
  this.dragging = false;
  this.dragIndex = null;
  this.hovering = null;


  event.target.classList.remove("dragging");
},
dragOver(event, index) {
  event.preventDefault();
  if (!this.dragging) {
    return;
  }

  if (index !== this.dragIndex) {
    this.targetIndex = index;
    this.hovering = true;
  } else {
    this.hovering = false;
  }

  if (this.targetIndex === null) {
    this.hovering = false;
  }

  const draggedElement = this.$refs.draggedQuestion[0].querySelector('.form-question');
  draggedElement.style.transform = `translateY(${event.offsetY}px)`;
  draggedElement.addEventListener("transitionend", () => {
    draggedElement.style.transform = `translateY(0px)`;
  }, { once: true });
  

  // Add event listener for dragleave event
  event.target.addEventListener("dragleave", () => {
    this.hovering = null;
  });
},
drop(index) {
  if (index !== this.dragIndex) {
    const questions = this.formData.forms[0].questions;
    const question = questions[this.dragIndex];
    const response = question.response.value;
    questions.splice(this.dragIndex, 1);
    questions.splice(index, 0, question);

  }

  this.dragging = false;
  this.dragIndex = null;
  this.targetIndex = null;
  this.hovering = null;
},

    loadFormData(event) {
      
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.readAsText(file);
      reader.onload = () => {
        const json = JSON.parse(reader.result);
        this.formData = json;
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
     // Todo Pouvoir classé les questions par importance si non classé 
    // RENVOYé TOUTE LES RéPONSE DANS LE DATABASE 
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
            'Authorization': `Bearer ${API_KEY}`,
            'Content-Type': 'application/json'
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
<style scoped>
 .up-button,
  .down-button {
    font-size: 0.7rem;
    padding: 0.25rem;
    margin: 0 0.25rem;
    background-color: #3498db;
    color: #fff;
    border: none;
    border-radius: 50%;
    width: 1.5rem;
    height: 1.5rem;
    display: flex;
    justify-content: center;
    align-items: center;
  }


  .up-button:hover,
  .down-button:hover {
    background-color: #2980b9;
    cursor: pointer;
  }





.form-question{
  transition: transform 0.2s ease-in-out;
}

.form-question.dragging {
  opacity: 0.5;
}
  .question-list {
    display: flex;
    flex-direction: column;
    align-items: stretch;
  }
  .question-transition-enter-active, .question-transition-leave-active {
    transition: all 0.2s ease-out;
  }
  .question-transition-enter, .question-transition-leave-to {
    opacity: 0;
    transform: translateY(10px);
  }
  .form-question.dragging {
    opacity: 0.5;
    transform: rotate(2deg) scale(1.05);
    transition: transform 0.3s ease-out;
  }
  .form-question:hover {
  background-color: #d9edf7;
  transition: background-color 0.3s ease-in-out;
}

  .form-wrapper {
  width: 80%;
  margin: 0 auto;
  /* add other styles as needed */
}

</style>