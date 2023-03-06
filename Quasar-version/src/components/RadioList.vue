<template>
  <div  class="radio-list-container">
    <div v-for="(choice, index) in options" :key="index">
      <q-radio v-model="selectedChoice" :val="choice" :id="index" />
      <q-label :for="index">{{ choice }}</q-label>
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
    const selectedChoice = ref(null);

    return {
      selectedChoice,
    };
  },
};
</script>
<style>
.radio-list-container {
  display: flex;
  flex-direction: row;
  align-items: center;
}   
</style>