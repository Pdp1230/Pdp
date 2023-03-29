<template>
    <div class="checkbox-list-container">
      <div v-for="(option, index) in options" :key="index">
        <q-checkbox v-model="selectedOptions" :val="option" :id="'checkbox-' + index" />
        <q-label :for="'checkbox-' + index">{{ option }}</q-label>
      </div>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue';
  
  export default {
    props: {
      options: {
        type: Array,
        required: true,
      },
    },
    setup(props) {
      const selectedOptions = ref([]);
  
      return {
        selectedOptions,
      };
    },
  };
  </script>
  
  <style>
  .checkbox-list-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }
  </style>
  