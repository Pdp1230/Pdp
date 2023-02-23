<script setup lang="ts">
import { ref } from 'vue';

const props = defineProps({
  choices: {
    type: Array,
    required: true,
  },
});

const selectedChoice = ref(null);
</script>

<template>
  <div>
    <div v-for="(choice, index) in choices" :key="index">
      <input type="radio" :id="index" :value="choice" v-model="selectedChoice">
      <label :for="index">{{ choice }}</label>
    </div>
  </div>
</template>

<style scoped>

</style>
