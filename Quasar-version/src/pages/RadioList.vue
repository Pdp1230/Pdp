<template>
    <div>
      <div v-for="(choice, index) in choices" :key="index">
        <q-radio v-model="selectedChoice" :val="choice" :id="index" />
        <q-label :for="index">{{ choice }}</q-label>
      </div>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue';
  
  export default {
    props: {
      choices: {
        type: Array,
        required: true,
      },
    },
    setup(props) {
      const selectedChoice = ref(null);
  
      return {
        selectedChoice,
      };
    },
  };
  </script>
  